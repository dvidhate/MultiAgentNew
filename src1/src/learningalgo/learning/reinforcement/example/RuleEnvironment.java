package learningalgo.learning.reinforcement.example;

import java.util.LinkedHashSet;
import java.util.Set;

import learningalgo.agent.Action;
import learningalgo.agent.Agent;
import learningalgo.agent.EnvironmentState;
import learningalgo.agent.Percept;
import learningalgo.agent.impl.AbstractEnvironment;
import learningalgo.environment.rule.Rile;
import learningalgo.environment.rule.RuleDataAction;
import learningalgo.probability.mdp.TransitionProbabilityFunction;
import learningalgo.util.Randomizer;

/**
 * Implementation of the Cell World Environment, supporting the execution of
 * trials for reinforcement learning agents.
 *
 *
 *
 */
public class RuleEnvironment extends AbstractEnvironment {

    private Rile<Double> startingCell = null;
    private Set<Rile<Double>> allStates = new LinkedHashSet<Rile<Double>>();
    private TransitionProbabilityFunction<Rile<Double>, RuleDataAction> tpf;
    private Randomizer r = null;
    private RuleEnvironmentState currentState = new RuleEnvironmentState();

    /**
     * Constructor.
     *
     * @param startingCell the cell that agent(s) are to start from at the
     * beginning of each trial within the environment.
     * @param allStates all the possible states in this environment.
     * @param tpf the transition probability function that simulates how the
     * environment is meant to behave in response to an agent action.
     * @param r a Randomizer used to sample actions that are actually to be
     * executed based on the transition probabilities for actions.
     */
    public RuleEnvironment(Rile<Double> startingCell,
            Set<Rile<Double>> allStates,
            TransitionProbabilityFunction<Rile<Double>, RuleDataAction> tpf,
            Randomizer r) {
        this.startingCell = startingCell;
        this.allStates.addAll(allStates);
        this.tpf = tpf;
        this.r = r;
    }

    /**
     * Execute N trials.
     *
     * @param n the number of trials to execute.
     */
    public void executeTrials(int n) {
        System.out.println("Excute N trials " + n);

        for (int i = 0; i < n; i++) {

            executeTrial();
        }
    }

    /**
     * Execute a single trial.
     */
    public void executeTrial() {
        currentState.reset();
        for (Agent a : agents) {
            a.setAlive(true);
            currentState.setAgentLocation(a, startingCell);
        }
        stepUntilDone();
    }

    @Override
    public EnvironmentState getCurrentState() {
        return currentState;
    }

    @Override
    public EnvironmentState executeAction(Agent agent, Action action) {
        if (!action.isNoOp()) {
            Rile<Double> s = currentState.getAgentLocation(agent);
            double probabilityChoice = r.nextDouble();
            double total = 0;
            boolean set = false;
            for (Rile<Double> sDelta : allStates) {
                total += tpf.probability(sDelta, s, (RuleDataAction) action);
                if (total > 1.0) {
                    throw new IllegalStateException("Bad probability calculation.");
                }
                if (total > probabilityChoice) {
                    currentState.setAgentLocation(agent, sDelta);
                    set = true;
                    break;
                }
            }
            if (!set) {
                throw new IllegalStateException("Failed to simulate the action=" + action + " correctly from s=" + s);
            }
        }

        return currentState;
    }

    @Override
    public Percept getPerceptSeenBy(Agent anAgent) {
        return currentState.getPerceptFor(anAgent);
    }
}
