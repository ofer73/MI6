package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;
import java.util.Map;

/**
 * AgentAvailableEvent symbols a call by M to check if (@Agents) are not acquired and
 * able to call them for an operation.
 */

public class AgentsAvailableEvent implements Event<Map<String,Object>> {
    private List<String>  agents;
    int duration;

    /**
     * construvtor of the Event
     * @param agents: agents serial numbers, of agents who we want to check
     *              in squad.
     * @param duration : duration of the mission
     */

    public AgentsAvailableEvent(List<String> agents, int duration) {
        this.agents = agents;
        this.duration = duration;
    }

    /**
     * @return the duration of the mission
     */

    public int getDuration() {
        return duration;
    }

    /**
     * @return list of agent's serial numbers
     */

    public List<String> getAgents() {
        return agents;
    }

    /**
     * @param agents list of the agent's we want to check
     *  (used for testing code)
     */

    public void setAgents(List<String> agents) {
        this.agents = agents;
    }


}
