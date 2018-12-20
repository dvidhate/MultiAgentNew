package learningalgo.probability.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import learningalgo.environment.rule.Rile;
import learningalgo.environment.rule.RuleData;
import learningalgo.environment.rule.RuleDataAction;
import learningalgo.probability.mdp.ActionsFunction;
import learningalgo.probability.mdp.MarkovDecisionProcess;
import learningalgo.probability.mdp.RewardFunction;
import learningalgo.probability.mdp.TransitionProbabilityFunction;
import learningalgo.probability.mdp.impl.MDP;

/**
 *
 *
 *
 */
public class MDPFactory {

    /**
     * Constructs an MDP that can be used to generate the utility values
     * detailed in Fig 17.3.
     *
     * @param cw the cell world from figure 17.1.
     * @return an MDP that can be used to generate the utility values detailed
     * in Fig 17.3.
     */
    public static MarkovDecisionProcess<Rile<Double>, RuleDataAction> createMDPForFigure17_3(
            final RuleData<Double> cw) {

        return new MDP<Rile<Double>, RuleDataAction>(cw.getCells(),
                cw.getCellAt(1, 1), createActionsFunctionForFigure17_1(cw),
                createTransitionProbabilityFunctionForFigure17_1(cw),
                createRewardFunctionForFigure17_1());
    }

    /**
     * Returns the allowed actions from a specified cell within the cell world
     * described in Fig 17.1.
     *
     * @param cw the cell world from figure 17.1.
     * @return the set of actions allowed at a particular cell. This set will be
     * empty if at a terminal state.
     */
    public static ActionsFunction<Rile<Double>, RuleDataAction> createActionsFunctionForFigure17_1(
            final RuleData<Double> cw) {
        final Set<Rile<Double>> terminals = new HashSet<Rile<Double>>();
        terminals.add(cw.getCellAt(4, 3));
        terminals.add(cw.getCellAt(4, 2));

        ActionsFunction<Rile<Double>, RuleDataAction> af = new ActionsFunction<Rile<Double>, RuleDataAction>() {

            @Override
            public Set<RuleDataAction> actions(Rile<Double> s) {
                // All actions can be performed in each cell
                // (except terminal states)
                if (terminals.contains(s)) {
                    return Collections.emptySet();
                }
                return RuleDataAction.actions();
            }
        };
        return af;
    }

    /**
     * Figure 17.1 (b) Illustration of the transition model of the environment:
     * the 'intended' outcome occurs with probability 0.8, but with probability
     * 0.2 the agent moves at right angles to the intended direction. A
     * collision with a wall results in no movement.
     *
     * @param cw the cell world from figure 17.1.
     * @return the transition probability function as described in figure 17.1.
     */
    public static TransitionProbabilityFunction<Rile<Double>, RuleDataAction> createTransitionProbabilityFunctionForFigure17_1(
            final RuleData<Double> cw) {
        TransitionProbabilityFunction<Rile<Double>, RuleDataAction> tf = new TransitionProbabilityFunction<Rile<Double>, RuleDataAction>() {
            private double[] distribution = new double[]{0.8, 0.1, 0.1};

            @Override
            public double probability(Rile<Double> sDelta, Rile<Double> s,
                    RuleDataAction a) {
                double prob = 0;

                List<Rile<Double>> outcomes = possibleOutcomes(s, a);
                for (int i = 0; i < outcomes.size(); i++) {
                    if (sDelta.equals(outcomes.get(i))) {
                        // Note: You have to sum the matches to
                        // sDelta as the different actions
                        // could have the same effect (i.e.
                        // staying in place due to there being
                        // no adjacent cells), which increases
                        // the probability of the transition for
                        // that state.
                        prob += distribution[i];
                    }
                }

                return prob;
            }

            private List<Rile<Double>> possibleOutcomes(Rile<Double> c,
                    RuleDataAction a) {
                // There can be three possible outcomes for the planned action
                List<Rile<Double>> outcomes = new ArrayList<Rile<Double>>();

                outcomes.add(cw.result(c, a));
                outcomes.add(cw.result(c, a.getFirstRightAngledAction()));
                outcomes.add(cw.result(c, a.getSecondRightAngledAction()));

                return outcomes;
            }
        };

        return tf;
    }

    /**
     *
     * @return the reward function which takes the content of the cell as being
     * the reward value.
     */
    public static RewardFunction<Rile<Double>> createRewardFunctionForFigure17_1() {
        RewardFunction<Rile<Double>> rf = new RewardFunction<Rile<Double>>() {
            @Override
            public double reward(Rile<Double> s) {
                return s.getContent();
            }
        };
        return rf;
    }
}
