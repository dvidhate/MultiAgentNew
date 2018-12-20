package learningalgo.search.framework;

import learningalgo.agent.Action;

/**
 * Returns one for every action.
 *
 *
 */
public class DefaultStepCostFunction implements StepCostFunction {

    public double c(Object stateFrom, Action action, Object stateTo) {
        return 1;
    }
}
