package org.opensplice.psm.java.core.policy;

import org.omg.dds.core.Duration;
import org.omg.dds.core.policy.*;
import org.omg.dds.runtime.TopicPolicyProvider;

import java.security.acl.Owner;

public class OSPLTopicPolicyProvider implements TopicPolicyProvider {

    private TopicData topicData = new TopicData();

    private DurabilityService durabilityService =
            new DurabilityService(History.KeepLast(), Duration.zero(), -1, -1, -1);

    private Deadline deadline = new Deadline(Duration.infinite());

    private LatencyBudget latencyBudget = new LatencyBudget(Duration.zero());

    private ResourceLimits resourceLimits = new ResourceLimits(-1, -1, -1);

    private TransportPriority transportPriority = new TransportPriority(0);

    private Lifespan lifespan = new Lifespan(Duration.infinite());


    public TopicData getTopicData() {
        return this.topicData;
    }

    public Durability getDurability() {
        return Durability.Volatile();
    }

    public DurabilityService getDurabilityService() {
        return this.durabilityService;
    }

    public Deadline getDeadline() {
        return this.deadline;
    }

    public LatencyBudget getLatencyBudget() {
        return this.latencyBudget;
    }

    public Liveliness getLiveliness() {
        return Liveliness.Automatic();
    }

    public Reliability getReliability() {
        return Reliability.BestEffort();
    }

    public DestinationOrder getDestinationOrder() {
        return DestinationOrder.ReceptionTimestamp();
    }

    public History getHistory() {
        return History.KeepLast();
    }

    public ResourceLimits getResourceLimits() {
        return this.resourceLimits;
    }

    public TransportPriority getTransportPriority() {
        return this.transportPriority;
    }

    public Lifespan getLifespan() {
        return this.lifespan;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Ownership getOwnership() {
        return Ownership.Shared();  //To change body of implemented methods use File | Settings | File Templates.
    }

    public DataRepresentation getRepresentation() {
        // TODO: Add support for specifing the Data Rep.
        return null;
    }

    public TypeConsistencyEnforcement getTypeConsistency() {
        return TypeConsistencyEnforcement.ExactType();
    }
}
