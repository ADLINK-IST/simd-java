package org.opensplice.demo.ping;

import org.omg.dds.core.Duration;
import org.omg.dds.core.event.InconsistentTopicEvent;
import org.omg.dds.core.policy.*;
import org.omg.dds.domain.*;
import org.omg.dds.topic.Topic;
import org.omg.dds.pub.*;
import org.omg.dds.topic.TopicQos;
import org.opensplice.demo.PingType;
import sun.rmi.log.ReliableLog;

public class PingPublisher {
    public static void main(String args[]) throws Exception {

        if (args.length < 4) {
            System.out.println("USAGE:\n\tPingPublisher <period ms> <iterations> <id> <name>");
            System.exit(1);
        }


        DomainParticipantFactory dpf =
                DomainParticipantFactory.getInstance();

        DomainParticipant dp = dpf.createParticipant(0);

        TopicQos tQos = new TopicQos(Reliability.Reliable());

        Topic<PingType> topic = dp.createTopic("PingTopic", PingType.class);

        Publisher pub = dp.createPublisher();

        DataWriterQos dwQos =
                new DataWriterQos(Reliability.Reliable(),
                                  Durability.Transient(),
                                  History.KeepLast(10));

        DataWriter<PingType> writer =
                pub.createDataWriter(topic, dwQos);

        PingType ping = new PingType();

        int period  = Integer.parseInt(args[0]);
        int N = Integer.parseInt(args[1]);
        int id = Integer.parseInt(args[2]);
        String name = args[3];
        for (int i = 0; i < N; ++i) {
            ping.number = id;
            ping.counter = i;
            ping.vendor = name;

            writer.write(ping);
            System.out.println("("+ping.counter +", "+ ping.number + ", " + ping.vendor +")");

            try {
                Thread.sleep(period);
            } catch (InterruptedException e) { }
        }
    }
}
