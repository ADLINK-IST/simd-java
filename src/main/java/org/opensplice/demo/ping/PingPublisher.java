package org.opensplice.demo.ping;

import org.omg.dds.core.policy.History;
import org.omg.dds.core.policy.Reliability;
import org.omg.dds.domain.*;
import org.omg.dds.topic.Topic;
import org.omg.dds.pub.*;
import org.omg.dds.topic.TopicQos;
import org.opensplice.demo.PingType;

public class PingPublisher {
    public static void main(String args[]) throws Exception {

        if (args.length < 2) {
            System.out.println("USAGE:\n\tPingPublisher <period ms> <iterations>");
            System.exit(1);
        }

        DomainParticipantFactory dpf =
                DomainParticipantFactory.getInstance();

        DomainParticipant dp = dpf.createParticipant(0);
        Topic<PingType> topic = dp.createTopic("PingTopic", PingType.class);

        Publisher pub = dp.createPublisher();

        DataWriter<PingType> writer = pub.createDataWriter(topic);
        PingType ping = new PingType();

        int period  = Integer.parseInt(args[0]);
        int N = Integer.parseInt(args[1]);

        for (int i = 0; i < N; ++i) {
            ping.counter = i;
            ping.number = i;
            ping.vendor = "JoJo";

            writer.write(ping);
            System.out.println("("+ping.counter +", "+ ping.number + ", " + ping.vendor +")");

            try {
                Thread.sleep(period);
            } catch (InterruptedException e) { }
        }
    }
}
