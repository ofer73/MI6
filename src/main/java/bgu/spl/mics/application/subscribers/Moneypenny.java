package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.HashMap;
import java.util.Map;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	private int serial;
	private Squad squad ;

	public Moneypenny(int serial) {
		super("Moneypenny");
		this.serial=serial;
		this.squad=Squad.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeOriginal();
	}

	/**
	 * subscribing all events & broadcasts
	 */
	private void subscribeOriginal() {
		subscribeTickBroadcast();
		subscribeAgentAvailableEvent();
	}



	/**
	 * next methods are private methods
	 * to make initialize() more readable
	 */

	private void subscribeTickBroadcast() {
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> {
			if (tick.isFinalTick()) {
				terminate();
			}
		});
	}

	/**
	 * private method to subscribe AgentAvailableEvent,
	 * and make register() clean and readable
	 */
	private void subscribeAgentAvailableEvent() {
		subscribeEvent(AgentsAvailableEvent.class, (AgentsAvailableEvent e) -> {
			Map<String, Object> map = new HashMap<>();
			map.put("serial", serial);
			map.put("acquired", squad.getAgents(e.getAgents()) ? 1 : 0);
			if(map.get("acquired").equals(0)){
				map.put("future",null);
				complete(e,map);
			}
			else {
				map.put("names", squad.getAgentsNames(e.getAgents())); //return a list of names (of agents)
				Future<Boolean> isSend = new Future<>();
				map.put("future", isSend);
				complete(e, map); //finished checking AvailableAgentsEvent, continue to process whether send/ release:
				if(isSend==null||((Future)map.get("future")).get()==null){
					squad.releaseAgents(e.getAgents());
					terminate();
				}
				else if (isSend.get()){ //sendAgents or releaseAgents
					squad.sendAgents(e.getAgents(),e.getDuration());
				} else {
					squad.releaseAgents(e.getAgents());
				}
			}
		});
	}

	/**
	 * getter
	 * @return serial number
	 */

	public int getSerial() {
		return serial;
	}

}
