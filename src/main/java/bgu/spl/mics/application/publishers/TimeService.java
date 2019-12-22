package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
	private int duration;
	private int tickNumber;

	public TimeService(int duration) {
		super("TimeService");
		// TODO Implement this
		this.duration = duration;
	}

	@Override
	protected void initialize() {
		// TODO Implement this
		System.out.println("TimeService -> start()");
	}

	@Override
	public void run() {
		// TODO ALON: impl 20.12
		initialize();
		tickNumber = 1;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				getSimplePublisher().sendBroadcast(new TickBroadcast(tickNumber, tickNumber == duration));
				if (tickNumber == duration)
					timer.cancel();
				tickNumber++;
			}
		},0,100);

	}

}

