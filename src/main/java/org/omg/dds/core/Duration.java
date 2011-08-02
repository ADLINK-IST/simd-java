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

import org.omg.dds.core.AbstractTime;
import org.omg.dds.core.OverflowException;
import org.omg.dds.type.Extensibility;
import org.omg.dds.type.Nested;

import java.util.concurrent.TimeUnit;


/**
 * A span of elapsed time expressed with nanosecond precision.
 */
@Extensibility(Extensibility.Kind.FINAL_EXTENSIBILITY)
@Nested

public class Duration extends AbstractTime
{

    // -----------------------------------------------------------------------
    // Private Constants
    // -----------------------------------------------------------------------

    private static final Duration ZERO  = new Duration(0,0);
    // Create Infinite value and work around preconditions.
    private static final Duration INFINITE = new InfiniteDuration();
    private static final long serialVersionUID = 6926514364942353575L;

    // -----------------------------------------------------------------------
    // Factory methods
    // -----------------------------------------------------------------------

    private Duration() { super(); }

    // This class is used to represent the infinite time w/o
    // violating the invariants of the class Time.
    private static final class InfiniteDuration extends Duration {
        InfiniteDuration() {
            super(AbstractTime.SEC_MAX, 0xffffffff);
        }
    }

    public Duration(long d, TimeUnit unit) {
        super(d, unit);
    }

    public Duration(int sec, long nanoSec) {
        super(sec, nanoSec);
    }

    // -----------------------------------------------------------------------
    // Proper methods
    // -----------------------------------------------------------------------



    /**
     * multiply <code>Duration</code> instances by a constant.
     *
     * @param  c the constant that will be multiplied  to this <code>Duration</code>.
     * @return new <code>Duration</code> as result of the multiplication
     */

    public Duration multiply(long c) {
        assert (c >= 0);

        long s = c * this.sec;
        long ns = c * this.nanoSec;

        ns %= AbstractTime.NSEC_MAX;
        s += (ns/AbstractTime.NSEC_MAX);
        // This is safe due to the valid ranges for the Duration.
        return new Duration((int)s, (int)ns);
    }
    /**
     * devide two <code>Duration</code> instances.
     *
     * @param that the <code>Duration</code> instance that will
     *              divide this <code>Duration</code>.
     * @return  a constant as result of the division
     */
    public float divide (Duration that) {
        long a = this.sec * AbstractTime.NSEC_MAX + this.nanoSec;
        long b = that.sec * AbstractTime.NSEC_MAX + that.nanoSec;
        return ((float)a)/b;
    }

    /**
     * devide this <code>Duration</code> instances by a constant.
     *
     * @param c the constant that will be used to divide this <code>Duration</code>.
     * @return new duration as result of the division
     */
    public Duration divide (int c) {
        assert ( c > 0);
        return new Duration(this.sec/c, this.nanoSec/c);

    }



    public static Duration infinite() {
        return INFINITE;
    }

    public static Duration zero(){
        return ZERO;
    }

    /**
     * Adds two <code>Duration</code> instances.
     *
     * @param that the <code>Duration</code> instance that will be
     *              added to this <code>Duration</code>.
     * @return new <code>Duration</code> as result of the summation
     */
    public Duration add(Duration that) {
        return new Duration(this.sec + that.sec, this.nanoSec + that.nanoSec);
    }

    /**
     * Subtracts two <code>Duration</code> instances.
     *
     * @param that the <code>Duration</code> instance that will be
     *              subtracted to this <code>Duration</code>.
     * @return new <code>Duration</code> as result of the subtraction
     */
    public Duration subtract(Duration that) {

        assert (this.compareTo(that) >= 0 );

        long a = this.sec*AbstractTime.NSEC_MAX + this.nanoSec;
        long b = that.sec*AbstractTime.NSEC_MAX + that.nanoSec;

        long sub = a - b;
        return new Duration(sub, TimeUnit.NANOSECONDS);
    }

    /**
     * Report whether this duration lasts no time at all. The result of this
     * method is equivalent to the following:
     * <code>this.getDuration(TimeUnit.NANOSECONDS) == 0;</code>
     * @see     #getDuration(TimeUnit)
     */
    public boolean isZero() {
        return (this.compareTo(ZERO)==0);
    }

    /**
     * Report whether this duration lasts forever.
     * If this duration is infinite, the following relationship shall be
     * true:
     * <code>this.equals(infiniteDuration(this.getBootstrap()))</code>
     * @see     #infinite()
     * @return true if this is infinite
     */
    public boolean isInfinite() {
        // There is only one Infinite Object!
        return this == INFINITE;
    }


    @Override
    public String toString() {
        return this.sec + " sec "+ this.nanoSec + " nsec";
    }

}


