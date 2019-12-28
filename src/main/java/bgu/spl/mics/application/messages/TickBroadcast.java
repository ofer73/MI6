package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

/**
 * The TickBroadcast according to the assignment. added field and a constructor to set is it the final tick
 */

public class TickBroadcast implements Broadcast {
    private final int tickNumber;
    private boolean isFinalTick;

    /**
     * constructor
     * @param tickNumber : which tick is it
     * @param isFinalTick : boolean represents if it is the final tick
     */

    public TickBroadcast(int tickNumber, boolean isFinalTick) {
       this.tickNumber = tickNumber;
       this.isFinalTick = isFinalTick;
    }

    /**
     * @return if it is the final tick
     */

    public boolean isFinalTick() { return isFinalTick; }

    /**
     * a getter
     * @return number of tick
     */

    public int getTickNumber() { return tickNumber; }
}
