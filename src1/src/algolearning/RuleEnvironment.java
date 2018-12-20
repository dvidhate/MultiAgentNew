package algolearning;

import algo.AgentBoolean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import learningalgo.agent.Action;
import learningalgo.agent.Agent;
import learningalgo.agent.EnvironmentState;
import learningalgo.agent.Percept;
import learningalgo.agent.impl.AbstractEnvironment;

import learningalgo.probability.mdp.TransitionProbabilityFunction;
import learningalgo.util.Randomizer;

public class RuleEnvironment extends AbstractEnvironment {

    private Cell<Double> startingCell = null;
    private Set<Cell<Double>> allStates = new LinkedHashSet<Cell<Double>>();
    private ArrayList<Cell<Double>> allStates1 = new ArrayList<Cell<Double>>();
    private TransitionProbabilityFunction<Cell<Double>, RuleDataAction> tpf;
    private Randomizer r = null;
    private RuleEnvironmentState currentState = new RuleEnvironmentState();
    AgentBoolean rec;

    public RuleEnvironment(Cell<Double> startingCell,
            Set<Cell<Double>> allStates,
            TransitionProbabilityFunction<Cell<Double>, RuleDataAction> tpf,
            Randomizer r, AgentBoolean rec) {
        this.startingCell = startingCell;
        this.rec = rec;
        this.allStates.addAll(allStates);
        this.tpf = tpf;
        this.r = r;
        Iterator it = allStates.iterator();
        while (it.hasNext()) {
            Cell<Double> c = (Cell<Double>) it.next();
            if (c.getY() == 1) {
                allStates1.add(c);
            }
        }
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
    Random rn = new Random();

    public void getSatartCell() {
        rec.initCol();
        startingCell = allStates1.get(rn.nextInt(allStates1.size()));
    }

    /**
     * Execute a single trial.
     */
    public void executeTrial() {
        currentState.reset();
        getSatartCell();
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
            Cell<Double> s = currentState.getAgentLocation(agent);
            double probabilityChoice = r.nextDouble();
            double total = 0;
            boolean set = false;
            for (Cell<Double> sDelta : allStates) {
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
