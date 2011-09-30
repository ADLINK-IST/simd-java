package org.opensplice.webcast.randomshapes;

import org.omg.dds.core.policy.Reliability;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.domain.DomainParticipantFactory;
import org.omg.dds.pub.DataWriter;
import org.omg.dds.pub.DataWriterQos;
import org.omg.dds.pub.Publisher;
import org.omg.dds.topic.Topic;
import org.opensplice.demo.ShapeType;

import java.util.concurrent.TimeoutException;

import static java.lang.System.*;
public class ShapesWriter {
    public static void main(String args[]) {
        if (args.length < 5) {
            out.println("Usage:\n\tShapesWriter <topic-name> <color> <size> <samples> <period(ms)>");
            exit(-1);
        }

        DomainParticipant dp =
                DomainParticipantFactory
                        .getInstance()
                        .createParticipant(0);

        Topic<ShapeType> shapeTopic = dp.createTopic(args[0], ShapeType.class);
        Publisher pub = dp.createPublisher();

        DataWriterQos dwQos =
                pub.getDefaultDataWriterQos()
                        .with(Reliability.Reliable());
        DataWriter<ShapeType> dw = pub.createDataWriter(shapeTopic, dwQos);

        final String color = args[1];
        final int size = Integer.parseInt(args[2]);
        final int samples = Integer.parseInt(args[3]);
        final int P = Integer.parseInt(args[4]);
        final int W = 320;
        final int H = 360;


        ShapeType st = new ShapeType(color, 0, 0, size);
        for (int i = 0; i < samples; ++i) {
            st.x = (int)(Math.random()*W);
            st.y = (int)(Math.random()*H);

            try {
                dw.write(st);
                Thread.sleep(P);
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }
}
