package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	List<MissionInfo> infoList;
	int index;


	public Intelligence(List<MissionInfo> infoList) {
		super("Intelligence");
		//load & sort infoList:
		this.infoList = infoList;
		this.infoList.sort((MissionInfo i1,MissionInfo i2) -> i1.getTimeIssued()-i2.getTimeIssued()); //sort by issue time
		index=0;
	}

	@Override
	protected void initialize() {
		SimplePublisher publish = getSimplePublisher();
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> {
			if (tick.isFinalTick()) {
				terminate();
			} else {
				while (index < infoList.size() && infoList.get(index).getTimeIssued() == tick.getTickNumber()) {
					publish.sendEvent(new MissionReceivedEvent(infoList.get(index)));
					index++;
				}
			}

		});
	}
}
