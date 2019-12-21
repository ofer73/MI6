package bgu.spl.mics;

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
        StringEvent e1 = new StringEvent();
        IntEvent e2=new IntEvent();
        e1.st="Money";
        e2.num=6;
        broker.subscribeEvent(e1.getClass(),s);
        broker.sendEvent(e1);
        broker.sendEvent(e2);
        try {
            assertEquals(StringEvent.class, broker.awaitMessage(s).getClass());
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void Test_complete(){
       StringEvent e =new StringEvent();
       Future<String> f=broker.sendEvent(e);
       broker.complete(e,"Spinach");
       assertTrue(f.isDone());


    }
    public void test_SendEvent(){
        StringEvent e =new StringEvent();
        Future<String> f=broker.sendEvent(e);
        broker.complete(e,"Spinach");
        assertEquals(f.get(),"Spinach");
    }


}
