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

public interface DataReaderPolicyProvider {
    /**
     * @return the durability
     */
    public Durability getDurability();

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
     * @return the userData
     */
    public UserData getUserData();

    /**
     * @return the ownership
     */
    public Ownership getOwnership();

    /**
     * @return the timeBasedFilter
     */
    public TimeBasedFilter getTimeBasedFilter();

    /**
     * @return the readerDataLifecycle
     */
    public ReaderDataLifecycle getReaderDataLifecycle();

    public DataRepresentation getRepresentation();

    public TypeConsistencyEnforcement getTypeConsistency();
}
