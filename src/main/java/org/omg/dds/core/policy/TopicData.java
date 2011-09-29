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
 * User data not known by the middleware, but distributed by means of
 * built-in topics. The default value is an empty (zero-sized) sequence.
 * 
 * <b>Concerns:</b> {@link org.omg.dds.topic.Topic}
 * 
 * <b>RxO:</b> No
 * 
 * <b>Changeable:</b> Yes
 * 
 * The purpose of this QoS is to allow the application to attach additional
 * information to the created {@link org.omg.dds.topic.Topic}s such that when a remote
 * application discovers their existence it can examine the information and
 * use it in an application-defined way. In combination with the listeners on
 * the {@link org.omg.dds.sub.DataReader} and {@link org.omg.dds.pub.DataWriter} as well as by means of
 * operations such as
 * {@link org.omg.dds.domain.DomainParticipant#ignoreTopic(org.omg.dds.core.InstanceHandle)},
 * these QoS can assist an application to extend the provided QoS.
 */
public class TopicData implements QosPolicy {

    static final long serialVersionUID = 4791687692148780088L;

    public static final int ID = 18;
    private static final String NAME = "TopicData";
    private static final TopicData EMPTY_TOPIC_DATA = new TopicData();
    private final byte value[];

    public TopicData() {
        this.value = new byte[0];
    }

    public TopicData(byte value[]) {
        this.value = new byte[value.length];
        System.arraycopy(value, 0, this.value, 0, value.length);
    }

    /**
     * Copy the data into the given array, starting at the index at the given
     * offset.
     *
     * @return The total number of bytes in the data, independent of the number
     *         of bytes copied. Callers can use this result to determine if the
     *         output array is long enough or, if it is long enough, what range
     *         within it contains valid data.
     */
    public byte[] getValue() {
        return this.value.clone();
    }

    public byte valueAt(int i) {
        return value[i];
    }



    /**
     * @return the length of the <code>value</code> property.
     */
    public int getLength() {
        return this.value.length;
    }

    public int getPolicyId() {
        return ID;
    }

    public String getPolicyName() {
        return NAME;
    }

    public TopicData EmptyTopicData() {
        return EMPTY_TOPIC_DATA;
    }
}