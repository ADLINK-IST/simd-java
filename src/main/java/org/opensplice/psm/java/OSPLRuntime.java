package org.opensplice.psm.java;

import org.omg.dds.runtime.*;
import org.omg.dds.runtime.DDSRuntime;

public class OSPLRuntime extends DDSRuntime {
    // Default QoS Policy Provider
    public TopicPolicyProvider getTopicPolicyProvider() {
        return null;
    }

    public DataReaderPolicyProvider getDataReaderPolicyProvider() {
        return null;
    }
    public SubscriberPolicyProvider getSubscriberPolicyProvider() {
        return null;
    }

    public DataWriterPolicyProvider getDataWriterPolicyProvider() {
        return null;
    }
    public PublisherPolicyProvider getPublisherPolicyProvider() {
        return null;
    }

    public DomainParticipantPolicyProvider getDomainParticipantPolicyProvider() {
        return null;
    }

    // Default QoS Provider
    public QosProvider getQosProvider() {
        return null;
    }
}
