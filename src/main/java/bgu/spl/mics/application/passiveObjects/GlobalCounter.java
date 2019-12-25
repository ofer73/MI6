package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.atomic.AtomicInteger;

public class GlobalCounter {
    private AtomicInteger counter = new AtomicInteger(0);
    private int max = 0;

    private static class CounterHolder {
        private static GlobalCounter instance = new GlobalCounter();
    }

    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static GlobalCounter getInstance() {
        return CounterHolder.instance;
    }

    public void increment() {
        counter.incrementAndGet();
        if (counter.get() == max)
            synchronized (this) {
                this.notifyAll();
            }
    }

    public int getCounter() {
        return counter.get();
    }

}