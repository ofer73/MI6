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
	private static MessageBrokerImpl instance = null; //TODO check if necessary & valid
	private ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> messageMap;
	private ConcurrentHashMap<Subscriber, ConcurrentLinkedQueue<Class<? extends Message>>> myTopicsMap;
	private  ConcurrentHashMap<Class<? extends Event>, ConcurrentLinkedQueue<Subscriber>> eventMap;
	private  ConcurrentHashMap<Class<? extends Broadcast>,ConcurrentLinkedQueue<Subscriber>> broadcastMap;
	private ConcurrentHashMap<Event,Future> futureMap;



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
		myTopicsMap.get(m).add(type);
		eventMap.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		// TODO Auto-generated method stub
		myTopicsMap.get(m).add(type);
		broadcastMap.get(type).add(m);


	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		if(e!=null&&result!=null){
			futureMap.get(e).resolve(result);
			futureMap.get(e).notifyAll(); //Alon: why not notifyAll ?
			futureMap.remove(e);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO check if it is a thread-safe function, and sync is not necessary here:
		synchronized (broadcastMap.get(b.getClass())) { //lock this queue
			for (Subscriber s : broadcastMap.get(b.getClass())) {
				messageMap.get(s).add(b);
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO check if thread safe or FIX
		Subscriber s = eventMap.get(e.getClass()).poll(); //remove head
		if ( s== null) { //means no-one subscribed to solve such event
			return null;
		}
		else{
			messageMap.get(s).offer(e);
			Future f=new Future<>();
			futureMap.put(e,f);
			eventMap.get(e.getClass()).add(s);//add s to the end of queue
			return f;
		}
	}

	@Override
	public void register(Subscriber m) {
		messageMap.putIfAbsent(m,new LinkedBlockingQueue<>());
		myTopicsMap.putIfAbsent(m,new ConcurrentLinkedQueue<>());


	}

	@Override
	public void unregister(Subscriber m) {
		//TODO check if the change of the signuture from message to a class type screw the function, or if this function is very BAD
		//update: it doesn't work now, need to be fixed

		messageMap.remove(m); // delete m's message queue
		for(Iterator it = myTopicsMap.get(m).iterator(); it.hasNext();){ //for each one of m's topics, delete m from topics q
			Object i=it.next();
			if(i instanceof Broadcast)
				broadcastMap.get(i.getClass()).remove(m); //is it not the right syntax?

			else
				eventMap.get(i.getClass()).remove(m);

		}
		myTopicsMap.remove(m); //delete m's topic queue
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
