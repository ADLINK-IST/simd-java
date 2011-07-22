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

package org.omg.dds.core;

import java.util.Collection;
import java.util.EventListener;

import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.pub.DataWriter;
import org.omg.dds.pub.Publisher;
import org.omg.dds.sub.DataReader;
import org.omg.dds.sub.Subscriber;
import org.omg.dds.topic.ContentFilteredTopic;
import org.omg.dds.topic.MultiTopic;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;


/**
 * This class is the abstract base class for all the DCPS objects that
 * support QoS policies, a listener and a status condition.
 * 
 * @param <SELF>        The most-derived DDS-standard interface implemented
 *                      by this entity.
 */
public interface Entity<SELF extends Entity<SELF>>
{




    /**
     * This operation enables the Entity. Entity objects can be created
     * either enabled or disabled. This is controlled by the value of the
     * {@link org.omg.dds.core.policy.EntityFactory} on the corresponding factory for the
     * Entity.
     * 
     * The default setting of {@link org.omg.dds.core.policy.EntityFactory} is such that, by
     * default, it is not necessary to explicitly call enable on newly
     * created entities.
     * 
     * The enable operation is idempotent. Calling enable on an already
     * enabled Entity has no effect.
     * 
     * If an Entity has not yet been enabled, the following kinds of
     * operations may be invoked on it:
     * 
     * <ul>
     *     <li>Operations to set or get an Entity's QoS policies (including
     *         default QoS policies) and listener</li>
     *     <li>{@link #getStatusCondition()}</li>
     *     <li>'factory' operations and {@link #close()}</li>
     *     <li>{@link #getStatusChanges(Collection)} and other get status
     *         operations (although the status of a disabled entity never
     *         changes)</li>
     *     <li>'lookup' operations</li>
     * </ul>
     * 
     * Other operations may explicitly state that they may be called on
     * disabled entities; those that do not will fail with
     *  {@link NotEnabledException}.
     *  
     *  It is legal to delete an Entity that has not been enabled by calling
     *  {@link #close()}. Entities created from a factory that is disabled
     *  are created disabled regardless of the setting of
     *  {@link org.omg.dds.core.policy.EntityFactory}.
     *  
     *  Calling enable on an Entity whose factory is not enabled will fail
     *  with {@link PreconditionNotMetException}.
     *  
     *  If {@link org.omg.dds.core.policy.EntityFactory#isAutoEnableCreatedEntities()} is
     *  true, the enable operation on the factory will automatically enable
     *  all entities created from the factory.
     *  
     *  The Listeners associated with an entity are not called until the
     *  entity is enabled. {@link Condition}s associated with an entity that
     *  is not enabled are "inactive," that is, have a triggerValue == false.
     */
    public void enable();

    /**
     * This operation allows access to the {@link StatusCondition} associated
     * with the Entity. The returned condition can then be added to a
     * {@link WaitSet} so that the application can wait for specific status
     * changes that affect the Entity.
     */
    public StatusCondition<SELF> getStatusCondition();

    /**
     * This operation retrieves the list of communication statuses in the
     * Entity that are 'triggered.' That is, the list of statuses whose value
     * has changed since the last time the application read the status.
     * 
     * When the entity is first created or if the entity is not enabled, all
     * communication statuses are in the "untriggered" state so the list
     * returned will be empty.
     * 
     * The list of statuses returned refers to the statuses that are
     * triggered on the Entity itself and does not include statuses that
     * apply to contained entities.
     * 
     * @param   statuses    a container for the resulting statuses; its
     *                      contents will be overwritten by the result of
     *                      this operation.
     * @return  the argument as a convenience in order to facilitate call
     *          chaining. 
     */
    public Collection<Class<? extends Status<?>>> getStatusChanges(
            Collection<Class<? extends Status<?>>> statuses);

    /**
     * @return  the {@link InstanceHandle} that represents the Entity.
     */
    public InstanceHandle getInstanceHandle();

    /**
     * Halt communication and dispose the resources held by this Entity.
     * 
     * Closing an Entity implicitly closes all of its contained objects, if
     * any. For example, closing a Publisher also closes all of its contained
     * DataWriters.
     * 
     * An Entity cannot be closed if it has any unclosed dependent objects,
     * not including contained objects. These include the following:
     * 
     * <ul>
     *     <li>A {@link Topic} cannot be closed if it is still in use by any
     *         {@link ContentFilteredTopic}s or {@link MultiTopic}s.</li>
     *     <li>A Topic cannot be closed if any {@link DataWriter}s or
     *         {@link DataReader} is still using it.</li>
     *     <li>A DataReader cannot be closed if it has any outstanding loans
     *         as a result of a call to {@link DataReader#read()},
     *         {@link DataReader#take()}, or one of the variants thereof.
     *         </li>
     * </ul>
     * 
     * The deletion of a {@link DataWriter} will automatically unregister all
     * instances. Depending on the settings of the
     * {@link org.omg.dds.core.policy.WriterDataLifecycle}, the deletion of the DataWriter
     * may also dispose all instances.
     * 
     * @throws  PreconditionNotMetException     if close is called on an
     *          Entity with unclosed dependent object(s), not including
     *          contained objects.
     * 
     * @see     TopicDescription#close()
     */
    public void close();

    /**
     * Indicates that references to this object may go out of scope but that
     * the application expects to look it up again later. Therefore, the
     * Service must consider this object to be still in use and may not
     * close it automatically.
     */
    public void retain();
}
