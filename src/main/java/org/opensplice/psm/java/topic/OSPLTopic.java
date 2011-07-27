package org.opensplice.psm.java.topic;

import java.util.Collection;

import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.policy.Durability;
import org.omg.dds.core.policy.Reliability;
import org.omg.dds.core.status.InconsistentTopic;
import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;
import org.omg.dds.topic.TopicQos;
import org.opensplice.dds.dcps.TypeSupportImpl;
import org.opensplice.psm.java.core.OSPLInstanceHandle;
import org.opensplice.psm.java.domain.OSPLDomainParticipant;

import DDS.ANY_STATUS;
import DDS.DURATION_INFINITE_NSEC;
import DDS.DURATION_INFINITE_SEC;
import DDS.DurabilityQosPolicyKind;
import DDS.Duration_t;
import DDS.RETCODE_OK;
import DDS.ReliabilityQosPolicyKind;
import DDS.TopicQosHolder;

public class OSPLTopic<TYPE> implements Topic<TYPE> {

    private static final DDS.DestinationOrderQosPolicyKind DESTINATION_ORDER_KIND =
            DDS.DestinationOrderQosPolicyKind.BY_SOURCE_TIMESTAMP_DESTINATIONORDER_QOS;
    private static final int SERVICE_CLEANUP_DELAY_SEC = 300;
    private static final int MAX_SAMPLES_PER_INSTANCE = 1;

    private final OSPLDomainParticipant participant;
    private final String topicName;
    private final Class<TYPE> type;
    private DDS.Topic topic = null;
    private TopicQos qos = null;

    public DDS.Topic getTopic() {
        return topic;
    }

    public OSPLTopic(OSPLDomainParticipant theparticipant, String thetopicName,
            Class<TYPE> thetype, TopicQos theqos) {
        participant = theparticipant;
        topicName = thetopicName;
        type = thetype;
        qos = theqos;
        registerType();
        createTopic();
    }

    private void registerType() {
        Class<?> typeSupportClass = null;
        String typeSupportName = type.getName() + "TypeSupport";
        int rc;
        try {
            typeSupportClass = Class.forName(typeSupportName);
            TypeSupportImpl typeSupport =
                    (TypeSupportImpl) typeSupportClass.newInstance();
            rc = typeSupport.register_type(
                    participant.getOpenspliceParticipant(),
                    topicName);
        } catch (java.lang.Exception e) {
            throw new RuntimeException("register_type failed " + e.getMessage());
        }
        if (rc != RETCODE_OK.value) {
            throw new RuntimeException("register_type failed");
        }
    }

    protected void createTopic() {
        DurabilityQosPolicyKind durabilityKind = DurabilityQosPolicyKind.VOLATILE_DURABILITY_QOS;
        ReliabilityQosPolicyKind reliabilityKind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;

        if (qos != null) {
            if (qos.getDurability().getKind()
                    .equals(Durability.Kind.PERSISTENT)) {
                durabilityKind = DurabilityQosPolicyKind.PERSISTENT_DURABILITY_QOS;
            } else if (qos.getDurability().getKind()
                    .equals(Durability.Kind.TRANSIENT)) {
                durabilityKind = DurabilityQosPolicyKind.TRANSIENT_DURABILITY_QOS;
            } else if (qos.getDurability().getKind()
                    .equals(Durability.Kind.TRANSIENT_LOCAL)) {
                durabilityKind = DurabilityQosPolicyKind.TRANSIENT_LOCAL_DURABILITY_QOS;
            }
            if (qos.getReliability().getKind()
                    .equals(Reliability.Kind.RELIABLE)) {
                reliabilityKind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
            }
            qos.getLatencyBudget().getDuration();

        }

        // Latency_Budget
        double latencyBudget = 0.04;
        if (reliabilityKind == ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS) {
            latencyBudget = 0.03;
        }

        // Lifespan
        double lifespan = Double.POSITIVE_INFINITY;

        // First find the Topic in the system...
        topic = participant.getOpenspliceParticipant().find_topic(topicName,
                new Duration_t(0, 50000000));
        if (null != topic) {
            return;
        }

        final TopicQosHolder holder = new TopicQosHolder();
        participant.getOpenspliceParticipant().get_default_topic_qos(holder);
        DDS.TopicQos topicQos = holder.value;
        setDuration(topicQos.latency_budget.duration, latencyBudget);
        setDuration(topicQos.lifespan.duration, lifespan);
        topicQos.durability.kind = durabilityKind;
        topicQos.durability_service.service_cleanup_delay.sec = SERVICE_CLEANUP_DELAY_SEC;
        topicQos.durability_service.max_samples_per_instance = MAX_SAMPLES_PER_INSTANCE;
        topicQos.reliability.kind = reliabilityKind;
        topicQos.destination_order.kind = DESTINATION_ORDER_KIND;
        // topicQos.resource_limits.max_samples_per_instance = 1;

        topic = participant.getOpenspliceParticipant().create_topic(topicName,
                topicName, topicQos, null, ANY_STATUS.value);

        // Still no Topic...
        if (null == topic) {
            throw new NullPointerException("not default Topic created for "
                    + topicName);
        }

    }

    private static void setDuration(Duration_t to, double from) {
        if (Double.isInfinite(from)) {
            to.sec = DURATION_INFINITE_SEC.value;
            to.nanosec = DURATION_INFINITE_NSEC.value;
        } else {
            to.sec = (int) from;
            to.nanosec = (int) ((from - to.sec) * 1000000000);
        }
    }

    @Override
    public Class<TYPE> getType() {
        return type;
    }

    @Override
    public <OTHER> TopicDescription<OTHER> cast() {
        return (TopicDescription<OTHER>) this;
    }

    @Override
    public String getTypeName() {
        return type.getName();
    }

    @Override
    public String getName() {
        return topicName;
    }

    @Override
    public DomainParticipant getParent() {
        return participant;
    }

    @Override
    public void close() {
    }

    @Override
    public void enable() {
        topic.enable();
    }

    @Override
    public StatusCondition<Topic<TYPE>> getStatusCondition() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InstanceHandle getInstanceHandle() {
        return new OSPLInstanceHandle(topic.get_instance_handle());
    }

    @Override
    public void retain() {
        // TODO Auto-generated method stub

    }

    @Override
    public Collection<Class<? extends Status<?>>> getStatusChanges(
            Collection<Class<? extends Status<?>>> statuses) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InconsistentTopic getInconsistentTopicStatus() {
        // TODO Auto-generated method stub
        return null;
    }
}
