package learningalgo.agent.impl;

import learningalgo.agent.EnvironmentState;

/**
 *
 *
 */
public class DynamicEnvironmentState extends ObjectWithDynamicAttributes
        implements EnvironmentState {

    public DynamicEnvironmentState() {

    }

    @Override
    public String describeType() {
        return EnvironmentState.class.getSimpleName();
    }
}
