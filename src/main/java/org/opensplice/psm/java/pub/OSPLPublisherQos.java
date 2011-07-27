package org.opensplice.psm.java.pub;

import org.omg.dds.core.policy.EntityFactory;
import org.omg.dds.core.policy.GroupData;
import org.omg.dds.core.policy.Partition;
import org.omg.dds.core.policy.Presentation;
import org.omg.dds.core.policy.QosPolicy;
import org.omg.dds.pub.DataWriterQos;
import org.omg.dds.pub.PublisherQos;
import org.opensplice.psm.java.core.policy.OSPL;
import org.opensplice.psm.java.core.policy.OSPLGroupData;
import org.opensplice.psm.java.core.policy.OSPLPartition;

public class OSPLPublisherQos implements PublisherQos {

    private DDS.PublisherQos qos;

    public DDS.PublisherQos getQos() {
        return qos;
    }

    public OSPLPublisherQos(DDS.PublisherQos theqos) {
        qos = theqos;
    }
    
    public PublisherQos with(QosPolicy... policies) {
        DDS.PublisherQos newqos = new DDS.PublisherQos();
        newqos.entity_factory = qos.entity_factory;
        newqos.group_data = qos.group_data;
        newqos.partition = qos.partition;
        newqos.presentation = qos.presentation;
        for (QosPolicy policy : policies) {
            switch (policy.getPolicyId()) {
            case EntityFactory.ID:
            	newqos.entity_factory = OSPL.convert((EntityFactory)policy);
            	break;
            case GroupData.ID:
            	// TODO
            	break;
            case Partition.ID:
            	// TODO
            	break;
            case Presentation.ID:
            	newqos.presentation = OSPL.convert((Presentation)policy);
            	break;
           	default:
           		throw (new RuntimeException(policy.getPolicyName() + 
           				" is not applicable for PublisherQos"));
            }
        }
        return new OSPLPublisherQos(newqos);
    }

    @Override
    public Presentation getPresentation() {
        return OSPL.convert(qos.presentation);
    }

    @Override
    public Partition getPartition() {
        return new OSPLPartition(qos.partition.name);
    }

    @Override
    public GroupData getGroupData() {
        return new OSPLGroupData(qos.group_data);
    }

    @Override
    public EntityFactory getEntityFactory() {
        if (qos.entity_factory.autoenable_created_entities) {
            return EntityFactory.AutoEnable();
        }
        return EntityFactory.ExplicitEnable();
    }
}
