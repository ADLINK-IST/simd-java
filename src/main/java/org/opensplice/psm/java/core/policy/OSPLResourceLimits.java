package org.opensplice.psm.java.core.policy;

import org.omg.dds.core.policy.ResourceLimits;

//TODO: This class should removed from here and
// should be implemented on the org.dds.omg.core.policy package
public class OSPLResourceLimits implements ResourceLimits {

    /** The default serialVersionUID. */
    private static final long serialVersionUID = 1L;
    public static final int ID = 106;
    private static final String NAME = "ResourceLimits";

    final private DDS.ResourceLimitsQosPolicy policy;

    public OSPLResourceLimits(DDS.ResourceLimitsQosPolicy policy) {
        this.policy = policy;
    }

    
    public int getPolicyId() {
        return ID;
    }

    
    public String getPolicyName() {
        return NAME;
    }

    
    public int getMaxSamples() {
        return policy.max_samples;
    }

    
    public int getMaxInstances() {
        return policy.max_instances;
    }

    
    public int getMaxSamplesPerInstance() {
        return policy.max_samples_per_instance;
    }
}
