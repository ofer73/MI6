package bgu.spl.mics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private static MessageBrokerImpl instance = null;
	private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> messageMap;
	private ConcurrentHashMap<Subscriber, ConcurrentLinkedQueue<Message>> mytopisMap;
	private  ConcurrentHashMap<Class<? extends Event>, ConcurrentLinkedQueue<Subscriber>> eventMap;
	private  ConcurrentHashMap<Class<? extends Broadcast>,ConcurrentLinkedQueue<Subscriber>> broadcastMap;
	private ConcurrentHashMap<Event,Future> FutureMap;
	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		synchronized (instance){
			if (instance == null) {
				instance = new MessageBrokerImpl();
			}
			return instance;
		}
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
		// TODO Auto-generated method stub

	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void register(Subscriber m) {
		messageMap.putIfAbsent(m,new LinkedBlockingQueue<>());
		mytopisMap.putIfAbsent(m,new ConcurrentLinkedQueue<>());


	}

	@Override
	public void unregister(Subscriber m) {
		messageMap.remove(m); // delete m's message queue
		for(Iterator it=mytopisMap.get(m).iterator();it.hasNext();){ //for each one of m's topics, delete m from topics q
			Object i=it.next();
			if(i instanceof Broadcast)
				broadcastMap.get(i.getClass()).remove(m);

			else
				eventMap.get(i.getClass()).remove(m);

		}
		mytopisMap.remove(m); //delete m's topic queue
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
