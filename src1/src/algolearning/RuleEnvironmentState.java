package algolearning;

import java.util.HashMap;
import java.util.Map;

import learningalgo.agent.Agent;
import learningalgo.agent.EnvironmentState;
 

/**
 * An implementation of the EnvironmentState interface for a Cell World.
 *
 *
 *
 */
public class RuleEnvironmentState implements EnvironmentState {

    private Map<Agent, RulePercept> agentLocations = new HashMap<Agent, RulePercept>();

    /**
     * Default Constructor.
     */
    public RuleEnvironmentState() {
    }

    /**
     * Reset the environment state to its default state.
     */
    public void reset() {
        agentLocations.clear();
    }

    /**
     * Set an agent's location within the cell world environment.
     *
     * @param anAgent the agents whose location is to be tracked.
     * @param location the location for the agent in the cell world environment.
     */
    public void setAgentLocation(Agent anAgent, Cell<Double> location) {
        RulePercept percept = agentLocations.get(anAgent);
        if (null == percept) {
            percept = new RulePercept(location);
            agentLocations.put(anAgent, percept);
        } else {
            percept.setCell(location);
        }
    }

    /**
     * Get the location of an agent within the cell world environment.
     *
     * @param anAgent the agent whose location is being queried.
     * @return the location of the agent within the cell world environment.
     */
    public Cell<Double> getAgentLocation(Agent anAgent) {
        return agentLocations.get(anAgent).getCell();
    }

    /**
     * Get a percept for an agent, representing what it senses within the cell
     * world environment.
     *
     * @param anAgent the agent a percept is being queried for.
     * @return a percept for the agent, representing what it senses within the
     * cell world environment.
     */
    public RulePercept getPerceptFor(Agent anAgent) {
        return agentLocations.get(anAgent);
    }
}
