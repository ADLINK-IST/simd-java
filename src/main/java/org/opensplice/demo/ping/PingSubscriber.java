package org.opensplice.demo.ping;


import org.omg.dds.domain.*;
import org.omg.dds.topic.Topic;
import org.omg.dds.sub.*;
import org.opensplice.demo.PingType;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static java.lang.System.out;


public class PingSubscriber {
    public static void main(String args[]) {
        if (args.length < 1) {
            out.println("USAGE:\n\tPingSubscriber <period>");
            System.exit(1);
        }


        DomainParticipantFactory dpf =
                DomainParticipantFactory.getInstance();

        DomainParticipant dp = dpf.createParticipant(0);

        Topic<PingType> topic =
                dp.createTopic("PingTopic", PingType.class);

        Subscriber sub = dp.createSubscriber();

        DataReader<PingType> dr = sub.createDataReader(topic);

        long period = Long.parseLong(args[0]);

        List<Sample<PingType>> data = new LinkedList<Sample<PingType>>();

        while (true) {
            dr.read(data);
            Iterator<Sample<PingType>> iterator = data.iterator();
            while (iterator.hasNext()) {
                PingType s = iterator.next().getData();
                out.println("("+ s.number +", "+ s.counter +", "+ s.vendor +")");
            }
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) { }
        }
    }
}
