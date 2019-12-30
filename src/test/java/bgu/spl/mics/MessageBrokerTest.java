package bgu.spl.mics;

import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.subscribers.Moneypenny;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import bgu.spl.mics.application.subscribers.M;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.fail;

public class MessageBrokerTest {
    private MessageBroker broker;
    @BeforeEach
    public void setUp(){
        broker=MessageBrokerImpl.getInstance();
    }

    @Test
    public void Test_subscribeEvent(){
        Subscriber s=new Moneypenny(0);
        broker.register(s);
        s.initialize();
        StringEvent e1 = new StringEvent();
        IntEvent e2=new IntEvent();
        e1.st="Money";
        e2.num=6;
        s.subscribeEvent(StringEvent.class,(StringEvent se) -> {
            System.out.println("Hello World");
        });
        Future<String> f1=broker.sendEvent(e1);
        Future<String> f2= broker.sendEvent(e2);
        assertTrue(f1!=null && f2==null);
    }
    @Test
    public void Test_complete(){
       StringEvent e =new StringEvent();
       Subscriber s=new Moneypenny(0);
        broker.register(s);
        s.initialize();
        s.subscribeEvent(StringEvent.class,(StringEvent se) -> {
            System.out.println("Hello World");
        });
       Future<String> f=broker.sendEvent(e);
       broker.complete(e,"Spinach");
       assertTrue(f.isDone());


    }
    @Test
    public void test_SendEvent(){
        StringEvent e =new StringEvent();
        Future<String> f=broker.sendEvent(e);
        broker.complete(e,"Spinach");
        assertEquals(f.get(),"Spinach");
    }


}
