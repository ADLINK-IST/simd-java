package org.opensplice.psm.java.pub;

import org.omg.dds.core.Duration;
import org.omg.dds.core.policy.DataRepresentation;
import org.omg.dds.core.policy.Deadline;
import org.omg.dds.core.policy.DestinationOrder;
import org.omg.dds.core.policy.Durability;
import org.omg.dds.core.policy.DurabilityService;
import org.omg.dds.core.policy.History;
import org.omg.dds.core.policy.LatencyBudget;
import org.omg.dds.core.policy.Lifespan;
import org.omg.dds.core.policy.Liveliness;
import org.omg.dds.core.policy.Ownership;
import org.omg.dds.core.policy.OwnershipStrength;
import org.omg.dds.core.policy.QosPolicy;
import org.omg.dds.core.policy.Reliability;
import org.omg.dds.core.policy.ResourceLimits;
import org.omg.dds.core.policy.TransportPriority;
import org.omg.dds.core.policy.TypeConsistencyEnforcement;
import org.omg.dds.core.policy.UserData;
import org.omg.dds.core.policy.WriterDataLifecycle;
import org.omg.dds.pub.DataWriterQos;
import org.opensplice.psm.java.core.policy.OSPL;
import org.opensplice.psm.java.core.policy.OSPLResourceLimits;

public class OSPLDataWriterQos implements DataWriterQos {

    private DDS.DataWriterQos qos;

    public OSPLDataWriterQos(DDS.DataWriterQos theqos) {
        qos = theqos;
    }

    public DDS.DataWriterQos getQos() {
        return qos;
    }

    public DataWriterQos with(QosPolicy... policies) {
        DDS.DataWriterQos newqos = new DDS.DataWriterQos();
        newqos.deadline = qos.deadline;
        newqos.destination_order = qos.destination_order;
        newqos.durability = qos.durability;
        newqos.history = qos.history;
        newqos.latency_budget = qos.latency_budget;
        newqos.lifespan = qos.lifespan;
        newqos.liveliness = qos.liveliness;
        newqos.ownership = qos.ownership;
        newqos.ownership_strength = qos.ownership_strength;
        newqos.reliability = qos.reliability;
        newqos.resource_limits = qos.resource_limits;
        newqos.transport_priority = qos.transport_priority;
        newqos.user_data = qos.user_data;
        newqos.writer_data_lifecycle = qos.writer_data_lifecycle;
        for (QosPolicy policy : policies) {
            switch (policy.getPolicyId()) {
            case Durability.ID:
                newqos.durability = OSPL.convert((Durability) policy);
                break;
            case Reliability.ID:
                newqos.reliability = OSPL.convert((Reliability) policy);
                break;
            case History.ID:
                newqos.history = OSPL.convert((History) policy);
                break;
            case Deadline.ID:
                newqos.deadline.period = OSPL.convert(((Deadline) policy)
                        .getPeriod());
                break;
            case DestinationOrder.ID:
                newqos.destination_order = OSPL
                        .convert((DestinationOrder) policy);
                break;
            case LatencyBudget.ID:
                newqos.latency_budget.duration = OSPL
                        .convert(((LatencyBudget) policy).getDuration());
                break;
            case Lifespan.ID:
                newqos.lifespan.duration = OSPL
                        .convert(((Lifespan) policy).getDuration());
                break;
            case Liveliness.ID:
                newqos.liveliness = OSPL.convert((Liveliness) policy);
                break;
            case Ownership.ID:
                newqos.ownership = OSPL.convert((Ownership) policy);
                break;
            case OwnershipStrength.ID:
            	// TODO use convert method which creates new instance instead of changing old one
                newqos.ownership_strength.value = ((OwnershipStrength) policy)
                        .getValue();
                break;
            case ResourceLimits.ID:
                /*
                 * ResourceLimits resourceLimits = (ResourceLimits) policy;
                 * newqos.resource_limits.max_instances = resourceLimits
                 * .getMaxInstances(); newqos.resource_limits.max_samples =
                 * resourceLimits .getMaxSamples();
                 * newqos.resource_limits.max_samples_per_instance =
                 * resourceLimits .getMaxSamplesPerInstance(); break;
                 */
                break;
            case TransportPriority.ID:
            	// TODO use convert method which creates new instance instead of changing old one
                newqos.transport_priority.value = ((TransportPriority) policy)
                        .getValue();
                break;
            case UserData.ID:
            	// TODO use convert method which creates new instance instead of changing old one
                newqos.user_data.value = ((UserData) policy).getValue();
                break;
            case WriterDataLifecycle.ID:
            	// TODO use convert method which creates new instance instead of changing old one
                newqos.writer_data_lifecycle.autodispose_unregistered_instances =
                        ((WriterDataLifecycle) policy)
                                .isAutDisposeUnregisteredInstances();
                break;
           	default:
           		throw (new RuntimeException(policy.getPolicyName() + 
           				" is not applicable for PublisherQos"));

            }
        }
        return new OSPLDataWriterQos(newqos);
    }


    public DurabilityService getDurabilityService() {
        return new DurabilityService(OSPL.convert(qos.history),
                Duration.infinite(),
                qos.resource_limits.max_samples,
                qos.resource_limits.max_instances,
                qos.resource_limits.max_samples_per_instance);
    }


    public Deadline getDeadline() {
        return new Deadline(OSPL.convert(qos.deadline.period));
    }


    public LatencyBudget getLatencyBudget() {
        return new LatencyBudget(OSPL.convert(
                qos.latency_budget.duration));
    }


    public Liveliness getLiveliness() {
        return OSPL.convert(qos.liveliness);
    }


    public Reliability getReliability() {
        return OSPL.convert(qos.reliability);
    }


    public DestinationOrder getDestinationOrder() {
        return OSPL.convert(qos.destination_order);
    }


    public History getHistory() {
        return OSPL.convert(qos.history);
    }


    public ResourceLimits getResourceLimits() {
        return new OSPLResourceLimits(qos.resource_limits);
    }


    public TransportPriority getTransportPriority() {
        return new TransportPriority(qos.transport_priority.value);
    }


    public Lifespan getLifespan() {
        return new Lifespan(OSPL.convert(qos.lifespan.duration));
    }


    public UserData getUserData() {
        return new UserData(qos.user_data.value);
    }


    public Ownership getOwnership() {
        return OSPL.convert(qos.ownership);
    }


    public OwnershipStrength getOwnershipStrength() {
        return new OwnershipStrength(qos.ownership_strength.value);
    }


    public Durability getDurability() {
        return OSPL.convert(qos.durability);
    }


    public WriterDataLifecycle getWriterDataLifecycle() {
        return OSPL.convert(qos.writer_data_lifecycle);
    }


    public DataRepresentation getRepresentation() {
        return new DataRepresentation(null);
    }


    public TypeConsistencyEnforcement getTypeConsistency() {
        // TODO Check that this is the currently only supported version
        return TypeConsistencyEnforcement.ExactType();
    }
}
