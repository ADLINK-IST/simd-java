package org.omg.dds.runtime;

import org.opensplice.psm.java.OSPLRuntime;
/**
 *  This class is provided as an example, but in general this
 *  will be provided by the vendor JAR. The purpose of this class
 *  is to bind the API with a specific DDS vendor implementation.
 */
public class DDSBinder {
    public static DDSRuntime bind() {
        return new OSPLRuntime();
    }
}
