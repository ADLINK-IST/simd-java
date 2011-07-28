package org.opensplice.psm.java.topic;

import java.util.Collection;

import DDS.*;
import org.omg.dds.core.DDSException;
import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.policy.Durability;
import org.omg.dds.core.policy.Reliability;
import org.omg.dds.core.status.InconsistentTopic;
import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicQos;
import org.opensplice.dds.dcps.TypeSupportImpl;
import org.opensplice.psm.java.core.OSPLInstanceHandle;
import org.opensplice.psm.java.core.policy.OSPL;
import org.opensplice.psm.java.domain.OSPLDomainParticipant;

public class OSPLTopic<TYPE> implements Topic<TYPE> {

    private static final DDS.DestinationOrderQosPolicyKind DESTINATION_ORDER_KIND =
            DDS.DestinationOrderQosPolicyKind.BY_SOURCE_TIMESTAMP_DESTINATIONORDER_QOS;
    private static final int SERVICE_CLEANUP_DELAY_SEC = 300;
    private static final int MAX_SAMPLES_PER_INSTANCE = 1;

    private final OSPLDomainParticipant participant;
    private final String topicName;
    private final Class<TYPE> type;
    private DDS.Topic peer = null;
    private TopicQos qos = null;
    private TypeSupport typeSupport= null;


    public OSPLTopic(OSPLDomainParticipant participant,
                     String topicName,
                    Class<TYPE> type)
    {
        this.participant = participant;
        this.topicName = topicName;
        this.type = type;

        TopicQosHolder holder = new TopicQosHolder();
        participant.getPeer().get_default_topic_qos(holder);
        DDS.TopicQos tqos = holder.value;
        // Add converter
        this.qos = null;
        this.typeSupport = registerType();
        System.out.println("OSPLTopic("+this.topicName +", "+ this.typeSupport.get_type_name() + ")");
        this.peer =
                this.participant.getPeer().create_topic(
                        this.topicName,
                        this.typeSupport.get_type_name(),
                        tqos, null,
                        DDS.STATUS_MASK_ANY_V1_2.value);
        assert(this.peer != null);
    }

    public OSPLTopic(OSPLDomainParticipant participant,
                     String topicName,
                     Class<TYPE> type,
                     TopicQos qos) {
        this.participant = participant;
        this.topicName = topicName;
        this.type = type;

        TopicQosHolder holder = new TopicQosHolder();
        participant.getPeer().get_default_topic_qos(holder);
        DDS.TopicQos tqos = holder.value;
        // TODO: Ensure that the QoS is really taken into account!
        this.qos = qos;
        this.typeSupport = registerType();
        this.peer =
                this.participant.getPeer().create_topic(
                        this.topicName,
                        this.typeSupport.get_type_name(),
                        tqos, null,
                        DDS.STATUS_MASK_ANY_V1_2.value);
    }

    private TypeSupport registerType() {
        TypeSupport typeSupport;
        Class<?> typeSupportClass = null;
        String typeSupportName = this.type.getName() + "TypeSupport";
        int rc;
        try {
            typeSupportClass = Class.forName(typeSupportName);
            typeSupport =
                    (TypeSupport) typeSupportClass.newInstance();
            rc = typeSupport.register_type(
                    this.participant.getPeer(),
                    typeSupport.get_type_name());
        } catch (java.lang.Exception e) {
            throw new RuntimeException("register_type failed " + e.getMessage());
        }
        if (rc != RETCODE_OK.value) {
            throw new RuntimeException("register_type failed");
        }
        return typeSupport;

    }

    public DDS.Topic getPeer() {
        return peer;
    }

    public TypeSupport getTypeSupport() {
        return typeSupport;
    }

    //TODO: Frans, what is this method for?
    private static void setDuration(Duration_t to, double from) {
        if (Double.isInfinite(from)) {
            to.sec = DURATION_INFINITE_SEC.value;
            to.nanosec = DURATION_INFINITE_NSEC.value;
        } else {
            to.sec = (int) from;
            to.nanosec = (int) ((from - to.sec) * 1000000000);
        }
    }

    
    public Class<TYPE> getType() {
        return type;
    }

    
    public String getTypeName() {
        return type.getName();
    }

    
    public String getName() {
        return topicName;
    }

    
    public DomainParticipant getParent() {
        return participant;
    }

    
    public void close() {
    }

    
    public void enable() {
        peer.enable();
    }

    
    public StatusCondition<Topic<TYPE>> getStatusCondition() {
        // TODO Auto-generated method stub
        return null;
    }

    
    public InstanceHandle getInstanceHandle() {
        return new OSPLInstanceHandle(peer.get_instance_handle());
    }

    
    public void retain() {
        // TODO Auto-generated method stub

    }

    
    public Collection<Class<? extends Status<?>>> getStatusChanges(
            Collection<Class<? extends Status<?>>> statuses) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public InconsistentTopic getInconsistentTopicStatus() {
        // TODO Auto-generated method stub
        return null;
    }
}
