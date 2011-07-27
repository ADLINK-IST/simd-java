package org.opensplice.psm.java.core.policy;

import java.util.ArrayList;
import java.util.Collection;

import org.omg.dds.core.policy.Partition;

public class OSPLPartition implements
        Partition {

    /** The default serialVersionUID. */
    private static final long serialVersionUID = 1L;
    public static final int ID = 102;
    private static final String NAME = "Partition";

    private Collection<String> name = new ArrayList<String>();

    public String[] getPartition() {
        return (String[]) name.toArray();
    }

    public OSPLPartition() {
    }

    public OSPLPartition(String[] partition) {
        for (int i = 0; i < partition.length; i++) {
            name.add(partition[i]);
        }
    }

    
    public Collection<String> getName() {
        return name;
    }

    
    public int getPolicyId() {
        return ID;
    }

    
    public String getPolicyName() {
        return NAME;
    }

}
