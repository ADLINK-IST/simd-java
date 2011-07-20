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

import org.omg.dds.core.policy.*;
import org.omg.dds.runtime.DDSRuntime;
import org.omg.dds.runtime.TopicPolicyProvider;

public class TopicQos implements TopicPolicyProvider
{
    private TopicData topicData;
    private Durability durability;
    private DurabilityService durabilityService;
    private Deadline deadline;
    private LatencyBudget latencyBudget;
    private Liveliness liveliness;
    private Reliability reliability;
    private DestinationOrder destinationOrder;
    private History history;
    private ResourceLimits resourceLimits;
    private TransportPriority transportPriority;
    private Lifespan lifespan;
    private Ownership ownership;
    private DataRepresentation dataRepresentation;
    private TypeConsistencyEnforcement typeConsistencyEnforcement;

    /**
     * Creates a <code>TopicQos</code> with default policy value.
     * In terms of QoS Policy values this object will have exactly
     * the same values as those provided by the DDSRuntime.
     */
    public TopicQos() {
        TopicPolicyProvider provider =
                DDSRuntime.getInstance().getTopicPolicyProvider();
        this.topicData                      = provider.getTopicData();
        this.durability                     = provider.getDurability();
        this.durabilityService              = provider.getDurabilityService();
        this.deadline                       = provider.getDeadline();
        this.latencyBudget                  = provider.getLatencyBudget();
        this.liveliness                     = provider.getLiveliness();
        this.reliability                    = provider.getReliability();
        this.destinationOrder               = provider.getDestinationOrder();
        this.history                        = provider.getHistory();
        this.resourceLimits                 = provider.getResourceLimits();
        this.transportPriority              = provider.getTransportPriority();
        this.lifespan                       = provider.getLifespan();
        this.ownership                      = provider.getOwnership();
        this.dataRepresentation             = provider.getRepresentation();
        this.typeConsistencyEnforcement     = provider.getTypeConsistency();
    }

    public TopicQos(QosPolicy ... policies) {
        for(QosPolicy p: policies) {
            switch (p.getPolicyId()) {
                case Reliability.ID:
                    this.reliability = (Reliability)p;
                    break;
                case Durability.ID:
                    this.durability = (Durability)p;
                    break;
                case Deadline.ID:
                    this.deadline = (Deadline)p;
                    break;
                /**
                 * TODO: Add other policies
                 */


                default:
                    System.err.println("[TopicQoS.new]: Unknown QoS");
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
                    TypeConsistencyEnforcement typeConsistencyEnforcement) {

        this.topicData                      = topicData;
        this.durability                     = durability;
        this.durabilityService              = durabilityService;
        this.deadline                       = deadline;
        this.latencyBudget                  = latencyBudget;
        this.reliability                    = reliability;
        this.destinationOrder               = destinationOrder;
        this.history                        = history;
        this.resourceLimits                 = resourceLimits;
        this.transportPriority              = transportPriority;
        this.lifespan                       = lifespan;
        this.ownership                      = ownership;
        this.dataRepresentation             = dataRepresentation;
        this.typeConsistencyEnforcement     = typeConsistencyEnforcement;
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

        //TODO: Complete code below!
        for (QosPolicy p: policies) {
            switch (p.getPolicyId()) {
                case Durability.ID:
                    durability = (Durability)p;
                    break;
                case Reliability.ID:
                    reliability = (Reliability)p;
                default:
                    System.err.println("[TopicQoS.with]: Unknown QoS");
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

    /**
     * @return the durability
     */
    public Durability getDurability() {
        return this.durability;
    }

    /**
     * @return the durabilityService
     */
    public DurabilityService getDurabilityService() {
        return this.durabilityService;
    }

    /**
     * @return the deadline
     */
    public Deadline getDeadline() {
        return this.deadline;
    }

    /**
     * @return the latencyBudget
     */
    public LatencyBudget getLatencyBudget() {
        return this.latencyBudget;
    }

    /**
     * @return the liveliness
     */
    public Liveliness getLiveliness() {
        return this.liveliness;
    }

    /**
     * @return the reliability
     */
    public Reliability getReliability() {
        return this.reliability;

    }

    /**
     * @return the destinationOrder
     */
    public DestinationOrder getDestinationOrder() {
        return  this.destinationOrder;
    }

    /**
     * @return the history
     */
    public History getHistory() {
        return this.history;
    }

    /**
     * @return the resourceLimits
     */
    public ResourceLimits getResourceLimits() {
        return  this.resourceLimits;
    }

    /**
     * @return the transportPriority
     */
    public TransportPriority getTransportPriority() {
        return this.transportPriority;
    }


    /**
     * @return the lifespan
     */
    public Lifespan getLifespan() {
        return this.lifespan;
    }

    /**
     * @return the ownership
     */
    public Ownership getOwnership() {
        return this.ownership;
    }

    public DataRepresentation getRepresentation() {
        return  this.dataRepresentation;
    }

    public TypeConsistencyEnforcement getTypeConsistency() {
        return this.typeConsistencyEnforcement;
    }
}
