package org.opensplice.psm.java.core.policy;

import org.omg.dds.core.policy.EntityFactory;
import org.omg.dds.core.policy.UserData;
import org.omg.dds.runtime.DomainParticipantPolicyProvider;

public class OSPLDomainParticipantPolicyProvider implements DomainParticipantPolicyProvider {

    private UserData userData = new UserData();

    public UserData getUserData() {
        return this.userData;
    }

    public EntityFactory getEntityFactory() {
        return EntityFactory.AutoEnable();
    }
}
