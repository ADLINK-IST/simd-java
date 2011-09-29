package org.opensplice.psm.java;

import org.omg.dds.domain.DomainParticipantFactory;
import org.omg.dds.runtime.*;
import org.omg.dds.runtime.DDSRuntime;
import org.opensplice.psm.java.core.policy.*;
import org.opensplice.psm.java.domain.OSPLDomainParticipantFactory;

public class OSPLRuntime extends DDSRuntime {

    private final OSPLDataWriterPolicyProvider dataWriterPolicyProvider =
            new OSPLDataWriterPolicyProvider();

    private final OSPLDataReaderPolicyProvider dataReaderPolicyProvider =
            new OSPLDataReaderPolicyProvider();

    private final OSPLPubSubPolicyProvider pubSubPolicyProvider =
            new OSPLPubSubPolicyProvider();

    private final OSPLDomainParticipantPolicyProvider domainParticipantPolicyProvider =
            new OSPLDomainParticipantPolicyProvider();

    private  OSPLQosProvider qosProvider = null;

    private static final OSPLDomainParticipantFactory participantFactory =
            new OSPLDomainParticipantFactory();


    ////////////////////////////////////////////////////////////////////////////////////////
    // Default QoS Policy Provider
    public TopicPolicyProvider getTopicPolicyProvider() {
        return this.dataWriterPolicyProvider;
    }

    public DataReaderPolicyProvider getDataReaderPolicyProvider() {
        return this.dataReaderPolicyProvider;
    }
    public SubscriberPolicyProvider getSubscriberPolicyProvider() {
        return this.pubSubPolicyProvider;
    }

    public DataWriterPolicyProvider getDataWriterPolicyProvider() {
        return this.dataWriterPolicyProvider;
    }
    public PublisherPolicyProvider getPublisherPolicyProvider() {
        return this.pubSubPolicyProvider;
    }

    public DomainParticipantPolicyProvider getDomainParticipantPolicyProvider() {
        return this.domainParticipantPolicyProvider;
    }

    // Default QoS Provider
    public synchronized QosProvider getQosProvider() {
        if (this.qosProvider == null) {
            this.qosProvider = new OSPLQosProvider();
        }
        return this.qosProvider;
    }

    @Override
    public DomainParticipantFactory getParticipantFactory() {
        return participantFactory;
    }
}
