package org.omg.dds.runtime;

import org.opensplice.psm.java.OSPLRuntime;
/**
 *  This class is provided as an example, but in general this
 *  will be provided by the vendor JAR. The purpose of this class
 *  is to bind the API with a specific DDS vendor implementation.
 */
public class Bootstrap {
    private static DDSRuntime theRuntime = null;

    public static synchronized DDSRuntime runtime() {
        if (theRuntime == null) {
            theRuntime = new OSPLRuntime();
        }
        return theRuntime;
    }
}
