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

package org.omg.dds.topic;

import DDS.DataReader;
import org.omg.dds.core.policy.*;
import org.omg.dds.core.qos.DataQos;
import org.omg.dds.runtime.DDSRuntime;
import org.omg.dds.runtime.TopicPolicyProvider;

public class TopicQos extends DataQos implements TopicPolicyProvider
{
    protected TopicData     topicData;
    private Lifespan        lifespan;

    /**
     * Creates a <code>TopicQos</code> with default policy value.
     * In terms of QoS Policy values this object will have exactly
     * the same values as those provided by the DDSRuntime.
     */
    public TopicQos() {
        super();
        this.setDefaults();
    }

    private void setDefaults() {
        TopicPolicyProvider provider =
                DDSRuntime.getInstance().getTopicPolicyProvider();
        this.topicData = provider.getTopicData();
        this.lifespan = provider.getLifespan();
    }

    public TopicQos(QosPolicy ... policies) {
        super(policies);
        this.setDefaults();
        for (QosPolicy p: policies) {
            switch (p.getPolicyId()) {
                case TopicData.ID:
                    this.topicData = (TopicData)p;
                    break;
                case Lifespan.ID:
                    this.lifespan = (Lifespan)p;
                    break;
            }
        }

    }
    public TopicQos(TopicData topicData,
                    Durability durability,
                    DurabilityService durabilityService,
                    Deadline deadline,
                    LatencyBudget latencyBudget,
                    Liveliness liveliness,
                    Reliability reliability,
                    DestinationOrder destinationOrder,
                    History history,
                    ResourceLimits resourceLimits,
                    TransportPriority transportPriority,
                    Lifespan lifespan,
                    Ownership ownership,
                    DataRepresentation dataRepresentation,
                    TypeConsistencyEnforcement typeConsistencyEnforcement)
    {
        super(durability,
                durabilityService,
                deadline,
                latencyBudget,
                liveliness,
                reliability,
                destinationOrder,
                history,
                resourceLimits,
                transportPriority,
                ownership,
                dataRepresentation,
                typeConsistencyEnforcement);

        this.topicData = topicData;
        this.lifespan = lifespan;
    }

    public TopicQos with(QosPolicy ... policies) {
        TopicData                   topicData                   = this.topicData;
        Durability                  durability                  = this.durability;
        DurabilityService           durabilityService           = this.durabilityService;
        Deadline                    deadline                    = this.deadline;
        LatencyBudget               latencyBudget               = this.latencyBudget;
        Liveliness                  liveliness                  = this.liveliness;
        Reliability                 reliability                 = this.reliability;
        DestinationOrder            destinationOrder            = this.destinationOrder;
        History                     history                     = this.history;
        ResourceLimits              resourceLimits              = this.resourceLimits;
        TransportPriority           transportPriority           = this.transportPriority;
        Lifespan                    lifespan                    = this.lifespan;
        Ownership                   ownership                   = this.ownership;
        DataRepresentation          dataRepresentation          = this.dataRepresentation;
        TypeConsistencyEnforcement  typeConsistencyEnforcement  = this.typeConsistencyEnforcement;

        for(QosPolicy p: policies) {
            switch (p.getPolicyId()) {
                case TopicData.ID:
                    topicData = (TopicData)p;
                    break;
                case Durability.ID:
                    durability = (Durability)p;
                    break;
                case DurabilityService.ID:
                    durabilityService = (DurabilityService)p;
                    break;
                case Deadline.ID:
                    deadline = (Deadline)p;
                    break;
                case LatencyBudget.ID:
                    latencyBudget = (LatencyBudget)p;
                    break;
                case Liveliness.ID:
                    liveliness = (Liveliness)p;
                    break;
                case  Reliability.ID:
                    reliability = (Reliability)p;
                    break;
                case DestinationOrder.ID:
                    destinationOrder = (DestinationOrder)p;
                    break;
                case History.ID:
                    history = (History)p;
                    break;
                case ResourceLimits.ID:
                    resourceLimits = (ResourceLimits)p;
                    break;
                case TransportPriority.ID:
                    transportPriority = (TransportPriority)p;
                    break;
                case Lifespan.ID:
                    lifespan = (Lifespan)p;
                    break;
                case Ownership.ID:
                    ownership = (Ownership)p;
                    break;
                case DataRepresentation.ID :
                    dataRepresentation = (DataRepresentation)p;
                    break;
                case TypeConsistencyEnforcement.ID:
                    typeConsistencyEnforcement = (TypeConsistencyEnforcement)p;
                    break;

                default:
                    System.err.println("[TopicQoS.new]: Unknown QoS");
            }
        }
        return new TopicQos(topicData,
                durability,
                durabilityService,
                deadline,
                latencyBudget,
                liveliness,
                reliability,
                destinationOrder,
                history,
                resourceLimits,
                transportPriority,
                lifespan,
                ownership,
                dataRepresentation,
                typeConsistencyEnforcement);
    }


    /**
     * @return the topicData
     */
    public TopicData getTopicData() {
        return this.topicData;
    }

    public Lifespan getLifespan() {
        return this.lifespan;
    }

}
