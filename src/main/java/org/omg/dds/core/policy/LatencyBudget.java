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

import org.omg.dds.core.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Specifies the maximum acceptable delay from the time the data is written
 * until the data is inserted in the receiver's application cache and the
 * receiving application is notified of the fact. This policy is a hint to the
 * Service, not something that must be monitored or enforced. The Service is not
 * required to track or alert the user of any violation. The default value of
 * the duration is zero indicating that the delay should be minimized.
 * 
 * <b>Concerns:</b> {@link org.omg.dds.topic.Topic}, {@link org.omg.dds.sub.DataReader}, {@link org.omg.dds.pub.DataWriter}
 * 
 * <b>RxO:</b> Yes
 * 
 * <b>Changeable:</b> Yes
 * 
 * This policy provides a means for the application to indicate to the
 * middleware the "urgency" of the data communication. By having a non-zero
 * duration the Service can optimize its internal operation.
 * 
 * This policy is considered a hint. There is no specified mechanism as to how
 * the service should take advantage of this hint.
 * 
 * The value offered is considered compatible with the value requested if and
 * only if the inequality "offered duration <= requested duration" evaluates to
 * true.
 */
public class LatencyBudget implements QosPolicy {

	private static final long serialVersionUID = 2966959985048968650L;
	
	public static final int ID = 6;
    private static final String NAME = "LatencyBudget";
    final private Duration duration;

    public LatencyBudget(long d, TimeUnit unit) {
        this.duration = new Duration(d, unit);
    }
    public LatencyBudget(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public int getPolicyId() {
        return ID;
    }

    public String getPolicyName() {
        return NAME;
    }

}
