package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;
import java.util.Map;

public class AgentsAvailableEvent implements Event<Map<String,Object>> {
    private List<String>  agents;
    int duration;

    public AgentsAvailableEvent(List<String> agents, int duration) {
        this.agents = agents;
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public List<String> getAgents() {
        return agents;
    }

    public void setAgents(List<String> agents) {
        this.agents = agents;
    }


}
