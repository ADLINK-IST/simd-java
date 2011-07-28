/*
package org.omg.dds.test.core;

import org.junit.Test;
import org.omg.dds.core.Duration;
import org.omg.dds.core.status.OverflowException;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class DurationTest {
    @Test
    public void testCreation() throws OverflowException {
        Duration d1 = new Duration(10, 10);
        Duration d2 = new Duration(20, 5);
        Duration d3 = new Duration(5, 30);
        Duration d4 = new Duration(50, 50);
        Duration d5 = new Duration(2, 2);
        Duration d6 = new Duration(Long.MAX_VALUE,0);

        System.out.println();
        System.out.println("compareto");
        System.out.println(d1.compareTo(d2));
        System.out.println(d1.compareTo(d3));
        System.out.println(d1.compareTo(d4));
        System.out.println(d1.compareTo(d5));

        System.out.println();
        System.out.println("add");
        print(d1.add(d2));
        print(d1.add(d3));
        print(d1.add(d4));
        print(d1.add(d5));
        //print(d6.add(d6));


        System.out.println();
        System.out.println("subtract");

        //print(d1.subtract(d2));
        print(d1.subtract(d3));
        //print(d1.subtract(d4));
        print(d1.subtract(d5));
        print(d6.subtract(d6));

        System.out.println();
        System.out.println("multiply");

        print(d1.multiply(200));
        print(d2.multiply(222));
        //print(d3.multiply(Long.MAX_VALUE));
        //print(d4.multiply(-111));


        System.out.println();
        System.out.println("divide");

        System.out.println(d1.divide(d2));
        System.out.println(d1.divide(d3));
        System.out.println(d1.divide(d4));
        System.out.println(d1.divide(d5));

        System.out.println();
        System.out.println("divide by cst");

        print(d1.divide(2).multiply(2));
        print(d2.divide(2).multiply(2));
        print(d6.divide(200));
        print(d1.divide(10));
        //print(d1.divide(-5));
        print(d1.divide(Long.MAX_VALUE));

        System.out.println();
        System.out.println("conversion");
        System.out.println(d2.getDuration(TimeUnit.SECONDS));
        System.out.println(d2.getDuration(TimeUnit.NANOSECONDS));


        System.out.println();
        System.out.println("remainder");
        System.out.println(d2.getRemainder(TimeUnit.SECONDS, TimeUnit.SECONDS));
        System.out.println(d2.getRemainder(TimeUnit.SECONDS, TimeUnit.NANOSECONDS));
        System.out.println(d2.getRemainder(TimeUnit.NANOSECONDS, TimeUnit.SECONDS));
        System.out.println(d2.getRemainder(TimeUnit.NANOSECONDS, TimeUnit.NANOSECONDS));



    }


    public void print(Duration d) {

        System.out.println(d.getSec() + " , " + d.getNanoSec());

    }

}
*/
