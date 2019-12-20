package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

/**
 * The TickBroadcast according to the assignment. added field and a constructor to set is it the final tick
 */

public class TickBroadcast implements Broadcast {
    private final int tickNumber;
    private boolean isFinalTick;

    public TickBroadcast(int tickNumber, boolean isFinalTick) {
       this.tickNumber = tickNumber;
       this.isFinalTick = isFinalTick;
    }

    public boolean isFinalTick() { return isFinalTick; }

    public int getTickNumber() { return tickNumber; }
//    private boolean isFinal;
//
//    public TickBroadcast(){ isFinal = false;}
//
//    /**
//     *
//     * @param isFinal
//     */
//    public TickBroadcast (boolean isFinal){
//        this.isFinal = isFinal;
//    }
//
//    public boolean isFinal() {return isFinal}
}
