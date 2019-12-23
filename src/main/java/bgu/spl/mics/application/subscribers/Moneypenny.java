package bgu.spl.mics.application.subscribers;

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
		System.out.println("Moneypenny " + serial + " initialized"); //TODO: delete before submission

		subscribeOriginal();

		//TODO: ALON 23.12 check which solution is better
		subscribeAlternative();
	}

	/**
	 * subscribing all events & broadcasts
	 */
	private void subscribeOriginal() {
		subscribeTickBroadcast();
		subscribeAgentAvailableEvent();
		subscribeSendAgentsEvent();
		subscribeReleaseAgentsEvent();
	}

	/**
	 * handling subscribing event for Moneypenny, with the alternaive solution
	 * which was given in the forum
	 */

	private void subscribeAlternative() {
		if (serial % 2 == 0) { //TODO: ALON 23.12 impl
			subscribeAgentAvailableEvent();
		} else {
			subscribeSendAgentsEvent();

			subscribeReleaseAgentsEvent();
		}
	}


	/**
	 * next 4 methods are private methods
	 * to make initialize() more readable
	 */

	private void subscribeTickBroadcast() {
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> {
			if (tick.isFinalTick()) {
				System.out.println("terminate Moneypenny " + serial + "  executed"); //TODO: delete before submission
				terminate();
			}
		});
	}

	private void subscribeReleaseAgentsEvent() {
		subscribeEvent(ReleaseAgentsEvent.class, (ReleaseAgentsEvent e) -> {
			System.out.println("Moneypenny " + serial + ": ReleaseAgentsEvent (without sending to mission)"); //TODO: delete before submission
			squad.releaseAgents(e.getAgents());
			complete(e, "ReleaseAgents -> executed");//TODO: ALON: 22.12 11:00
		});
	}

	private void subscribeSendAgentsEvent() {
		subscribeEvent(SendAgentsEvent.class, (SendAgentsEvent e) -> {
			System.out.println("Moneypenny " + serial + ": SendAgentsEvent (and released automatically)"); //TODO: delete before submission
			squad.sendAgents(e.getAgents(), e.getDuration());
			complete(e, "SendAgents -> executed");//TODO: ALON: 22.12 11:00

		});
	}

	private void subscribeAgentAvailableEvent() {
		subscribeEvent(AgentsAvailableEvent.class, (AgentsAvailableEvent e) -> {
			System.out.println("Moneypenny " + serial + ": AgentAvailableEvent -> " + e.getAgents().toString() + ""); //TODO: delete before submission

			Map<String, Object> map = new HashMap<>();
			map.put("serial", serial);
			map.put("acquired", squad.getAgents(e.getAgents()) ? 1 : 0);
			map.put("names", squad.getAgentsNames(e.getAgents())); //return a list of names (of agents)
			complete(e, map);
			System.out.println("Moneypenny " + serial + ": AgentAvailableEvent -> " + e.getAgents().toString() + " result: " + map.get("acquired")); //TODO: delete before submission


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
