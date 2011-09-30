package org.opensplice.webcast.randomshapes;

import org.omg.dds.core.event.DataAvailableEvent;
import org.omg.dds.core.policy.History;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.domain.DomainParticipantFactory;
import org.omg.dds.sub.*;
import org.omg.dds.topic.Topic;
import org.opensplice.demo.ShapeType;

import java.util.LinkedList;

import static java.lang.System.*;

public class ShapesReader {
    public static void main(String args[]) {

        if (args.length < 1) {
            out.println("USAGE:\n\tShapesReader <topic-name>");
            exit(-1);
        }

        DomainParticipant dp =
                DomainParticipantFactory
                        .getInstance()
                        .createParticipant(0);

        Topic<ShapeType> shapeTopic =
                dp.createTopic(args[0], ShapeType.class);

        Subscriber sub =
                dp.createSubscriber();

        DataReaderQos drQos =
                sub.getDefaultDataReaderQos()
                        .with(History.KeepLast(10));

        DataReader<ShapeType> dr =
                sub.createDataReader(shapeTopic, drQos);

        dr.setListener(
                new SimpleDataReaderListener<ShapeType>() {
                    LinkedList<Sample<ShapeType>> samples =
                            new LinkedList<Sample<ShapeType>>();
                    @Override
                    public void onDataAvailable(DataAvailableEvent<ShapeType> e) {

                        e.getSource().read(samples);
                        out.println("---------------------------------------------");
                        out.println("\tREAD");
                        out.println("---------------------------------------------");
                        for (Sample<ShapeType> s: samples) {
                            ShapeType st = s.getData();
                            out.println("["+ st.color +", ("+ st.x +","+ st.y +")]");
                        }

                        e.getSource().history(samples);
                        out.println("---------------------------------------------");
                        out.println("\tHISTORY");
                        out.println("---------------------------------------------");
                        for (Sample<ShapeType> s: samples) {
                            ShapeType st = s.getData();
                            out.println("["+ st.color +", ("+ st.x +","+ st.y +")]");
                        }

                    }
                }
        );

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
