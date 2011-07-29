package org.omg.dds.test.core;

import junit.framework.Assert;
import org.junit.Test;
import org.omg.dds.core.Duration;
import org.omg.dds.core.Time;
import org.omg.dds.core.OverflowException;

import java.util.concurrent.TimeUnit;


public class TimeTest {
    @Test
    public void testCreation() throws OverflowException {
        Time t1 = new Time(10, 10);
        Time t2 = new Time(20, 20);
    }


    @Test
    public void testComparison() {
        Time t1 = new Time(10, 10);
        Time t2 = new Time(20, 20);
        // Comparison
        Assert.assertEquals(-1, t1.compareTo(t2));
        Assert.assertEquals(1, t2.compareTo(t1));
        Assert.assertEquals(0, t2.compareTo(t2));
    }

    @Test
    public void testAddition() {
        Time t1 = new Time(10, 10);
        Time t2 = new Time(20, 20);
        Duration d = new Duration(10, 10);

        // Addition
        Time s = t1.add(d);
        System.out.println("sum = "+ s);
        System.out.println("t2 = "+ t2);

        Assert.assertEquals(0, t1.add(d).compareTo(t2));
    }

    @Test
    public void testSubstraction() {
        Time t1 = new Time(10, 10);
        Time t2 = new Time(20, 20);
        Duration d = new Duration(10, 10);
        // Substraction
        Time s = t2.subtract(d);
        System.out.println(s);
        Assert.assertEquals(0, s.compareTo(t1));
        Assert.assertEquals(0, t1.subtract(d).compareTo(Time.zero()));

    }

}
