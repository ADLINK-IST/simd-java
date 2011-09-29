package org.opensplice.psm.java.core.policy;

import org.omg.dds.core.policy.OwnershipStrength;
import org.omg.dds.core.policy.UserData;
import org.omg.dds.core.policy.WriterDataLifecycle;
import org.omg.dds.runtime.DataWriterPolicyProvider;


public class OSPLDataWriterPolicyProvider extends OSPLTopicPolicyProvider implements DataWriterPolicyProvider {
    private UserData userData = new UserData();
    private OwnershipStrength ownershipStrength = new OwnershipStrength(0);

    public UserData getUserData() {
        return this.userData;
    }

    public OwnershipStrength getOwnershipStrength() {
        return this.ownershipStrength;
    }

    public WriterDataLifecycle getWriterDataLifecycle() {
        return WriterDataLifecycle.AutDisposeUnregisterdInstances();
    }
}
