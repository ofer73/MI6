package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionReceivedEvent implements Event<Boolean> {
    MissionInfo info;

    public MissionInfo getInfo() {
        return info;
    }

    public MissionReceivedEvent(MissionInfo info) {
        this.info = info;
    }

}
