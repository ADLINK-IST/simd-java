package org.opensplice.psm.java.core;

import org.omg.dds.core.InstanceHandle;

public class OSPLInstanceHandle extends InstanceHandle {
    /** default serialVersionUID */
    private static final long serialVersionUID = 1L;

    private long handle = 0L;

    public OSPLInstanceHandle(long thehandle) {
        handle = thehandle;
    }

    public long getHandle() {
        return handle;
    }

    @Override
    public boolean isNil() {
        return handle == 0L;
    }

    @Override
    public InstanceHandle clone() {
        return new OSPLInstanceHandle(handle);
    }
}
