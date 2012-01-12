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

import javax.print.DocFlavor;

/**
 * Specifies the resources that the Service can consume in order to meet the
 * requested QoS.
 * 
 * <b>Concerns:</b> {@link org.omg.dds.topic.Topic}, {@link org.omg.dds.sub.DataReader}, {@link org.omg.dds.pub.DataWriter}
 * 
 * <b>RxO:</b> No
 * 
 * <b>Changeable:</b> No
 * 
 * This policy controls the resources that the Service can use in order to meet
 * the requirements imposed by the application and other QoS settings.
 * 
 * If the DataWriter objects are communicating samples faster than they are
 * ultimately taken by the DataReader objects, the middleware will eventually
 * hit against some of the QoS-imposed resource limits. Note that this may occur
 * when just a single DataReader cannot keep up with its corresponding
 * DataWriter. The behavior in this case depends on the setting for the
 * {@link Reliability}. If reliability is {@link Reliability.Kind#BEST_EFFORT},
 * then the Service is allowed to drop samples. If the reliability is
 * {@link Reliability.Kind#RELIABLE}, the Service will block the DataWriter or
 * discard the sample at the DataReader in order not to lose existing samples
 * (so that the sample can be resent at a later time).
 * 
 * The constant {@link #LENGTH_UNLIMITED} may be used to indicate the absence of
 * a particular limit. For example setting maxSamplesPerInstance to
 * LENGH_UNLIMITED will cause the middleware to not enforce this particular
 * limit.
 * 
 * The setting of RESOURCE_LIMITS maxSamples must be consistent with the
 * maxSamplesPerInstance. For these two values to be consistent they must verify
 * that "max_samples &gt;= max_samples_per_instance."
 * 
 * The setting of RESOURCE_LIMITS maxSamplesPerInstance must be consistent with
 * the HISTORY depth. For these two QoS to be consistent, they must verify that
 * "depth &lt;= maxSamplesPerInstance."
 * 
 * An attempt to set this policy to inconsistent values when an entity is
 * created or via a <code>setQos</code> operation will cause the operation to
 * fail.
 * 
 * @see History
 * @see Reliability
 */
public class ResourceLimits implements QosPolicy {
    public static final int ID = 14;
    public static final String NAME = "ResourceLimits";

    private final int maxSamples;
    private final int maxInstances;
    private final int maxSamples4Instance;

    private final static ResourceLimits UNLIMITED = new ResourceLimits(-1, -1, -1);

    /**
     * Creates a Resource Limits QoS policy.
     *
     * @param ms Max Samples
     * @param mi Max Instances
     * @param ms4i Max Samples for Instance
     */

    public ResourceLimits(int ms, int mi, int ms4i) {
        this.maxSamples = ms;
        this.maxInstances = mi;
        this.maxSamples4Instance = ms4i;
    }

    /**
     * @return the maxSamples
     */
    public int getMaxSamples() {
        return this.maxSamples;
    }

    /**
     * @return the maxInstances
     */
    public int getMaxInstances() {
        return this.maxInstances;
    }

    /**
     * @return the maxSamplesPerInstance
     */
    public int getMaxSamplesPerInstance() {
        return this.maxSamples4Instance;
    }

    public int getPolicyId() {
        return ID;
    }

    public String getPolicyName() {
        return NAME;
    }

    public ResourceLimits Unlimited() {
        return ResourceLimits.UNLIMITED;
    }

    @Override
    public boolean equals(Object that) {
        boolean r = false;
        if (this == that)
            r =  true;
        else if (that instanceof ResourceLimits) {
            ResourceLimits rl = (ResourceLimits)that;
            r = (rl.maxInstances == this.maxInstances)
                    && (rl.maxSamples == this.maxSamples)
                    && (rl.maxSamples4Instance == this.maxSamples4Instance);
        }
        return r;
    }
}
