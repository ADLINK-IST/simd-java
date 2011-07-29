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

package org.omg.dds.core.policy;


/**
 * Controls the behavior of the {@link org.omg.dds.core.Entity} when acting as a factory for
 * other entities. In other words, configures the side-effects of the
 * <code>create_*</code> operations.
 * 
 * <b>Concerns:</b> {@link org.omg.dds.domain.DomainParticipantFactory},
 *                  {@link org.omg.dds.domain.DomainParticipant}, {@link org.omg.dds.pub.Publisher},
 *                  {@link org.omg.dds.sub.Subscriber}
 * 
 * <b>RxO:</b> No
 * 
 * <b>Changeable:</b> Yes
 * 
 * This policy controls the behavior of the Entity as a factory for other entities.
 * 
 * This policy concerns only DomainParticipant (as factory for Publisher,
 * Subscriber, and {@link org.omg.dds.topic.Topic}), Publisher (as factory for
 * {@link org.omg.dds.pub.DataWriter}), and Subscriber (as factory for {@link org.omg.dds.sub.DataReader}).
 * 
 * This policy is mutable. A change in the policy affects only the entities
 * created after the change; not the previously created entities.
 *  
 * The setting of autoenableCreatedEntities to true indicates that the
 * factory <code>create&lt;<i>entity</i>&gt;</code> operation will
 * automatically invoke the {@link org.omg.dds.core.Entity#enable()} operation each time a new
 * Entity is created. Therefore, the Entity returned by
 * <code>create&lt;<i>entity</i>&gt;</code> will already be enabled. A
 * setting of false indicates that the Entity will not be automatically
 * enabled. The application will need to enable it explicitly by means of the
 * enable operation.
 * 
 * The default setting of autoenableCreatedEntities = true means that, by
 * default, it is not necessary to explicitly call enable on newly created
 * entities.
 */
public class EntityFactory implements QosPolicy {

	private static final long serialVersionUID = 1703684279864746623L;
	
	public static final int ID = 15;
    private static final String NAME = "EntityFactory";

    private final static EntityFactory AUTO_ENABLE = new EntityFactory(true);
    private final static EntityFactory EXPLICIT_ENABLE = new EntityFactory(false);
    private boolean autoEnable;

    private EntityFactory(boolean autoEnable) {
        this.autoEnable = autoEnable;
    }

    public static final EntityFactory AutoEnable() {
        return AUTO_ENABLE;
    }

    public static final EntityFactory ExplicitEnable() {
        return EXPLICIT_ENABLE;
    }

    /**
     * @return the autoEnableCreatedEntities
     */
    public boolean isAutoEnable() {
        return this.autoEnable;
    }


    public int getPolicyId() { return ID; }

    public String getPolicyName() { return NAME; }

    public boolean equals(Object that) {
        return (this == that);
    }
}
