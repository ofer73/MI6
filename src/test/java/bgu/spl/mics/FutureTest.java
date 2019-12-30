package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.TimeUnit;


public class FutureTest {
    Future<Boolean> future;
    @BeforeEach
    public void setUp(){
        future=new Future<>();
    }

    @Test
    public void test_resolve(){
        future.resolve(true);
       assertTrue(future.isDone());
    }
    @Test

    public void test_Done(){
        assertFalse(future.isDone());
        future.resolve(false);
        assertTrue(future.isDone());
    }
    @Test
    public void test_get(){
        future.resolve(true);
        assertTrue(future.get());
        future.resolve(false);
        assertFalse(future.get());
    }
    @Test
    public void test_get_time(){
        Thread TT = new Thread(() -> future.resolve(false));
        TT.start();
        long startTime = System.nanoTime();
        Boolean ans = future.get(200000000, TimeUnit.NANOSECONDS);
        long endTime = System.nanoTime();
        if(ans != null){
            assertTrue(future.isDone());
        } else {
           assertTrue(startTime+200000000>endTime);
        }
    }

}
