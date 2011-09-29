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

package org.omg.dds.sub;

import org.omg.dds.core.policy.*;
import org.omg.dds.core.qos.DataQos;
import org.omg.dds.runtime.DDSRuntime;
import org.omg.dds.runtime.DataReaderPolicyProvider;


public class DataReaderQos extends DataQos {


    private UserData            userData;
    private ReaderDataLifecycle readerDataLifecycle;
    private TimeBasedFilter     timeBasedFilter;

    private void setDefaults() {
        DataReaderPolicyProvider provider = DDSRuntime.getInstance()
                        .getDataReaderPolicyProvider();

        this.userData = provider.getUserData();
        this.readerDataLifecycle = provider.getReaderDataLifecycle();
        this.timeBasedFilter = provider.getTimeBasedFilter();

    }

    public DataReaderQos() {
        super();
        this.setDefaults();
    }

    public DataReaderQos(QosPolicy ... policies) {
        super(policies);
        this.setDefaults();
        for (QosPolicy p: policies) {
            switch (p.getPolicyId()) {
                case ReaderDataLifecycle.ID:
                    this.readerDataLifecycle = (ReaderDataLifecycle)p;
                    break;
                case UserData.ID:
                    this.userData = (UserData)p;
                    break;
                case TimeBasedFilter.ID:
                    this.timeBasedFilter = (TimeBasedFilter)p;
                    break;
            }
        }
    }



    public DataReaderQos with(QosPolicy... policies) {
        Durability                  durability                  = this.durability;
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
        UserData                    userData                    = this.userData;
        ReaderDataLifecycle         writerDataLifecycle         = this.readerDataLifecycle;
        TimeBasedFilter             timeBasedFilter             = this.timeBasedFilter;

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
                case TimeBasedFilter.ID:
                    timeBasedFilter = (TimeBasedFilter)p;
                    break;
                case DataRepresentation.ID :
                    dataRepresentation = (DataRepresentation)p;
                    break;
                case TypeConsistencyEnforcement.ID:
                    typeConsistencyEnforcement = (TypeConsistencyEnforcement)p;
                    break;
                case UserData.ID:
                    userData = (UserData)p;
                    break;
                case ReaderDataLifecycle.ID:
                    readerDataLifecycle = (ReaderDataLifecycle)p;
                    break;

                default:
                    System.err.println("[TopicQoS.new]: Unknown QoS");
            }
        }
        return new DataReaderQos(
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
             //   dataRepresentation,
                typeConsistencyEnforcement,
                timeBasedFilter,
                userData,
                writerDataLifecycle);
    }

    /**
     * @return the writerDataLifecycle
     */
    public ReaderDataLifecycle getReaderDataLifecycle() {
        return this.readerDataLifecycle;
    }
    public UserData getUserData() {
        return this.userData;
    }

    public TimeBasedFilter getTimeBasedFilter() {
        return this.timeBasedFilter;
    }


}
