package learningalgo.agent.impl;

import learningalgo.agent.State;

/**
 *
 */
public class DynamicState extends ObjectWithDynamicAttributes implements State {

    public DynamicState() {

    }

    @Override
    public String describeType() {
        return State.class.getSimpleName();
    }
}
