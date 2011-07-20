package org.omg.dds.runtime;

import org.omg.dds.core.policy.*;

/**
 * This interface defines the method supported by those
 * classes that behave as QoS Policy providers. All DDS
 * implementation always provide a default policy provider
 * which provides the default value for each policy as
 * specified by the DDS specification or otherwise
 * specified by the user overriding some implementation specific
 * configuration files.
 */
public interface DataWriterPolicyProvider {
     /**
     * @return the durability
     */
    public Durability getDurability();

    /**
     * @return the durabilityService
     */
    public DurabilityService getDurabilityService();

    /**
     * @return the deadline
     */
    public Deadline getDeadline();

    /**
     * @return the latencyBudget
     */
    public LatencyBudget getLatencyBudget();

    /**
     * @return the liveliness
     */
    public Liveliness getLiveliness();

    /**
     * @return the reliability
     */
    public Reliability getReliability();

    /**
     * @return the destinationOrder
     */
    public DestinationOrder getDestinationOrder();

    /**
     * @return the history
     */
    public History getHistory();

    /**
     * @return the resourceLimits
     */
    public ResourceLimits getResourceLimits();

    /**
     * @return the transportPriority
     */
    public TransportPriority getTransportPriority();

    /**
     * @return the lifespan
     */
    public Lifespan getLifespan();

    /**
     * @return the userData
     */
    public UserData getUserData();

    /**
     * @return the ownership
     */
    public Ownership getOwnership();

    /**
     * @return the ownershipStrength
     */
    public OwnershipStrength getOwnershipStrength();

    /**
     * @return the writerDataLifecycle
     */
    public WriterDataLifecycle getWriterDataLifecycle();

    public DataRepresentation getRepresentation();

    public TypeConsistencyEnforcement getTypeConsistency();
}
