package org.opensplice.demo.ping;


import org.omg.dds.core.event.*;
import org.omg.dds.core.policy.Durability;
import org.omg.dds.core.policy.History;
import org.omg.dds.core.policy.Reliability;
import org.omg.dds.domain.*;
import org.omg.dds.topic.Topic;
import org.omg.dds.sub.*;
import org.opensplice.demo.PingType;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.out;


public class PingSubscriber {
    public static void main(String args[]) {

        DomainParticipantFactory dpf =
                DomainParticipantFactory.getInstance();

        // Join the domain with ID = 0
        DomainParticipant dp = dpf.createParticipant(0);

        // Create the PingTopic
        Topic<PingType> topic =
                dp.createTopic("PingTopic", PingType.class);

        // Create a Subscriber on the default Partition
        Subscriber sub = dp.createSubscriber();

        // Get the default DataRederQos
        DataReaderQos tdrQos = sub.getDefaultDataReaderQos();

        // Override the defaults to reflect your needs...
        DataReaderQos drQos =
                tdrQos.with(
                        Reliability.Reliable(),
                        History.KeepLast(10),
                        Durability.Transient()
                );

        // Create a DataReader
        DataReader<PingType> dr = sub.createDataReader(topic, drQos);

        // Add a listener
        dr.setListener(
                new SimpleDataReaderListener<PingType>() {
                    @Override
                    public void onDataAvailable(DataAvailableEvent<PingType> e) {
                        DataReader<PingType> dr = e.getSource();
                        List<Sample<PingType>> data = new LinkedList<Sample<PingType>>();
                        dr.read(data);
                        System.out.println("------------------------------------------");
                        Iterator<Sample<PingType>> iterator = data.iterator();
                        while (iterator.hasNext()) {
                            PingType s = iterator.next().getData();
                            out.println("("+ s.counter+", "+ s.number+", "+ s.vendor +")");
                        }
                        System.out.println("------------------------------------------");
                    }
                }
        );

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
