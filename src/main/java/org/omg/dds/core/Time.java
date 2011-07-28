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

import org.omg.dds.core.AbstractTime;
import org.omg.dds.type.Extensibility;
import org.omg.dds.type.Nested;



/**
 * A moment in time expressed with nanosecond precision (though not
 * necessarily nanosecond accuracy).
 */
@Extensibility(Extensibility.Kind.FINAL_EXTENSIBILITY)
@Nested

public  class Time extends AbstractTime
{
    private static final Time ZERO = new Time(0,0);
    private static final Time INFINITE = createInfinite();

    private static Time createInfinite() {
        Time inf = ZERO ;
        inf.sec = Long.MAX_VALUE ;
        inf.nanoSec = Long.MAX_VALUE ;
        return inf ;
    }

    private static final long serialVersionUID = -132361141453190372L;


    public Time(long sec, long nanoSec){

        assert ( sec >= 0  && Math.pow(10,9)> nanoSec );
        this.sec = sec;
        this.nanoSec = nanoSec;

    }


    /**
     * Adds a <code>Duration</code> instance to this <code>time</code>  .
     *
     * @param d : the <code>Duration</code> instance that will be
     *              added to this <code>Time</code>.
     * @return new <code>Time</code> as result of the summation
     */
    public Time add (Duration d) throws OverflowException {
        return (Time) super.add(d);
    }

    /**
     * Subtracts two <code>Duration</code> instances.
     *
     * @param that the <code>Duration</code> instance that will be
     *              added to this <code>Duration</code>.
     * @return new <code>Duration</code> as result of the summation
     */

    public Time subtract(Duration that) {

        return (Time) super.subtract(that);

    }


    /**
     * Truncate this time to a whole-number quantity of the given time
     * unit. For example, if this time is equal to one second plus 100
     * nanoseconds since the start of the epoch, calling this method with an
     * argument of {@link TimeUnit#SECONDS} will result in the value
     * <code>1</code>.
     * <p/>
     * If this time is invalid, this method shall return
     * a negative value, regardless of the units given.
     * <p/>
     * If this time cannot be expressed in the given units without
     * overflowing, this method shall return {@link Long#MAX_VALUE}. In such
     * a case, the caller may wish to use this method in combination with
     * {@link #getRemainder(TimeUnit, TimeUnit)} to obtain the full time
     * without lack of precision.
     *
     * @param inThisUnit The time unit in which the return result will
     *                   be measured.
     * @see #getRemainder(TimeUnit, TimeUnit)
     * @see Long#MAX_VALUE
     * @see TimeUnit
     */
    public long getTime(TimeUnit inThisUnit) {
        return super.getDuration(inThisUnit);
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
     * <p/>
     * This method is equivalent to the following pseudo-code:
     * <p/>
     * <code>(this - getTime(primaryUnit)).getTime(remainderUnit)</code>
     * <p/>
     * If <code>remainderUnit</code> is represents a coarser granularity than
     * <code>primaryUnit</code> (for example, the former is
     * {@link TimeUnit#HOURS} but the latter is {@link TimeUnit#SECONDS}),
     * this method shall return <code>0</code>.
     * <p/>
     * If the resulting time cannot be expressed in the given units
     * without overflowing, this method shall return {@link Long#MAX_VALUE}.
     *
     * @param primaryUnit
     * @param remainderUnit The time unit in which the return result will
     *                      be measured.
     * @see #getTime(TimeUnit)
     * @see Long#MAX_VALUE
     * @see TimeUnit
     */

    public long getRemainder(TimeUnit primaryUnit, TimeUnit remainderUnit) {
        return super.getRemainder(primaryUnit,remainderUnit);
    }


    public boolean isValid() {
        return (this.nanoSec >= 0 && this.sec >= 0 && Math.pow(10,9)> this.nanoSec);
    }

    /**
     *
     * @return  A {@link AbstractTime} of zero length.
     */
    public static Time zero() {
        return ZERO;
    }

    public static AbstractTime infinite() {
        return INFINITE ;
    }

    public int compareTo(Time that) {
        return super.compareTo(that) ;
    }



}
