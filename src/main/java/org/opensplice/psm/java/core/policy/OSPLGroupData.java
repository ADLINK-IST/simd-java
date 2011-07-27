package org.opensplice.psm.java.core.policy;

import org.omg.dds.core.policy.GroupData;

public class OSPLGroupData implements GroupData {

    /** The default serialVersionUID. */
    private static final long serialVersionUID = 1L;
    public static final int ID = 103;
    private static final String NAME = "GroupData";
    final private DDS.GroupDataQosPolicy policy;

    public OSPLGroupData(DDS.GroupDataQosPolicy thepolicy) {
        policy = thepolicy;
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
    public int getValue(byte[] value, int offset) {
        // TODO what should be done here???
        return 0;
    }

    @Override
    public int getLength() {
        return policy.value.length;
    }

}
