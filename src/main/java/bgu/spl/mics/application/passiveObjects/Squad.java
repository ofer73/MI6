package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;

import java.util.*;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents = new HashMap<>(); //key (string) = serial number

	/**
	 * Retrieves the single instance of this class.
	 */
	private static class SquadHolder {
		private static Squad instance = new Squad();
	}
	public static Squad getInstance() {
		return SquadHolder.instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		// adding every agent in Agent to the map, the key is serial number
		for(Agent newAgent : agents){
			this.agents.put(newAgent.getSerialNumber(), newAgent);
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		synchronized (this) {
			for (String s : serials)
				agents.get(s).release();
			this.notifyAll();
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   time ticks to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		getAgents(serials);
		try {
			Thread.currentThread().sleep(time);
		}
		catch (InterruptedException e){}
		releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials){

		for(String s:serials){
			if(!agents.containsKey(s)) //check that squad contains all agents
				return false;
		}
			synchronized (this) {
				boolean allAvailable = false;
				while (!allAvailable) {
					allAvailable=true;
					for (String s : serials) { //check if all agents are available to acquire
						if (!agents.get(s).isAvailable()) {
							allAvailable = false;
							break;
						}
					}
					if(!allAvailable) { //if not we will wait
						try {
							this.wait();
						}catch (InterruptedException e){}
					}
					else{ //else acquire all
						for (String s : serials){
							agents.get(s).acquire();
						}
					}
				}
			}
			return true;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
		List<String> newList = new LinkedList<>();
		for (String serialNumber : serials){
			newList.add(this.agents.get(serialNumber).getName()); //adding the name of agent with the serialNumber
		}
	    return newList;
    }

}
