package org.omg.dds.runtime;

import org.omg.dds.core.policy.EntityFactory;
import org.omg.dds.core.policy.UserData;

/**
 * This interface defines the method supported by those
 * classes that behave as QoS Policy providers. All DDS
 * implementation always provide a default policy provider
 * which provides the default value for each policy as
 * specified by the DDS specification or otherwise
 * specified by the user overriding some implementation specific
 * configuration files.
 */
public interface DomainParticipantPolicyProvider {
    /**
     * @return the userData
     */
    public UserData getUserData();

    /**
     * @return the entityFactory
     */
    public EntityFactory getEntityFactory();
}
