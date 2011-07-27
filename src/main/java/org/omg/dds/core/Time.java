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

package org.omg.dds.core;

import java.util.concurrent.TimeUnit;

import org.omg.dds.type.Extensibility;
import org.omg.dds.type.Nested;


/**
 * A moment in time expressed with nanosecond precision (though not
 * necessarily nanosecond accuracy).
 */
@Extensibility(Extensibility.Kind.FINAL_EXTENSIBILITY)
@Nested
public class Time implements Value, Comparable<Time>
{
    // -----------------------------------------------------------------------
    // Private Constants
    // -----------------------------------------------------------------------

    private static final long serialVersionUID = -132361141453190372L;

    private static final Time INVALID = new Time(0x7fffffffffffffffL,
            0x7fffffffffffffffL);
    private static long NANOSECONDSPERSECOND = 1000000000L;

    /**
     * TODO verify that this should be two longs, as opposed to two int's. A
     * java long is 64 bit whereas a c/c++ long is likely 32 bit on a 32 bit
     * machine
     */
    private long sec;
    private long nanoSec;


    // -----------------------------------------------------------------------
    // Factory Methods
    // -----------------------------------------------------------------------

    /**
     * Construct a specific instant in time.
     * 
     * Negative values are considered invalid and will result in the
     * construction of a time <code>t</code> such that:
     * 
     * <code>t.isValid() == false</code>
     * 
     * @param bootstrap Identifies the Service instance to which the new
     *                  object will belong.
     * 
     * @see     #isValid()
     */
    /*
    public static ModifiableTime newTime(
            long time, TimeUnit units, Bootstrap bootstrap) {
        return bootstrap.getSPI().newTime(time, units);
    }
      */

    /**
     * @return      An unmodifiable {@link Time} that is not valid.
     */
    public static Time invalidTime() {
        return INVALID;
    }


    public Time(long sec) {
        this.sec = sec;
        this.nanoSec = 0;
    }

    public Time(long time, TimeUnit unit) {
    	long timeInNanoSec = unit.convert(time, TimeUnit.NANOSECONDS);
    	this.sec = timeInNanoSec / NANOSECONDSPERSECOND;
        this.nanoSec = timeInNanoSec % NANOSECONDSPERSECOND;
    }

    public Time(long sec, long nanoSec) {
        this.sec = sec;
        this.nanoSec = nanoSec;
    }


    // -----------------------------------------------------------------------
    // Instance Methods
    // -----------------------------------------------------------------------

    // --- Data access: ------------------------------------------------------

    /**
     * Truncate this time to a whole-number quantity of the given time
     * unit. For example, if this time is equal to one second plus 100
     * nanoseconds since the start of the epoch, calling this method with an
     * argument of {@link TimeUnit#SECONDS} will result in the value
     * <code>1</code>.
     * 
     * If this time is invalid, this method shall return
     * a negative value, regardless of the units given.
     * 
     * If this time cannot be expressed in the given units without
     * overflowing, this method shall return {@link Long#MAX_VALUE}. In such
     * a case, the caller may wish to use this method in combination with
     * {@link #getRemainder(TimeUnit, TimeUnit)} to obtain the full time
     * without lack of precision.
     * 
     * @param   inThisUnit  The time unit in which the return result will
     *                      be measured.
     * 
     * @see     #getRemainder(TimeUnit, TimeUnit)
     * @see     Long#MAX_VALUE
     * @see     TimeUnit
     */
    public long getTime(TimeUnit inThisUnit) {
        if (inThisUnit.equals(TimeUnit.SECONDS)) {
            return sec;
        }
        return inThisUnit.convert(nanoSec, TimeUnit.NANOSECONDS) +
                inThisUnit.convert(sec, TimeUnit.SECONDS);
    }

    /**
     * If getting the magnitude of this time in the given
     * <code>primaryUnit</code> would cause truncation with respect to the
     * given <code>remainderUnit</code>, return the magnitude of the
     * truncation in the latter (presumably finer-grained) unit. For example,
     * if this time is equal to one second plus 100 nanoseconds since the
     * start of the epoch, calling this method with arguments of
     * {@link TimeUnit#SECONDS} and {@link TimeUnit#NANOSECONDS} respectively
     * will result in the value <code>100</code>.
     * 
     * This method is equivalent to the following pseudo-code:
     * 
     * <code>(this - getTime(primaryUnit)).getTime(remainderUnit)</code>
     * 
     * If <code>remainderUnit</code> is represents a coarser granularity than
     * <code>primaryUnit</code> (for example, the former is
     * {@link TimeUnit#HOURS} but the latter is {@link TimeUnit#SECONDS}),
     * this method shall return <code>0</code>.
     * 
     * If the resulting time cannot be expressed in the given units
     * without overflowing, this method shall return {@link Long#MAX_VALUE}.
     * 
     * @param   primaryUnit
     * @param   remainderUnit   The time unit in which the return result will
     *                          be measured.
     * 
     * @see     #getTime(TimeUnit)
     * @see     Long#MAX_VALUE
     * @see     TimeUnit
     */
    public long getRemainder(
            TimeUnit primaryUnit, TimeUnit remainderUnit) {
        // TODO This will require some unit testing to confirm
        long valueInRemainderUnit =
                remainderUnit.convert(sec, TimeUnit.SECONDS) +
                        remainderUnit.convert(nanoSec, TimeUnit.NANOSECONDS);
        long valueInPrimaryUnit = primaryUnit.convert(valueInRemainderUnit,
                remainderUnit);
        long truncatedValueInRemainderUnit =
                remainderUnit.convert(valueInPrimaryUnit, primaryUnit);
        return valueInRemainderUnit - truncatedValueInRemainderUnit;
    }


    // --- Query: ------------------------------------------------------------

    /**
     * @return  whether this time represents a meaningful instant in time.
     */
    public boolean isValid() {
    	return sec >= 0 && nanoSec >= 0 && !INVALID.equals(this);
    }


    // --- From Object: ------------------------------------------------------

    @Override
    public Time clone() {
    	return new Time(sec, nanoSec);
    }

	@Override
	public int compareTo(Time that) {
        if (sec != that.sec) {
            return sec < that.sec ? -1 : 1;
        }
        if (nanoSec == that.nanoSec) {
            return 0;
        }
        return nanoSec < that.nanoSec ? -1 : 1;
	}
}
