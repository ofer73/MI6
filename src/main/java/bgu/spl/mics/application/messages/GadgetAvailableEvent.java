package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.Map;

public class GadgetAvailableEvent implements Event<Map<String,Integer>> {
    private String gadgetname;

    public GadgetAvailableEvent(String gadgetname) {
        this.gadgetname = gadgetname;
    }

    public String getGadgetname() {
        return gadgetname;
    }
}
