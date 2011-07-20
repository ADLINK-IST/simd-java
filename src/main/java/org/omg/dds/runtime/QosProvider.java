package org.omg.dds.runtime;


import org.omg.dds.domain.DomainParticipantFactoryQos;
import org.omg.dds.domain.DomainParticipantQos;
import org.omg.dds.pub.DataWriterQos;
import org.omg.dds.pub.PublisherQos;
import org.omg.dds.sub.DataReaderQos;
import org.omg.dds.sub.SubscriberQos;
import org.omg.dds.topic.TopicQos;

public interface QosProvider {

    public DomainParticipantFactoryQos getDomainParticipantFactoryQos();
    public DomainParticipantQos getDomainParticipantQos();

    public TopicQos getTopicQos();

    public DataWriterQos getDataWriterQos();
    public PublisherQos getPublisherQos();

    public DataReaderQos getDataReaderQos();
    public SubscriberQos getSubscriberQos();
}
