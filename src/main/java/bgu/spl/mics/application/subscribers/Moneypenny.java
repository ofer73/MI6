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
		System.out.println("Monepenny " + serial + " initialized"); //TODO: delete before submission
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> {
			if (tick.isFinalTick()) {
				System.out.println("terminate Moneypenny " + serial + "  executed -> start unregister"); //TODO: delete before submission
				terminate();
			}
			});
		subscribeEvent(AgentsAvailableEvent.class,(AgentsAvailableEvent e)->{
					Map<String,Object> map=new HashMap<>();
					map.put("serial", serial);
					map.put("names",squad.getAgentsNames(e.getAgents())); //return a list of names (of agents)
					map.put("acquired", squad.getAgents(e.getAgents())? 1 : 0);
					complete(e,map);
				}
		);
		subscribeEvent(SendAgentsEvent.class,(SendAgentsEvent e)->{
			squad.sendAgents(e.getAgents(),e.getDuration());
			complete(e, "SendAgents -> executed");//TODO: ALON: 22.12 11:00

		});
		subscribeEvent(ReleaseAgentsEvent.class,(ReleaseAgentsEvent e)->{
			squad.releaseAgents(e.getAgents());
			complete(e, "ReleaseAgents -> executed");//TODO: ALON: 22.12 11:00
		});
	}
}
