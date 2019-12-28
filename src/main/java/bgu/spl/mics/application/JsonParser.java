package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Services;

/**
 * JsonParser is an object to be created according to the json input.
 * contains field wich will contain and represent all the input for the program
 * afterwards we will use it to initialize the system.
 */
public class JsonParser {

    private String[] inventory;
    private Services services;
    private Agent[] squad;

    public JsonParser(){}


    /**
     * list of getters, to receive the information parsed from the input
     */

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
