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

    private static final Duration ZERO  = new Duration(0, 0);
    private static final Duration INFINITE = createInfinite();
    private static final long serialVersionUID = 6926514364942353575L;

    // -----------------------------------------------------------------------
    // Factory methods
    // -----------------------------------------------------------------------


    public Duration(long sec) {
        super(sec);
    }

    public Duration(long sec, long nanoSec) {
        super(sec, nanoSec);
    }


    private static Duration createInfinite() {
        Duration inf = ZERO ;
        inf.sec = Long.MAX_VALUE ;
        inf.nanoSec = Long.MAX_VALUE ;
        return inf ;
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

    public Duration multiply (long c) throws OverflowException {

        assert (c >= 0);

        Duration result =  ZERO;
        double multiplication = (this.sec * Math.pow(10, 9) * c) + (double) (this.nanoSec * c);

        //  check overflow for seconds multiplication
        if ( multiplication /Math.pow(10,9) >= Long.MAX_VALUE)  throw new OverflowException();
        else
            result.sec = (long)(multiplication / Math.pow(10,9))  ;

        result.nanoSec = (long) (multiplication - (result.sec * Math.pow(10, 9)));

        return result;
    }
    /**
     * devide two <code>Duration</code> instances.
     *
     * @param that the <code>Duration</code> instance that will
     *              divide this <code>Duration</code>.
     * @return  a constant as result of the division
     */
    public double divide (Duration that) {

        double d_this = this.sec * Math.pow(10,9) + this.nanoSec ;
        double d_that = that.sec * Math.pow(10,9) + that.nanoSec ;
        return d_this/d_that;

    }

    /**
     * devide this <code>Duration</code> instances by a constant.
     *
     * @param c the constant that will be used to divide this <code>Duration</code>.
     * @return new duration as result of the division
     */
    public Duration divide (long c) {
       assert ( c > 0);

        Duration result = (Duration) ZERO;

        double division = ((double)this.sec * Math.pow(10,9) +(double)( this.nanoSec)) /c ;

        result.sec = (long) (division / Math.pow(10,9));
        result.nanoSec = (long) (division - result.sec*Math.pow(10,9));


        return result ;

    }



    public static Duration infinite(){
        return INFINITE;
    }

    public static Duration zero(){
        return ZERO;
    }
}


