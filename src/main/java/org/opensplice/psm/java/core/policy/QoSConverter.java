package org.opensplice.psm.java.core.policy;


import org.omg.dds.pub.DataWriterQos;
import org.omg.dds.sub.DataReaderQos;
import org.opensplice.psm.java.OSPLRuntime;
import org.opensplice.psm.java.core.TypeConverter;
import org.opensplice.psm.java.core.policy.PolicyConverter;
public class QoSConverter {
    public static DDS.DataReaderQos convert(DataReaderQos qos, DDS.DataReaderQos toQos) {
        toQos.durability = PolicyConverter.convert(qos.getDurability());
        toQos.deadline = PolicyConverter.convert(qos.getDeadline());
        toQos.latency_budget = PolicyConverter.convert(qos.getLatencyBudget());
        toQos.liveliness = PolicyConverter.convert(qos.getLiveliness());
        toQos.reliability = PolicyConverter.convert(qos.getReliability());
        toQos.destination_order = PolicyConverter.convert(qos.getDestinationOrder());
        toQos.history = PolicyConverter.convert(qos.getHistory());
        toQos.resource_limits  = PolicyConverter.convert(qos.getResourceLimits());
        toQos.user_data = PolicyConverter.convert(qos.getUserData());
        toQos.ownership = PolicyConverter.convert(qos.getOwnership());
        // TODO: Add Converters
        // toQos.reader_data_lifecycle = PolicyConverter.convert(qos.getReaderDataLifecycle());
        // toQos.time_based_filter = PolicyConverter.convert(qos.getTimeBasedFilter());

        return toQos;
    }

    public static DDS.DataWriterQos convert(DataWriterQos qos, DDS.DataWriterQos toQos) {
        toQos.durability = PolicyConverter.convert(qos.getDurability());
        toQos.deadline = PolicyConverter.convert(qos.getDeadline());
        toQos.latency_budget = PolicyConverter.convert(qos.getLatencyBudget());
        toQos.liveliness = PolicyConverter.convert(qos.getLiveliness());
        toQos.reliability = PolicyConverter.convert(qos.getReliability());
        toQos.destination_order = PolicyConverter.convert(qos.getDestinationOrder());
        toQos.history = PolicyConverter.convert(qos.getHistory());
        toQos.resource_limits  = PolicyConverter.convert(qos.getResourceLimits());
        toQos.transport_priority = PolicyConverter.convert(qos.getTransportPriority());
        toQos.lifespan = PolicyConverter.convert(qos.getLifespan());
        toQos.user_data = PolicyConverter.convert(qos.getUserData());
        toQos.ownership = PolicyConverter.convert(qos.getOwnership());
        toQos.ownership_strength = PolicyConverter.convert(qos.getOwnershipStrength());
        toQos.writer_data_lifecycle = PolicyConverter.convert(qos.getWriterDataLifecycle());

        return toQos;
    }

    public static DataWriterQos convert(DDS.DataWriterQos qos) {

        return new DataWriterQos(
                        PolicyConverter.convert(qos.durability),
                        PolicyConverter.convert(qos.latency_budget),
                        PolicyConverter.convert(qos.liveliness),
                        PolicyConverter.convert(qos.reliability),
                        PolicyConverter.convert(qos.destination_order),
                        PolicyConverter.convert(qos.history),
                        PolicyConverter.convert(qos.resource_limits),
                        PolicyConverter.convert(qos.transport_priority),
                        PolicyConverter.convert(qos.lifespan),
                        PolicyConverter.convert(qos.ownership),
                        PolicyConverter.convert(qos.ownership_strength), //TODO: FixMe
                        // OSPLRuntime.getInstance().getDataWriterPolicyProvider().getRepresentation(),
                        // OSPLRuntime.getInstance().getDataWriterPolicyProvider().getTypeConsistency(),
                        PolicyConverter.convert(qos.user_data),
                        PolicyConverter.convert(qos.writer_data_lifecycle)
                );

    }
}
