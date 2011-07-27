package org.opensplice.psm.java.sub;

import org.omg.dds.core.policy.DataRepresentation;
import org.omg.dds.core.policy.Deadline;
import org.omg.dds.core.policy.DestinationOrder;
import org.omg.dds.core.policy.Durability;
import org.omg.dds.core.policy.History;
import org.omg.dds.core.policy.LatencyBudget;
import org.omg.dds.core.policy.Liveliness;
import org.omg.dds.core.policy.Ownership;
import org.omg.dds.core.policy.QosPolicy;
import org.omg.dds.core.policy.ReaderDataLifecycle;
import org.omg.dds.core.policy.Reliability;
import org.omg.dds.core.policy.ResourceLimits;
import org.omg.dds.core.policy.TimeBasedFilter;
import org.omg.dds.core.policy.TypeConsistencyEnforcement;
import org.omg.dds.core.policy.UserData;
import org.omg.dds.sub.DataReaderQos;
import org.opensplice.psm.java.core.policy.OSPL;

public class OSPLDataReaderQos implements DataReaderQos {

    private DDS.DataReaderQos qos;

    public OSPLDataReaderQos(DDS.DataReaderQos theqos) {
        qos = theqos;
    }

    public DDS.DataReaderQos getQos() {
        return qos;
    }

    public DataReaderQos with(QosPolicy... policies) {
        DDS.DataReaderQos newqos = new DDS.DataReaderQos();
        newqos.deadline = qos.deadline;
        newqos.destination_order = qos.destination_order;
        newqos.durability = qos.durability;
        newqos.history = qos.history;
        newqos.latency_budget = qos.latency_budget;
        newqos.liveliness = qos.liveliness;
        newqos.ownership = qos.ownership;
        newqos.reliability = qos.reliability;
        newqos.resource_limits = qos.resource_limits;
        newqos.user_data = qos.user_data;
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
            case Liveliness.ID:
                newqos.liveliness = OSPL.convert((Liveliness) policy);
                break;
            case Ownership.ID:
                newqos.ownership = OSPL.convert((Ownership) policy);
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
            case UserData.ID:
                // TODO use convert method which creates new instance instead of
                // changing old one
                newqos.user_data.value = ((UserData) policy).getValue();
                break;
            default:
                throw (new RuntimeException(policy.getPolicyName() +
                        " is not applicable for PublisherQos"));

            }
        }
        return new OSPLDataReaderQos(newqos);
    }

    @Override
    public Durability getDurability() {
        return OSPL.convert(qos.durability);
    }

    @Override
    public Deadline getDeadline() {
        return new Deadline(OSPL.convert(
                qos.deadline.period));
    }

    @Override
    public LatencyBudget getLatencyBudget() {
        return new LatencyBudget(OSPL.convert(
                qos.latency_budget.duration));
    }

    @Override
    public Liveliness getLiveliness() {
        return OSPL.convert(qos.liveliness);
    }

    @Override
    public DestinationOrder getDestinationOrder() {
        return OSPL.convert(qos.destination_order);
    }

    @Override
    public History getHistory() {
        return OSPL.convert(qos.history);
    }

    @Override
    public ResourceLimits getResourceLimits() {
        // return OSPL.convert(qos.resource_limits);
        return null;
    }

    @Override
    public UserData getUserData() {
        // return OSPL.convert(qos.user_data);
        return null;
    }

    @Override
    public Ownership getOwnership() {
        return OSPL.convert(qos.ownership);
    }

    @Override
    public DataRepresentation getRepresentation() {
        // TODO Auto-generated method stub
        // return OSPL.convert(qos.);
        return null;
    }

    @Override
    public TypeConsistencyEnforcement getTypeConsistency() {
        // TODO Check that this is the currently only supported version
        return TypeConsistencyEnforcement.ExactType();
    }

    public OSPLDataReaderQos clone() {
        return new OSPLDataReaderQos(qos);
    }

    @Override
    public TimeBasedFilter getTimeBasedFilter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ReaderDataLifecycle getReaderDataLifecycle() {
        // TODO Auto-generated method stub
        return null;
    }
}
