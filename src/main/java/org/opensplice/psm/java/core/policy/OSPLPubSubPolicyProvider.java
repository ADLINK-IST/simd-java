package org.opensplice.psm.java.core.policy;

import org.omg.dds.core.policy.EntityFactory;
import org.omg.dds.core.policy.GroupData;
import org.omg.dds.core.policy.Partition;
import org.omg.dds.core.policy.Presentation;
import org.omg.dds.runtime.PublisherPolicyProvider;
import org.omg.dds.runtime.SubscriberPolicyProvider;

public class OSPLPubSubPolicyProvider implements PublisherPolicyProvider, SubscriberPolicyProvider {
    private Presentation presentation =
            new Presentation(Presentation.AccessScopeKind.INSTANCE, false, false);
    private Partition partition =
            new Partition("");
    private GroupData groupData =
            new GroupData();

    public Presentation getPresentation() {
        return this.presentation;
    }

    public Partition getPartition() {
        return this.partition;
    }

    public GroupData getGroupData() {
        return this.groupData;
    }

    public EntityFactory getEntityFactory() {
        return EntityFactory.AutoEnable();
    }
}
