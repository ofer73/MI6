package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.Map;

/**
 * an Event called by M, to check if a certain gadget is in inventory.
 * if so, it will be taken automatically from inventory.
 */

public class GadgetAvailableEvent implements Event<Map<String,Integer>> {
    private String gadgetName;

    /**
     * constructor of the Event.
     * @param gadgetName: name of the gadget
     */

    public GadgetAvailableEvent(String gadgetName) {
        this.gadgetName = gadgetName;
    }

    /**
     * @return the gadget's name
     */

    public String getGadgetName() {
        return gadgetName;
    }
}
