package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
	int duration;

	public TimeService(int duration) {
		super("TimeService");
		// TODO Implement this
		this.duration = duration;
		Timer timer = new Timer();
	}

//	public TimeService (int dureation , String name){
//		super(name);
//		Timer timer = new Timer();
//	}

	@Override
	protected void initialize() {
		// TODO Implement this
		
	}

	@Override
	public void run() {
		// TODO Implement this
		initialize();
		int tickNumber = 1;
		synchronized (this) {
			while (tickNumber >= duration) {
				getSimplePublisher().sendBroadcast(new TickBroadcast(tickNumber, tickNumber == duration));
				tickNumber++;
				try {
					this.wait(100);
				} catch (InterruptedException e) {
				}
			}
		}
	}

}

