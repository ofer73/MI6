package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	List<MissionInfo> missions = new LinkedList<>();
	int index;


	public Intelligence(List<MissionInfo> missions) {
		super("Intelligence");
		//load & sort infoList:
		this.missions = missions;
		this.missions.sort((MissionInfo i1, MissionInfo i2) -> i1.getTimeIssued()-i2.getTimeIssued()); //sort by issue time
		index=0;
	}

	public List<MissionInfo> getMissions() {
		return missions;
	}

	@Override
	protected void initialize() {
		SimplePublisher publish = getSimplePublisher();
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> {
			if (tick.isFinalTick()) {
				terminate();
			} else {
				while (index < missions.size() && missions.get(index).getTimeIssued() == tick.getTickNumber()) {
					publish.sendEvent(new MissionReceivedEvent(missions.get(index)));
					index++;
				}
			}

		});
	}
}
