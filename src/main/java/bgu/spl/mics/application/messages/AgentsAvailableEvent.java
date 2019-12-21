package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;
import java.util.Map;

public class AgentsAvailableEvent implements Event<Map<String,Object>> {
    private List<String>  agents;
    public AgentsAvailableEvent(List<String> agents) {
        this.agents = agents;
    }

    public List<String> getAgents() {
        return agents;
    }

    public void setAgents(List<String> agents) {
        this.agents = agents;
    }


}
