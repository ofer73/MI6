package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

/**
 * an Event which is sent by the Intelligence.
 * contains information of the specific mission
 */

public class MissionReceivedEvent implements Event<Boolean> {
    MissionInfo info;

    /**
     * @return @MissionInfo object which contains
     * information of the specific mission
     */

    public MissionInfo getInfo() {
        return info;
    }

    /**
     * constructor
     * @param info:  information of the mission
     */

    public MissionReceivedEvent(MissionInfo info) {
        this.info = info;
    }

}
