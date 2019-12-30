package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.fail;

public class InventoryTest {
    private Inventory inv;
    @BeforeEach
    public void setUp(){
        inv = Inventory.getInstance();

    }


    @Test
    public void testGet(){
       String []toload = {"Goggles","GPS","Pistol"};
       inv.load(toload);
       assertTrue(inv.getItem("Pistol"));
        assertTrue(inv.getItem("GPS"));
        assertTrue(inv.getItem("Goggles"));
        assertFalse(inv.getItem("Pistol"));
        assertFalse(inv.getItem(""));
        assertFalse(inv.getItem("Flashlight"));
        assertFalse(inv.getItem(null));
    }
    @Test
    public void testLoad() {
        String []toload1 = {"Goggles","GPS","Pistol"};
        String []toload2 = {"Knife","Cloack","Belt"};
        inv.load(toload1);
        for(String e: toload1)
            assertTrue(inv.getItem(e));
        for(String e : toload2)
            assertFalse(inv.getItem(e));

    }
}




