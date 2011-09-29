/* Copyright 2010, Object Management Group, Inc.
 * Copyright 2010, PrismTech, Inc.
 * Copyright 2010, Real-Time Innovations, Inc.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.omg.dds.pub;

import org.omg.dds.core.policy.QosPolicy;
import org.omg.dds.core.policy.EntityFactory;
import org.omg.dds.core.policy.GroupData;
import org.omg.dds.core.policy.Partition;
import org.omg.dds.core.policy.Presentation;
import org.omg.dds.runtime.DDSRuntime;
import org.omg.dds.runtime.PublisherPolicyProvider;

import java.security.Policy;


public class PublisherQos  {

    static PublisherPolicyProvider provider =
            DDSRuntime.getInstance().getPublisherPolicyProvider();

    private Presentation   presentation   = provider.getPresentation();
    private Partition      partition      = provider.getPartition();
    private GroupData      groupData      = provider.getGroupData();
    private EntityFactory  entityFactory  = provider.getEntityFactory();

    private void setDefaults() {
        this.presentation   = provider.getPresentation();
        this.partition      = provider.getPartition();
        this.groupData      = provider.getGroupData();
        this.entityFactory  = provider.getEntityFactory();
    }

    public PublisherQos() { }

    public PublisherQos(QosPolicy ... policies) {
        this.setDefaults();
        for (QosPolicy p: policies) {
            switch (p.getPolicyId()) {
                case Presentation.ID:
                    this.presentation = (Presentation)p;
                    break;
                case Partition.ID:
                    this.partition = (Partition)p;
                    break;
                case GroupData.ID:
                    this.groupData = (GroupData)p;
                    break;
                case EntityFactory.ID:
                    this.entityFactory = (EntityFactory)p;
                    break;
                default:
                    System.err.println("[PublisherQos]>> Unknown QoS passed to constructor");
            }
        }
    }
    public PublisherQos with(QosPolicy ... policies) {
        Presentation   presentation   = provider.getPresentation();
        Partition      partition      = provider.getPartition();
        GroupData      groupData      = provider.getGroupData();
        EntityFactory  entityFactory  = provider.getEntityFactory();
          for (QosPolicy p: policies) {
            switch (p.getPolicyId()) {
                case Presentation.ID:
                    presentation = (Presentation)p;
                    break;
                case Partition.ID:
                    partition = (Partition)p;
                    break;
                case GroupData.ID:
                    groupData = (GroupData)p;
                    break;
                case EntityFactory.ID:
                    entityFactory = (EntityFactory)p;
                    break;
                default:
                    System.err.println("[PublisherQos]>> Unknown QoS passed to constructor");
            }

        }
        return new PublisherQos(presentation, partition, groupData, entityFactory);
    }
    /**
     * @return the presentation
     */
    public Presentation getPresentation() {
        return this.presentation;
    }

    /**
     * @return the partition
     */
    public Partition getPartition() {
        return this.partition;
    }

    /**
     * @return the groupData
     */
    public GroupData getGroupData() {
        return this.groupData;
    }

    /**
     * @return the entityFactory
     */
    public EntityFactory getEntityFactory() {
        return  this.entityFactory;
    }

}
