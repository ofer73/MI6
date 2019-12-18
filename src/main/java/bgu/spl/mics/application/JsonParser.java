package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Agent;

public class JsonParser {

    private String[] inventory;
    private Services services;
    private Agent[] squad;

    public String[] getInventory(){
        return inventory;
    }
    public Services getServices(){
        return services;
    }
    public Agent[] getSquad(){
        return squad;
    }
}
