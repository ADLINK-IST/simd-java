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
 * Controls the criteria used to determine the logical order among changes
 * made by {@link org.omg.dds.pub.Publisher} entities to the same instance of data (i.e.,
 * matching Topic and key). The default kind is
 * {@link DestinationOrder.Kind#BY_RECEPTION_TIMESTAMP}.
 * 
 * <b>Concerns:</b> {@link org.omg.dds.topic.Topic}, {@link org.omg.dds.sub.DataReader}, {@link org.omg.dds.pub.DataWriter}
 * 
 * <b>RxO:</b> Yes
 * 
 * <b>Changeable:</b> No
 * 
 * This policy controls how each subscriber resolves the final value of a
 * data instance that is written by multiple DataWriter objects (which may be
 * associated with different Publisher objects) running on different nodes.
 * 
 * The setting {@link Kind#BY_RECEPTION_TIMESTAMP} indicates that, assuming
 * the {@link Ownership} allows it, the latest received value for
 * the instance should be the one whose value is kept. This is the default
 * value.
 * 
 * The setting {@link Kind#BY_SOURCE_TIMESTAMP} indicates that, assuming the
 * {@link Ownership} allows it, a time stamp placed at the source
 * should be used. This is the only setting that, in the case of concurrent
 * same-strength DataWriter objects updating the same instance, ensures all
 * subscribers will end up with the same final value for the instance. The
 * mechanism to set the source time stamp is middleware dependent.
 * 
 * The value offered is considered compatible with the value requested if and
 * only if the inequality "offered kind &gt;= requested kind" evaluates to
 * true. For the purposes of this inequality, the values of DESTINATION_ORDER
 * kind are considered ordered such that BY_RECEPTION_TIMESTAMP &lt;
 * BY_SOURCE_TIMESTAMP.
 * 
 * @see Ownership
 */
public class DestinationOrder implements QosPolicy, Comparable<DestinationOrder> {

	private static final long serialVersionUID = -5111030565286393445L;
	
	public static final int ID = 12;
    private static final String NAME = "DestinationOrder";

    private static final DestinationOrder RECEPTION_TIMESTAMP =
            new DestinationOrder(Kind.BY_RECEPTION_TIMESTAMP);

    private static final DestinationOrder SOURCE_TIMESTAMP =
            new DestinationOrder(Kind.BY_SOURCE_TIMESTAMP);

    private Kind kind;

     // -----------------------------------------------------------------------
    // Types
    // -----------------------------------------------------------------------

    public enum Kind {
        /**
         * Indicates that data is ordered based on the reception time at each
         * {@link org.omg.dds.sub.Subscriber}. Since each subscriber may receive the data at
         * different times there is no guaranteed that the changes will be
         * seen in the same order. Consequently, it is possible for each
         * subscriber to end up with a different final value for the data.
         */
        BY_RECEPTION_TIMESTAMP,

        /**
         * Indicates that data is ordered based on a time stamp placed at the
         * source (by the Service or by the application). In any case this
         * guarantees a consistent final value for the data in all
         * subscribers.
         */
        BY_SOURCE_TIMESTAMP
    }

    private DestinationOrder(Kind kind) {
        this.kind = kind;
    }
    // -----------------------------------------------------------------------
    // Methods
    // -----------------------------------------------------------------------

    /**
     * @return the kind
     */
    public Kind getKind() {
        return this.kind;
    }

    public static DestinationOrder ReceptionTimestamp() {
        return RECEPTION_TIMESTAMP;
    }

    public static DestinationOrder SourceTimestamp() {
        return SOURCE_TIMESTAMP;
    }

    public int getPolicyId() { return ID; }

    public String getPolicyName() { return NAME; }

    public int compareTo(DestinationOrder that) {
        return (this.kind.ordinal() - that.kind.ordinal());
    }


}
