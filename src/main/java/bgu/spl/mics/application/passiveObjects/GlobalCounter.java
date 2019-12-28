package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.atomic.AtomicInteger;

public class GlobalCounter {
    private AtomicInteger counter = new AtomicInteger(0);
    private int max = 0;


    /**
     * Singleton implantation:
     */

    private static class CounterHolder {
        private static GlobalCounter instance = new GlobalCounter();
    }

    /**
     * a setter
     * @param max: sets number of total subscribers to initialize
     */

    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static GlobalCounter getInstance() {
        return CounterHolder.instance;
    }

    /**
     * increment the number in counter.
     * if we reached the max. we will notify main thread.
     */

    public void increment() {
        counter.incrementAndGet();
        if (counter.get() == max)
            synchronized (this) {
                this.notifyAll();
            }
    }

    /**
     * a getter
     * @return counter
     */

    public int getCounter() {
        return counter.get();
    }

}