package org.opensplice.psm.java.core.policy;

import DDS.Publisher;
import DDS.Subscriber;
import org.omg.dds.core.policy.*;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.domain.DomainParticipantFactoryQos;
import org.omg.dds.domain.DomainParticipantQos;
import org.omg.dds.pub.DataWriterQos;
import org.omg.dds.pub.PublisherQos;
import org.omg.dds.runtime.QosProvider;
import org.omg.dds.sub.DataReaderQos;
import org.omg.dds.sub.SubscriberQos;
import org.omg.dds.topic.TopicQos;

public class OSPLQosProvider implements QosProvider {
    // private DomainParticipantQos domainParticipantQos = new DomainParticipantQos();
    private TopicQos        topicQos        = new TopicQos();
    private PublisherQos    publisherQos    = new PublisherQos();
    private DataWriterQos   dataWriterQos   = new DataWriterQos();
    private DataReaderQos   dataReaderQos   = new DataReaderQos();

    public DomainParticipantFactoryQos getDomainParticipantFactoryQos() {
        return null;
    }

    public DomainParticipantQos getDomainParticipantQos() {
        return null;
    }

    public TopicQos getTopicQos() {
        return this.topicQos;
    }

    public DataWriterQos getDataWriterQos() {
        return this.dataWriterQos;
    }

    public PublisherQos getPublisherQos() {
        return this.publisherQos;
    }

    public DataReaderQos getDataReaderQos() {
        return dataReaderQos;
    }

    public SubscriberQos getSubscriberQos() {
        return null;
    }
}
