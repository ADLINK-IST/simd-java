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
 * <b>Concerns:</b> {@link org.omg.dds.pub.Publisher}, {@link org.omg.dds.sub.Subscriber}
 *
 * <b>RxO:</b> No
 *
 * <b>Changeable:</b> Yes
 *
 * The purpose of this QoS is to allow the application to attach additional
 * information to the created {@link org.omg.dds.pub.Publisher} or {@link org.omg.dds.sub.Subscriber}. The
 * value of the GROUP_DATA is available to the application on the
 * {@link org.omg.dds.sub.DataReader} and {@link org.omg.dds.pub.DataWriter} entities and is propagated by
 * means of the built-in topics.
 *
 * This QoS can be used by an application combination with the
 * {@link org.omg.dds.sub.DataReaderListener} and {@link org.omg.dds.pub.DataWriterListener} to implement
 * matching policies similar to those of the {@link Partition}
 * except the decision can be made based on an application-defined policy.
 */
public class GroupData implements QosPolicy {
    public final static int ID = 20;
    private final static String NAME = "GroupData";
    private static final GroupData EMPTY_GROUP_DATA = new GroupData();


    private final byte value[];

    public GroupData() {
        this.value = new byte[0];
    }
    public GroupData(byte v[]) {
        this.value = new byte[v.length];
        System.arraycopy(v, 0, this.value, 0, v.length);
    }

    public byte[] getValue() {
        return this.value.clone();
    }
    public byte valueAt(int offset) {
        return this.value[offset];
    }

    /**
     * @return  the length of the <code>value</code> property.
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

    public GroupData EmptyGroupData() {
        return EMPTY_GROUP_DATA;
    }
}
