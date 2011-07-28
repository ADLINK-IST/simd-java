package org.omg.dds.test.core;

import org.junit.Test;
import org.omg.dds.core.Duration;
import org.omg.dds.core.Time;
import org.omg.dds.core.OverflowException;

import java.util.concurrent.TimeUnit;


public class TimeTest {
    @Test
    public void testCreation() throws OverflowException {
        Time t1 = new Time(10, 10);
        Time t2 = new Time(30000, 10);
        Time t3 = new Time(5, 30);
        Time t4 = new Time(60000, 50);
        Time t5 = new Time(2, 2);
        Duration d1 = new Duration(10, 10);
        Duration d2 = new Duration(20, 5);
        Duration d3 = new Duration(5, 30);
        Duration d4 = new Duration(60000, 50);
        Duration d5 = new Duration(2, 2);
        //     Time d6 = new Time(Long.MAX_VALUE,0);

        System.out.println();
        System.out.println("compareto");
        System.out.println(t1.compareTo(t2));
        System.out.println(t1.compareTo(t3));
        System.out.println(t1.compareTo(t4 ));
        System.out.println(t1.compareTo(t5));

        System.out.println();
        System.out.println("add");
        print((Time) t1.add(d2));
        print((Time) t1.add(d3));
        print((Time) t2.add(d4));
        print((Time) t1.add(d5));


        System.out.println();
        System.out.println("subtract");

        print((Time) t1.subtract(d2));
        print((Time) t1.subtract(d3));
        print((Time) t1.subtract(d4));
        print((Time) t1.subtract(d5));



        System.out.println();
        System.out.println("conversion");
        System.out.println(d2.getDuration(TimeUnit.SECONDS));
        System.out.println(d2.getDuration(TimeUnit.NANOSECONDS));


        System.out.println();
        System.out.println("remainder");
        System.out.println(d2.getRemainder(TimeUnit.SECONDS, TimeUnit.SECONDS));
        System.out.println(d2.getRemainder(TimeUnit.SECONDS, TimeUnit.NANOSECONDS));
        System.out.println   (d2.getRemainder(TimeUnit.NANOSECONDS, TimeUnit.SECONDS));
        System.out.println(d2.getRemainder(TimeUnit.NANOSECONDS, TimeUnit.NANOSECONDS));



    }


    private void print(Time add) {
        System.out.println(add.getSec() + " , " + add.getNanoSec());
    }


    public void print(Duration d) {

        System.out.println(d.getSec() + " , " + d.getNanoSec());

    }


}
