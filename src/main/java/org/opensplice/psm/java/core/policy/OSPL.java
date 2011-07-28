package org.opensplice.psm.java.core.policy;

import java.util.concurrent.TimeUnit;


import org.omg.dds.core.Duration;
import org.omg.dds.core.Time;
import org.omg.dds.core.policy.DestinationOrder;
import org.omg.dds.core.policy.Durability;
import org.omg.dds.core.policy.EntityFactory;
import org.omg.dds.core.policy.History;
import org.omg.dds.core.policy.Liveliness;
import org.omg.dds.core.policy.Ownership;
import org.omg.dds.core.policy.Presentation;
import org.omg.dds.core.policy.Presentation.AccessScopeKind;
import org.omg.dds.core.policy.Reliability;
import org.omg.dds.core.policy.WriterDataLifecycle;

import DDS.DestinationOrderQosPolicyKind;
import DDS.ReliabilityQosPolicyKind;

public class OSPL {
    /**
     * A utility class should not have a public constructor.
     */
    private OSPL() { }

    public static Duration convert(DDS.Duration_t ddsduration) {
        if (DDS.DURATION_INFINITE.value.equals(ddsduration)) {
            return Duration.infinite();
        } else if (DDS.DURATION_ZERO.value.equals(ddsduration)) {
            return Duration.zero();
        }
        return new Duration(ddsduration.sec, ddsduration.nanosec);
    }
    
    public static Time convert(DDS.Time_t ddstime) {
    	if (DDS.TIMESTAMP_INVALID.value.equals(ddstime)) {
    		return Time.invalidTime();
    	}
    	return new Time(ddstime.sec, ddstime.nanosec);
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
                .equals(DestinationOrderQosPolicyKind.BY_RECEPTION_TIMESTAMP_DESTINATIONORDER_QOS)
                ? DestinationOrder.ReceptionTimestamp()
                : DestinationOrder.SourceTimestamp();
    }

    public static Ownership convert(DDS.OwnershipQosPolicy ownership) {
        return ownership.kind
                .equals(DDS.OwnershipQosPolicyKind.EXCLUSIVE_OWNERSHIP_QOS) ?
                        Ownership.Exclusive() : Ownership.Shared();
    }

    public static WriterDataLifecycle convert(
            DDS.WriterDataLifecycleQosPolicy writerDataLifecycle) {
        return writerDataLifecycle.autodispose_unregistered_instances ? WriterDataLifecycle
                .AutDisposeUnregisterdInstances()
                :
                WriterDataLifecycle.NotAutDisposeUnregisterdInstance();
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
        if (Durability.Kind.PERSISTENT.equals(durability)) {
            ddsdurability.kind = DDS.DurabilityQosPolicyKind.PERSISTENT_DURABILITY_QOS;
        } else if (Durability.Kind.TRANSIENT.equals(durability)) {
            ddsdurability.kind = DDS.DurabilityQosPolicyKind.TRANSIENT_DURABILITY_QOS;
        } else if (Durability.Kind.TRANSIENT_LOCAL.equals(durability)) {
            ddsdurability.kind = DDS.DurabilityQosPolicyKind.TRANSIENT_LOCAL_DURABILITY_QOS;
        } else {
            ddsdurability.kind = DDS.DurabilityQosPolicyKind.VOLATILE_DURABILITY_QOS;
        }
        return ddsdurability;
    }

    public static DDS.ReliabilityQosPolicy convert(Reliability reliability) {
        DDS.ReliabilityQosPolicy ddsreliability = new DDS.ReliabilityQosPolicy();
        if (Reliability.Kind.RELIABLE.equals(reliability)) {
            ddsreliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
            ddsreliability.max_blocking_time = convert(reliability
                    .getMaxBlockingTime());
        } else {
            ddsreliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
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
        ddsliveliness.lease_duration = OSPL.convert(liveliness
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

}
