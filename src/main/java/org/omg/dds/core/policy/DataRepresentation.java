package org.omg.dds.core.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataRepresentation implements QosPolicy {
    private static final long serialVersionUID = 5725828041265152040L;
	public static final int ID = 19;
    private static final String NAME = "DataRepresentation";
    private final List<Short> value;

    // -----------------------------------------------------------------------
    // Properties
    // -----------------------------------------------------------------------

    public List<Short> getValue() {
        return this.value;
    }

    public DataRepresentation(List<Short> value) {
        assert (value != null);
        this.value = new ArrayList<Short>();
        Collections.copy(value, this.value);

    }

    // -----------------------------------------------------------------------
    // Types
    // -----------------------------------------------------------------------

    public static final class Id {
        public static final short XCDR_DATA_REPRESENTATION = 0;
        public static final short XML_DATA_REPRESENTATION = 1;
    }

    public int getPolicyId() {
        return ID;
    }

    public String getPolicyName() {
        return NAME;
    }
}
