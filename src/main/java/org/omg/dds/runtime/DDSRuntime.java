package org.omg.dds.runtime;


import org.omg.dds.domain.DomainParticipantFactory;

public abstract class DDSRuntime {

    public static DDSRuntime getInstance() {
        return Bootstrap.runtime();
    }

    // Default QoS Policy Provider
    public abstract TopicPolicyProvider getTopicPolicyProvider();

    public abstract DataReaderPolicyProvider getDataReaderPolicyProvider();
    public abstract SubscriberPolicyProvider getSubscriberPolicyProvider();

    public abstract DataWriterPolicyProvider getDataWriterPolicyProvider();
    public abstract PublisherPolicyProvider getPublisherPolicyProvider();

    public abstract DomainParticipantPolicyProvider getDomainParticipantPolicyProvider();

    // Default QoS Provider
    public abstract QosProvider getQosProvider();

    // DomainParticipant Factory
    public abstract DomainParticipantFactory getParticipantFactory();

}
