package org.opensplice.psm.java.core;

import org.omg.dds.core.Duration;

public class TypeConverter {
    public static DDS.Duration_t convert(Duration d) {
        DDS.Duration_t r =
                (d == null)
                ? null
                : new DDS.Duration_t(d.getSec(), (int)d.getNanoSec());
        return r;
    }

    public static Duration convert(DDS.Duration_t d) {
        Duration r =
                (d == null)
                ? null
                : new Duration(d.sec, d.nanosec);
        return r;
    }


}
