package bgu.spl.mics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> messageMap= new ConcurrentHashMap<>();
	private ConcurrentHashMap<Subscriber, ConcurrentLinkedQueue<Class<? extends Message>>> myTopicsMap = new ConcurrentHashMap<>();
	private  ConcurrentHashMap<Class<? extends Event>, ConcurrentLinkedQueue<Subscriber>> eventMap = new ConcurrentHashMap<>();
	private  ConcurrentHashMap<Class<? extends Broadcast>,ConcurrentLinkedQueue<Subscriber>> broadcastMap= new ConcurrentHashMap<>();
	private ConcurrentHashMap<Event,Future> futureMap = new ConcurrentHashMap<>();



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
		eventMap.putIfAbsent(type, new ConcurrentLinkedQueue<>()); //check
		eventMap.get(type).add(m);
		myTopicsMap.get(m).add(type);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		broadcastMap.putIfAbsent(type, new ConcurrentLinkedQueue<>());
		myTopicsMap.get(m).add(type);
		broadcastMap.get(type).add(m);


	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		if(e!=null && futureMap.containsKey(e)){
				futureMap.get(e).resolve(result);
				futureMap.remove(e);

		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		broadcastMap.putIfAbsent(b.getClass(), new ConcurrentLinkedQueue<>());
		synchronized (broadcastMap.get(b.getClass())) { //lock this queue
			for (Subscriber s : broadcastMap.get(b.getClass())) {
				messageMap.get(s).add(b);
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		eventMap.putIfAbsent(e.getClass(), new ConcurrentLinkedQueue<>());
		synchronized(eventMap.get(e.getClass())) {//lock e's subscriber queue
			Subscriber s = eventMap.get(e.getClass()).poll(); //remove head

			if (s == null ) { //means no-one subscribed to solve such event
				return null;
			}

				if (!messageMap.containsKey(s)) { //means no-one subscribed to solve such event
					return null;
				} else {
					Future<T> f = new Future<>();
					futureMap.put(e, f);
					eventMap.get(e.getClass()).add(s);//add s to the end of queue
					messageMap.get(s).offer(e);
					return f;
				}
		}
	}

	@Override
	public void register(Subscriber m) {
		messageMap.putIfAbsent(m,new LinkedBlockingQueue<>());
		myTopicsMap.putIfAbsent(m,new ConcurrentLinkedQueue<>());
	}

	@Override
	public void unregister(Subscriber m) {

			for (Class topic : myTopicsMap.get(m)) {
				if (eventMap.containsKey(topic)) {
					synchronized (eventMap.get(topic)) {//lock e's subscriber queue
						if (Event.class.isAssignableFrom(topic)) {
							eventMap.get(topic).remove(m);
						} //the topic is event
						else {
							broadcastMap.get(topic).remove(m);
						} //the topic is a broadCast
					}
				}
			}

			myTopicsMap.remove(m); //delete m's topic queue


			for (Message msgToAvoid : messageMap.get(m)) {
				if (msgToAvoid instanceof Event) {
					this.complete(((Event)msgToAvoid),null);
				}
				//for every msg received after terminate() call, return null to all waiters (on those messages)
			}
			messageMap.remove(m); // delete m's message queue



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
