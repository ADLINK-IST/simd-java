package org.omg.dds.runtime;

import org.omg.dds.core.policy.EntityFactory;
import org.omg.dds.core.policy.GroupData;
import org.omg.dds.core.policy.Partition;
import org.omg.dds.core.policy.Presentation;

/**
 * This interface defines the method supported by those
 * classes that behave as QoS Policy providers. All DDS
 * implementation always provide a default policy provider
 * which provides the default value for each policy as
 * specified by the DDS specification or otherwise
 * specified by the user overriding some implementation specific
 * configuration files.
 */

public interface PublisherPolicyProvider {
     /**
     * @return the presentation
     */
    public Presentation getPresentation();

    /**
     * @return the partition
     */
    public Partition getPartition();

    /**
     * @return the groupData
     */
    public GroupData getGroupData();

    /**
     * @return the entityFactory
     */
    public EntityFactory getEntityFactory();
}
