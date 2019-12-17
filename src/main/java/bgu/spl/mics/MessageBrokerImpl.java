package bgu.spl.mics;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private static MessageBrokerImpl instance = null; // check if necessary
	private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> messageMap;
	private ConcurrentHashMap<Subscriber, ConcurrentLinkedQueue<Message>> mytopicsMap;
	private  ConcurrentHashMap<Class<? extends Event>, ConcurrentLinkedQueue<Subscriber>> eventMap;
	private  ConcurrentHashMap<Class<? extends Broadcast>,ConcurrentLinkedQueue<Subscriber>> broadcastMap;
	private ConcurrentHashMap<Event,Future> FutureMap;



	/**
	 * A wrapper class for the singleton (@MessageBroker),
	 * to handle a valid and thread-safe initialize for the singleton (@MessageBroker)
	 */
	private static class MsbHolder {
		private static MessageBroker instance = new MessageBrokerImpl();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return MsbHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		if(e!=null&&result!=null){
			FutureMap.get(e).resolve(result);
			FutureMap.get(e).notify();
			FutureMap.remove(e);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO check if it is a thread-safe function, and sync is not necessary here:
		Iterator iterator = broadcastMap.get(b.getClass()).iterator();
		while (iterator.hasNext()){
			Subscriber s = (Subscriber) iterator.next();
			messageMap.get(s).add(b);
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void register(Subscriber m) {
		messageMap.putIfAbsent(m,new LinkedBlockingQueue<>());
		mytopicsMap.putIfAbsent(m,new ConcurrentLinkedQueue<>());


	}

	@Override
	public void unregister(Subscriber m) {
		messageMap.remove(m); // delete m's message queue
		for(Iterator it = mytopicsMap.get(m).iterator(); it.hasNext();){ //for each one of m's topics, delete m from topics q
			Object i=it.next();
			if(i instanceof Broadcast)
				broadcastMap.get(i.getClass()).remove(m); //is it the right syntax?

			else
				eventMap.get(i.getClass()).remove(m);

		}
		mytopicsMap.remove(m); //delete m's topic queue
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		if(!messageMap.containsKey(m))
			throw new IllegalStateException();
		return messageMap.get(m).take();
		//return the head of m's personal queue
		//if doesn't exist, take() blocks m's wrapping thread
	}
}
