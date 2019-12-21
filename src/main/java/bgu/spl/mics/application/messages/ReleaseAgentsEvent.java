package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class ReleaseAgentsEvent implements Event<String> {
    private List<String> agents;

    public List<String> getAgents() {
        return agents;
    }

    public ReleaseAgentsEvent(List<String> agents) {
        this.agents = agents;
    }
}
