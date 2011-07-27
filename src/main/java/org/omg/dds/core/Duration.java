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
 * A span of elapsed time expressed with nanosecond precision.
 */
@Extensibility(Extensibility.Kind.FINAL_EXTENSIBILITY)
@Nested
public class Duration implements Value, Comparable<Duration> {
    private static final Duration INFINITE = new Duration(0x7fffffffffffffffL,
            0x7fffffffffffffffL);
    private static final Duration ZERO = new Duration(0, 0);
    private static long NANOSECONDSPERSECOND = 1000000000L;

    /**
     * TODO verify that this should be two longs, as opposed to two int's. A
     * java long is 64 bit whereas a c/c++ long is likely 32 bit on a 32 bit
     * machine
     */
    private long sec;
    private long nanoSec;

    public Duration(long sec) {
        this.sec = sec;
        this.nanoSec = 0;
    }

    public Duration(long sec, long nanoSec) {
        this.sec = sec;
        this.nanoSec = nanoSec;
    }

    public Duration(long time, TimeUnit unit) {
    	long timeInNanoSec = unit.convert(time, TimeUnit.NANOSECONDS);
    	this.sec = timeInNanoSec / NANOSECONDSPERSECOND;
        this.nanoSec = timeInNanoSec % NANOSECONDSPERSECOND;
    }

    /**
     * Compares two <code>Duration</code> instances.
     * 
     * @param that
     *            the <code>Duration</code> instance that will be compared with
     *            this <code>Duration</code>.
     * @return (1) a negative value if (this < that), (2) zero if (this ==
     *         that), and (3) a positive value if (this > that)
     */
    public int compareTo(Duration that) {
        if (sec != that.sec) {
            return sec < that.sec ? -1 : 1;
        }
        if (nanoSec == that.nanoSec) {
            return 0;
        }
        return nanoSec < that.nanoSec ? -1 : 1;
    }

    /**
     * 
     * @param that
     * @return
     */
    public Duration add(Duration that) {
        long sec = this.sec + that.sec;
        long nanoSec = this.nanoSec + that.nanoSec;
        if (nanoSec >= NANOSECONDSPERSECOND) {
            nanoSec -= NANOSECONDSPERSECOND;
            sec--;
        } else if (nanoSec < 0) {
            nanoSec += NANOSECONDSPERSECOND;
            sec++;
        }
        return new Duration(sec, nanoSec);
    }

    // -----------------------------------------------------------------------
    // Private Constants
    // -----------------------------------------------------------------------

    private static final long serialVersionUID = 6926514364942353575L;

    // -----------------------------------------------------------------------
    // Factory Methods
    // -----------------------------------------------------------------------

    /**
     * 
     * @return An unmodifiable {@link Duration} of infinite length.
     */
    public static Duration infinite() {
        return INFINITE;
    }

    /**
     * 
     * @return A {@link Duration} of zero length.
     */
    public static Duration zero() {
        return ZERO;
    }

    // -----------------------------------------------------------------------
    // Instance Methods
    // -----------------------------------------------------------------------

    // --- Data access: ------------------------------------------------------

    /**
     * Truncate this duration to a whole-number quantity of the given time unit.
     * For example, if this duration is equal to one second plus 100
     * nanoseconds, calling this method with an argument of
     * {@link TimeUnit#SECONDS} will result in the value <code>1</code>.
     * 
     * If this duration is infinite, this method shall return
     * {@link Long#MAX_VALUE}, regardless of the units given.
     * 
     * If this duration cannot be expressed in the given units without
     * overflowing, this method shall return {@link Long#MAX_VALUE}. In such a
     * case, the caller may wish to use this method in combination with
     * {@link #getRemainder(TimeUnit, TimeUnit)} to obtain the full duration
     * without lack of precision.
     * 
     * @param inThisUnit
     *            The time unit in which the return result will be measured.
     * 
     * @see #getRemainder(TimeUnit, TimeUnit)
     * @see Long#MAX_VALUE
     * @see TimeUnit
     */
    public long getDuration(TimeUnit inThisUnit) {
        if (inThisUnit.equals(TimeUnit.SECONDS)) {
            return sec;
        }
        return inThisUnit.convert(nanoSec, TimeUnit.NANOSECONDS) +
                inThisUnit.convert(sec, TimeUnit.SECONDS);
    }

    /**
     * If getting the magnitude of this duration in the given
     * <code>primaryUnit</code> would cause truncation with respect to the given
     * <code>remainderUnit</code>, return the magnitude of the truncation in the
     * latter (presumably finer-grained) unit. For example, if this duration is
     * equal to one second plus 100 nanoseconds, calling this method with
     * arguments of {@link TimeUnit#SECONDS} and {@link TimeUnit#NANOSECONDS}
     * respectively will result in the value <code>100</code>.
     * 
     * This method is equivalent to the following pseudo-code:
     * 
     * <code>
     * (this - getDuration(primaryUnit)).getDuration(remainderUnit)
     * </code>
     * 
     * If <code>remainderUnit</code> is represents a coarser granularity than
     * <code>primaryUnit</code> (for example, the former is
     * {@link TimeUnit#HOURS} but the latter is {@link TimeUnit#SECONDS}), this
     * method shall return <code>0</code>.
     * 
     * If the resulting duration cannot be expressed in the given units without
     * overflowing, this method shall return {@link Long#MAX_VALUE}.
     * 
     * @param primaryUnit
     * @param remainderUnit
     *            The time unit in which the return result will be measured.
     * 
     * @see #getDuration(TimeUnit)
     * @see Long#MAX_VALUE
     * @see TimeUnit
     */
    public long getRemainder(TimeUnit primaryUnit, TimeUnit remainderUnit) {
        if (TimeUnit.SECONDS.equals(primaryUnit)
                && TimeUnit.NANOSECONDS.equals(remainderUnit)) {
            return nanoSec;
        }
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
     * Report whether this duration lasts no time at all. The result of this
     * method is equivalent to the following:
     * 
     * <code>this.getDuration(TimeUnit.NANOSECONDS) == 0;</code>
     * 
     * @see #getDuration(TimeUnit)
     */
    public boolean isZero() {
        return equals(ZERO);
    }

    /**
     * Report whether this duration lasts forever.
     * 
     * If this duration is infinite, the following relationship shall be true:
     * 
     * <code>this.equals(infiniteDuration(this.getBootstrap()))</code>
     * 
     * @see #infinite()
     */
    public boolean isInfinite() {
        return equals(INFINITE);
    }

    // --- From Object: ------------------------------------------------------

    @Override
    public Duration clone() {
        return new Duration(this.sec, this.nanoSec);
    }
}
