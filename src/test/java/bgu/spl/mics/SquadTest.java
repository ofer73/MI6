package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {
    private Squad sq;
    @BeforeEach
    public void setUp(){
        sq = Squad.getInstance();
    }

    @Test
    public void testLoadAndNames(){

        Agent[] agentArr = cArr();
        List<String> ls = cList(agentArr);
        sq.load(agentArr);
        assertTrue(sq.getAgents(ls));
        //check getAgentsNames():
        assertEquals(ls,sq.getAgentsNames(ls));
    }

    @Test
    public void testRelease(){

        Agent[] agentArr = cArr();
        List<String> ls = cList(agentArr);
        sq.load(agentArr);
        List<String> tmp = new ArrayList<>();
        tmp.add(ls.get(0));
        sq.releaseAgents(tmp);
        assertFalse(sq.getAgents(ls));
    }


    @Test
    public void testSend(){

        Agent[] agentArr = cArr();
        List<String> ls = cList(agentArr);
        sq.load(agentArr);
        List<String> tmp = new ArrayList<>();
        sq.sendAgents(tmp, 300);
        assertFalse(sq.getAgents(ls));
    }

    private List<String> cList(Agent[] agentArr){
        List<String> ls = new ArrayList<>();
        for (Agent a : agentArr) {
            ls.add(a.getName());
        }
        return ls;
    }

    private Agent[] cArr(){
        Agent a0 = new Agent();
        Agent a1 = new Agent() ;
        Agent a2 = new Agent();
        Agent a3 = new Agent();
        a0.setName("000");
        a1.setName("001");
        a2.setName("002");
        a3.setName("003");
        a0.setSerialNumber("000");
        a1.setSerialNumber("001");
        a2.setSerialNumber("002");
        a3.setSerialNumber("003");
        Agent[] agentArr = {a0,a1,a2,a3};
        return agentArr;
    }


}
