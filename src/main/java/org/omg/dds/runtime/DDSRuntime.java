package org.omg.dds.runtime;


public abstract class DDSRuntime {

    public static DDSRuntime getInstance() {
        return DDSBinder.bind();
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

}
