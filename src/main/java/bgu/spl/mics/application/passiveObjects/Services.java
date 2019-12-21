package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.subscribers.Intelligence;

import java.util.LinkedList;
import java.util.List;

public class Services {
    private int M;
    private int Moneypenny;
    private MissionInfo[][] intelligence;
    private int time;

    public int getM() {
        return M;
    }

    public int getMoneypenny() {
        return Moneypenny;
    }

    public MissionInfo[][] getIntelligence() { return intelligence; }

    public int getTime() {
        return time;
    }
}
