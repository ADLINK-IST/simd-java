package org.omg.dds.core.qos;


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

import org.omg.dds.core.policy.*;
import org.omg.dds.runtime.DDSRuntime;
import org.omg.dds.runtime.TopicPolicyProvider;



import DDS.DataReader;
import org.omg.dds.core.policy.*;
import org.omg.dds.runtime.DDSRuntime;
import org.omg.dds.runtime.TopicPolicyProvider;

public class DataQos
{
    protected Durability durability;
    protected DurabilityService durabilityService;
    protected Deadline deadline;
    protected LatencyBudget latencyBudget;
    protected Liveliness liveliness;
    protected Reliability reliability;
    protected DestinationOrder destinationOrder;
    protected History history;
    protected ResourceLimits resourceLimits;
    protected TransportPriority transportPriority;
    protected Ownership ownership;
    protected DataRepresentation dataRepresentation;
    protected TypeConsistencyEnforcement typeConsistencyEnforcement;

    /**
     * Creates a <code>TopicQos</code> with default policy value.
     * In terms of QoS Policy values this object will have exactly
     * the same values as those provided by the DDSRuntime.
     */
    public DataQos() {
        this.setDefaults();
    }

    private void setDefaults() {
        TopicPolicyProvider provider =
                DDSRuntime.getInstance().getTopicPolicyProvider();
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
        this.ownership                      = provider.getOwnership();
        this.dataRepresentation             = provider.getRepresentation();
        this.typeConsistencyEnforcement     = provider.getTypeConsistency();
    }

    private void initMembers(QosPolicy ... policies) {
        for(QosPolicy p: policies) {
            switch (p.getPolicyId()) {
                case Durability.ID:
                    this.durability = (Durability)p;
                    break;
                case DurabilityService.ID:
                    this.durabilityService = (DurabilityService)p;
                    break;
                case Deadline.ID:
                    this.deadline = (Deadline)p;
                    break;
                case LatencyBudget.ID:
                    this.latencyBudget = (LatencyBudget)p;
                    break;
                case Liveliness.ID:
                    this.liveliness = (Liveliness)p;
                    break;
                case  Reliability.ID:
                    this.reliability = (Reliability)p;
                    break;
                case DestinationOrder.ID:
                    this.destinationOrder = (DestinationOrder)p;
                    break;
                case History.ID:
                    this.history = (History)p;
                    break;
                case ResourceLimits.ID:
                    this.resourceLimits = (ResourceLimits)p;
                    break;
                case TransportPriority.ID:
                    this.transportPriority = (TransportPriority)p;
                    break;
                case Ownership.ID:
                    this.ownership = (Ownership)p;
                    break;
                case DataRepresentation.ID :
                    this.dataRepresentation = (DataRepresentation)p;
                    break;
                case TypeConsistencyEnforcement.ID:
                    this.typeConsistencyEnforcement = (TypeConsistencyEnforcement)p;
                    break;
            }
        }
    }
    public DataQos(QosPolicy ... policies) {
        this.setDefaults();
        this.initMembers(policies);
    }
    public DataQos(
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

        this.durability                     = durability;
        this.durabilityService              = durabilityService;
        this.deadline                       = deadline;
        this.latencyBudget                  = latencyBudget;
        this.reliability                    = reliability;
        this.destinationOrder               = destinationOrder;
        this.history                        = history;
        this.resourceLimits                 = resourceLimits;
        this.transportPriority              = transportPriority;
        this.ownership                      = ownership;
        this.dataRepresentation             = dataRepresentation;
        this.typeConsistencyEnforcement     = typeConsistencyEnforcement;
    }

    public DataQos with(QosPolicy ... policies) {
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
        Ownership                   ownership                   = this.ownership;
        DataRepresentation          dataRepresentation          = this.dataRepresentation;
        TypeConsistencyEnforcement  typeConsistencyEnforcement  = this.typeConsistencyEnforcement;

        for(QosPolicy p: policies) {
            switch (p.getPolicyId()) {
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
                case Ownership.ID:
                    ownership = (Ownership)p;
                    break;
                case DataRepresentation.ID :
                    dataRepresentation = (DataRepresentation)p;
                    break;
                case TypeConsistencyEnforcement.ID:
                    typeConsistencyEnforcement = (TypeConsistencyEnforcement)p;
                    break;

            }
        }
        return new DataQos(
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
                ownership,
                dataRepresentation,
                typeConsistencyEnforcement);
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