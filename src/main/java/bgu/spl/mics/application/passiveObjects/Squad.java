package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;

import java.util.List;
import java.util.Map;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents;

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
		// TODO Implement this
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		// TODO Implement this
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   time ticks to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		// TODO Implement this
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
				boolean allavailable = false;
				while (!allavailable) {
					allavailable=true;
					for (String s : serials) { //check if all agents are available to acquire
						if (!agents.get(s).isAvailable()) {
							allavailable = false;
							break;
						}
					}
					if(!allavailable) { //if not we will wait
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
        // TODO Implement this
	    return null;
    }

}
