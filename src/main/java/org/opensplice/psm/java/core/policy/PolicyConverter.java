package org.opensplice.psm.java.core.policy;

import java.util.concurrent.TimeUnit;

import org.omg.dds.core.DDSException;
import org.omg.dds.core.Duration;
import org.omg.dds.core.Time;
import org.omg.dds.core.policy.*;
import org.omg.dds.core.policy.Presentation.AccessScopeKind;
import org.opensplice.psm.java.core.TypeConverter;

public class PolicyConverter {
    /**
     * A utility class should not have a public constructor.
     */
    private PolicyConverter() { }

    public static Duration convert(DDS.Duration_t ddsduration) {
        if (DDS.DURATION_INFINITE.value.equals(ddsduration)) {
            return Duration.infinite();
        } else if (DDS.DURATION_ZERO.value.equals(ddsduration)) {
            return Duration.zero();
        }
        return new Duration(ddsduration.sec, ddsduration.nanosec);
    }

    public static Time convert(DDS.Time_t time) {
        // TODO: Fix extreme cases.
        return new Time(time.sec, time.nanosec);
    }

    public static Durability convert(DDS.DurabilityQosPolicy durability) {
        switch (durability.kind.value()) {
        case DDS.DurabilityQosPolicyKind._PERSISTENT_DURABILITY_QOS:
            return Durability.Persistent();
        case DDS.DurabilityQosPolicyKind._TRANSIENT_DURABILITY_QOS:
            return Durability.Transient();
        case DDS.DurabilityQosPolicyKind._TRANSIENT_LOCAL_DURABILITY_QOS:
            return Durability.TransientLocal();
        default:
            return Durability.Volatile();
        }
    }

    public static Reliability convert(DDS.ReliabilityQosPolicy reliability) {
        switch (reliability.kind.value()) {
        case DDS.ReliabilityQosPolicyKind._RELIABLE_RELIABILITY_QOS:
            return Reliability.Reliable(convert(reliability.max_blocking_time));
        default:
            return Reliability.BestEffort();
        }
    }

    public static History convert(DDS.HistoryQosPolicy history) {
        switch (history.kind.value()) {
        case DDS.HistoryQosPolicyKind._KEEP_ALL_HISTORY_QOS:
            return History.KeepAll();
        default:
            return History.KeepLast(history.depth);
        }
    }

    public static Liveliness convert(DDS.LivelinessQosPolicy liveliness) {
        switch (liveliness.kind.value()) {
        case DDS.LivelinessQosPolicyKind._AUTOMATIC_LIVELINESS_QOS:
            return Liveliness.Automatic();
        case DDS.LivelinessQosPolicyKind._MANUAL_BY_PARTICIPANT_LIVELINESS_QOS:
            return Liveliness
                    .ManualByParticipant(convert(liveliness.lease_duration));
        default:
            return Liveliness.ManualByTopic(convert(liveliness.lease_duration));
        }
    }

    public static DestinationOrder convert(
            DDS.DestinationOrderQosPolicy destination_order) {
        return destination_order.kind
                .equals(DDS.DestinationOrderQosPolicyKind.BY_RECEPTION_TIMESTAMP_DESTINATIONORDER_QOS)
                ? DestinationOrder.ReceptionTimestamp()
                : DestinationOrder.SourceTimestamp();
    }

    public static Ownership convert(DDS.OwnershipQosPolicy ownership) {
        return ownership.kind
                .equals(DDS.OwnershipQosPolicyKind.EXCLUSIVE_OWNERSHIP_QOS) ?
                        Ownership.Exclusive() : Ownership.Shared();
    }

    
    public static Presentation convert(DDS.PresentationQosPolicy ddspresentation) {
    	AccessScopeKind kind;
    	switch (ddspresentation.access_scope.value()) {
    	case DDS.PresentationQosPolicyAccessScopeKind._GROUP_PRESENTATION_QOS:
    		kind = AccessScopeKind.GROUP;
    		break;
    	case DDS.PresentationQosPolicyAccessScopeKind._INSTANCE_PRESENTATION_QOS:
    		kind = AccessScopeKind.INSTANCE;
    		break;
    		default:
    			kind = AccessScopeKind.TOPIC;
    			break;
    	}
    	Presentation presentation = new Presentation(kind,
    			ddspresentation.ordered_access, ddspresentation.coherent_access); 
    	return presentation;
    }
    
    public static EntityFactory convert(DDS.EntityFactoryQosPolicy ddsentityfactory) {
        if (ddsentityfactory.autoenable_created_entities) {
            return EntityFactory.AutoEnable();
        }
        return EntityFactory.ExplicitEnable();
    }

    public static DDS.Duration_t convert(Duration duration) {
        if (duration.isInfinite()) {
            return DDS.DURATION_INFINITE.value;
        }
        if (duration.isZero()) {
            return DDS.DURATION_ZERO.value;
        }
        return new DDS.Duration_t((int) duration.getDuration(TimeUnit.SECONDS),
                (int) duration.getRemainder(TimeUnit.SECONDS,
                        TimeUnit.NANOSECONDS));
    }

    public static DDS.DurabilityQosPolicy convert(Durability durability) {
        DDS.DurabilityQosPolicy ddsdurability = new DDS.DurabilityQosPolicy();
        if (Durability.Kind.PERSISTENT == durability.getKind()) {
            ddsdurability.kind = DDS.DurabilityQosPolicyKind.PERSISTENT_DURABILITY_QOS;
        } else if (Durability.Kind.TRANSIENT == durability.getKind()) {
            ddsdurability.kind = DDS.DurabilityQosPolicyKind.TRANSIENT_DURABILITY_QOS;
        } else if (Durability.Kind.TRANSIENT_LOCAL == durability.getKind()) {
            ddsdurability.kind = DDS.DurabilityQosPolicyKind.TRANSIENT_LOCAL_DURABILITY_QOS;
        } else {
            ddsdurability.kind = DDS.DurabilityQosPolicyKind.VOLATILE_DURABILITY_QOS;
        }
        return ddsdurability;
    }

    public static DDS.ReliabilityQosPolicy convert(Reliability reliability) {
        DDS.ReliabilityQosPolicy ddsreliability = new DDS.ReliabilityQosPolicy();
        if (Reliability.Kind.RELIABLE == reliability.getKind()) {
            ddsreliability.kind = DDS.ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
            ddsreliability.max_blocking_time = convert(reliability.getMaxBlockingTime());
            ddsreliability.synchronous = false;
        } else {
            ddsreliability.kind = DDS.ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
            ddsreliability.max_blocking_time = convert(reliability.getMaxBlockingTime());
            ddsreliability.synchronous = false;
        }
        return ddsreliability;
    }

    public static DDS.HistoryQosPolicy convert(History history) {
        DDS.HistoryQosPolicy ddshistory = new DDS.HistoryQosPolicy();
        if (history.getKind().equals(History.KEEP_ALL)) {
            ddshistory.kind = DDS.HistoryQosPolicyKind.KEEP_ALL_HISTORY_QOS;
        } else {
            ddshistory.kind = DDS.HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
            ddshistory.depth = history.getDepth();
        }
        return ddshistory;
    }

    public static DDS.DestinationOrderQosPolicy convert(
            DestinationOrder destinationOrder) {
        DDS.DestinationOrderQosPolicy ddsdestinationorder = new DDS.DestinationOrderQosPolicy();
        if (destinationOrder.getKind().equals(
                DestinationOrder.Kind.BY_RECEPTION_TIMESTAMP)) {
            ddsdestinationorder.kind = DDS.DestinationOrderQosPolicyKind.BY_RECEPTION_TIMESTAMP_DESTINATIONORDER_QOS;
        } else {
            ddsdestinationorder.kind = DDS.DestinationOrderQosPolicyKind.BY_SOURCE_TIMESTAMP_DESTINATIONORDER_QOS;
        }
        return ddsdestinationorder;
    }

    public static DDS.LivelinessQosPolicy convert(Liveliness liveliness) {
        DDS.LivelinessQosPolicy ddsliveliness = new DDS.LivelinessQosPolicy();
        if (liveliness.getKind().equals(Liveliness.Kind.AUTOMATIC)) {
            ddsliveliness.kind = DDS.LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS;
        } else if (liveliness.getKind().equals(
                Liveliness.Kind.MANUAL_BY_PARTICIPANT)) {
            ddsliveliness.kind = DDS.LivelinessQosPolicyKind.MANUAL_BY_PARTICIPANT_LIVELINESS_QOS;
        } else if (liveliness.getKind().equals(Liveliness.Kind.MANUAL_BY_TOPIC)) {
            ddsliveliness.kind = DDS.LivelinessQosPolicyKind.MANUAL_BY_TOPIC_LIVELINESS_QOS;
        }
        ddsliveliness.lease_duration = PolicyConverter.convert(liveliness
                .getLeaseDuration());
        return ddsliveliness;
    }

    public static DDS.OwnershipQosPolicy convert(Ownership ownership) {
        DDS.OwnershipQosPolicy ddsownership = new DDS.OwnershipQosPolicy();
        if (ownership.getKind().equals(Ownership.Kind.EXCLUSIVE)) {
            ddsownership.kind = DDS.OwnershipQosPolicyKind.EXCLUSIVE_OWNERSHIP_QOS;
        } else {
            ddsownership.kind = DDS.OwnershipQosPolicyKind.SHARED_OWNERSHIP_QOS;
        }
        return ddsownership;
    }
    
    public static DDS.PresentationQosPolicy convert(Presentation presentation) {
    	DDS.PresentationQosPolicy ddspresentation = new DDS.PresentationQosPolicy();
    	ddspresentation.coherent_access = presentation.isCoherentAccess();
    	ddspresentation.ordered_access = presentation.isOrderedAccess();
    	if (presentation.getAccessScope().equals(Presentation.AccessScopeKind.GROUP)) {
    		ddspresentation.access_scope = DDS.PresentationQosPolicyAccessScopeKind.GROUP_PRESENTATION_QOS;
    	} else if (presentation.getAccessScope().equals(Presentation.AccessScopeKind.INSTANCE)) {
    		ddspresentation.access_scope = DDS.PresentationQosPolicyAccessScopeKind.INSTANCE_PRESENTATION_QOS;
    	} else if (presentation.getAccessScope().equals(Presentation.AccessScopeKind.TOPIC)) {
    		ddspresentation.access_scope = DDS.PresentationQosPolicyAccessScopeKind.TOPIC_PRESENTATION_QOS;
    	}
    	return ddspresentation;
    }

    public static DDS.EntityFactoryQosPolicy convert(EntityFactory entityfactory) {
        DDS.EntityFactoryQosPolicy ddsentityfactory = new DDS.EntityFactoryQosPolicy();
        ddsentityfactory.autoenable_created_entities = entityfactory.isAutoEnable();
        return ddsentityfactory;
    }

    public static DDS.ResourceLimitsQosPolicy convert(ResourceLimits rl) {
        return new DDS.ResourceLimitsQosPolicy(
                rl.getMaxSamples(),
                rl.getMaxInstances(),
                rl.getMaxSamplesPerInstance());
    }

    public static ResourceLimits convert(DDS.ResourceLimitsQosPolicy rl) {
        return new ResourceLimits(
                rl.max_samples,
                rl.max_instances,
                rl.max_samples_per_instance);
    }

    public static DDS.GroupDataQosPolicy convert(GroupData gd){
        return new DDS.GroupDataQosPolicy(gd.getValue());
    }

    public static GroupData convert(DDS.GroupDataQosPolicy gd){
        return new GroupData(gd.value);
    }

    // -- Deadline QoS Policy --

    public static DDS.DeadlineQosPolicy convert(Deadline d) {

        DDS.DeadlineQosPolicy dl = (d == null)
                ? null
                : new DDS.DeadlineQosPolicy(TypeConverter.convert(d.getPeriod()));
        return dl;
    }

    public static  Deadline convert(DDS.DeadlineQosPolicy d) {
        return new Deadline(TypeConverter.convert(d.period));
    }

    // -- Latency Budget QoS Policy --

    public static DDS.LatencyBudgetQosPolicy convert(LatencyBudget lb) {
        return new DDS.LatencyBudgetQosPolicy(TypeConverter.convert(lb.getDuration()));
    }

    public static  LatencyBudget convert(DDS.LatencyBudgetQosPolicy lb) {
        return new LatencyBudget (TypeConverter.convert(lb.duration));
    }

    // -- TransportPriority Qos Policy --

    public static DDS.TransportPriorityQosPolicy convert(TransportPriority tp) {
        return new DDS.TransportPriorityQosPolicy(tp.getValue());
    }

    public static  TransportPriority convert(DDS.TransportPriorityQosPolicy tp) {
        return new TransportPriority(tp.value);
    }

    // -- Lifespan Qos Policy --
    public static DDS.LifespanQosPolicy convert(Lifespan l) {
        return new DDS.LifespanQosPolicy(TypeConverter.convert(l.getDuration()));
    }

    public static Lifespan convert( DDS.LifespanQosPolicy l) {
        return new Lifespan(TypeConverter.convert(l.duration));
    }

    // -- User Data Qos Policy --
    public static DDS.UserDataQosPolicy convert(UserData ud) {
        DDS.UserDataQosPolicy rv = (ud == null)
                ? null
                : new DDS.UserDataQosPolicy(ud.getValue());
        return rv;
    }
    public static UserData convert(DDS.UserDataQosPolicy ud) {
        return new UserData (ud.value);
    }

    // -- Ownership Strength Qos Policy --
    public static DDS.OwnershipStrengthQosPolicy convert(OwnershipStrength os) {
        DDS.OwnershipStrengthQosPolicy rv = (os == null)
                ? null
                : new DDS.OwnershipStrengthQosPolicy(os.getValue());
        return rv;
    }

    public static OwnershipStrength convert(DDS.OwnershipStrengthQosPolicy os) {
        return new OwnershipStrength (os.value);
    }

    //TODO: Fix the purge delay conversion
    public static DDS.WriterDataLifecycleQosPolicy convert(WriterDataLifecycle wdl) {
        DDS.Duration_t d = TypeConverter.convert(Duration.infinite());
        return new DDS.WriterDataLifecycleQosPolicy(wdl.isAutDisposeUnregisteredInstances(), d, d);
    }
    public static WriterDataLifecycle convert(DDS.WriterDataLifecycleQosPolicy wdl) {
        return
                wdl.autodispose_unregistered_instances
                        ? WriterDataLifecycle.AutDisposeUnregisterdInstances()
                        : WriterDataLifecycle.NotAutDisposeUnregisterdInstance();
    }
}
