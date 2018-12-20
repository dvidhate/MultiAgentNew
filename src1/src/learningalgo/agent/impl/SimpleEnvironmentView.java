package learningalgo.agent.impl;

import learningalgo.agent.Action;
import learningalgo.agent.Agent;
import learningalgo.agent.EnvironmentState;
import learningalgo.agent.EnvironmentView;

/**
 * Simple environment view which uses the standard output stream to inform about
 * relevant events.
 *
 * @author Ruediger Lunde
 */
public class SimpleEnvironmentView implements EnvironmentView {

    @Override
    public void agentActed(Agent agent, Action action,
            EnvironmentState resultingState) {
        System.out.println("Agent acted: " + action.toString());
    }

    @Override
    public void agentAdded(Agent agent, EnvironmentState resultingState) {
        System.out.println("Agent added.");
    }

    @Override
    public void notify(String msg) {
        System.out.println("Message: " + msg);
    }
}
