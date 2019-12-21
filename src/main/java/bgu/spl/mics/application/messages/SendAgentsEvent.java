package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class SendAgentsEvent implements Event<String> {
    private List<String> agents;
    int duration;

    public int getDuration() {
        return duration;
    }

    public List<String> getAgents() {
        return agents;
    }

    public SendAgentsEvent(List<String> agents,int duration) {
        this.agents = agents;
        this.duration = duration;
    }
}
