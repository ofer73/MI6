package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.subscribers.Intelligence;

import java.util.LinkedList;
import java.util.List;

/**
 * Services object to be used for json input reading.
 * will contain the information of:
 * num of M, Moneypenny, Intelligence's info and the total time for the system
 */

public class Services {
    private int M;
    private int Moneypenny;
    private Intelligence[] intelligence;
    private int time;

    /**
     * @return num of M
     */

    public int getM() {
        return M;
    }

    /**
     * @return num of moneypennny
     */

    public int getMoneypenny() {
        return Moneypenny;
    }

    /**
     *
     * @return an array of the Intelligences
     */

    public Intelligence[] getIntelligence() { return intelligence; }


    /**
     * @return the total time for the system (before terminate)
     */

    public int getTime() {
        return time;
    }

}
