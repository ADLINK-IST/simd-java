package org.opensplice.psm.java.core.policy;

import org.omg.dds.core.policy.ResourceLimits;

public class OSPLResourceLimits implements ResourceLimits {

    /** The default serialVersionUID. */
    private static final long serialVersionUID = 1L;
    public static final int ID = 106;
    private static final String NAME = "ResourceLimits";

    final private DDS.ResourceLimitsQosPolicy policy;

    public OSPLResourceLimits(DDS.ResourceLimitsQosPolicy policy) {
        this.policy = policy;
    }

    @Override
    public int getPolicyId() {
        return ID;
    }

    @Override
    public String getPolicyName() {
        return NAME;
    }

    @Override
    public int getMaxSamples() {
        return policy.max_samples;
    }

    @Override
    public int getMaxInstances() {
        return policy.max_instances;
    }

    @Override
    public int getMaxSamplesPerInstance() {
        return policy.max_samples_per_instance;
    }
}
