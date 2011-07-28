package org.omg.dds.core;


import org.omg.dds.core.Value;

import java.util.concurrent.TimeUnit;


public  class AbstractTime implements Value, Comparable<AbstractTime>
{

    protected static final AbstractTime INFINITE = infinite();
    protected static final AbstractTime ZERO  = new AbstractTime(0, 0);

    protected long sec;
    protected long nanoSec;


    public AbstractTime(long sec) {
        assert (sec >= 0 );
        this.sec = sec;
        this.nanoSec = 0;
    }
    public AbstractTime(long sec, long nanoSec) {
        assert (nanoSec >= 0 && sec >= 0 &&  (nanoSec <(long)Math.pow(10,9)));
        this.sec = sec;
        this.nanoSec = nanoSec;
    }

    public AbstractTime() {
    }

    /**
     * Truncate this duration to a whole-number quantity of the given time
     * unit. For example, if this duration is equal to one second plus 100
     * nanoseconds, calling this method with an argument of
     * {@link java.util.concurrent.TimeUnit#SECONDS} will result in the value <code>1</code>.
     *
     * If this duration is infinite, this method shall return
     * {@link Long#MAX_VALUE}, regardless of the units given.
     *
     *
     **/


    public long getDuration(TimeUnit inThisUnit) {

        AbstractTime result = new AbstractTime(Long.MAX_VALUE,0) ;

        if ( ! this.isInfinite() )
        {
            result.sec = inThisUnit.convert(this.sec,TimeUnit.SECONDS);
            result.nanoSec = inThisUnit.convert(this.nanoSec,TimeUnit.NANOSECONDS);
        }
        return result.sec + result.nanoSec ;
    }


    /**
     * If getting the magnitude of this duration in the given
     * <code>primaryUnit</code> would cause truncation with respect to the
     * given <code>remainderUnit</code>, return the magnitude of the
     * truncation in the latter (presumably finer-grained) unit. For example,
     * if this duration is equal to one second plus 100 nanoseconds, calling
     * this method with arguments of {@link TimeUnit#SECONDS} and
     * {@link TimeUnit#NANOSECONDS} respectively will result in the value
     * <code>100</code>.
     *
     * This method is equivalent to the following pseudo-code:
     *
     * <code>
     * (this.substract(getDuration(primaryUnit)).getDuration(remainderUnit)
     * </code>
     *
     * If <code>remainderUnit</code> is represents a coarser granularity than
     * <code>primaryUnit</code> (for example, the former is
     * {@link TimeUnit#HOURS} but the latter is {@link TimeUnit#SECONDS}),
     * this method shall return <code>0</code>.
     *
     * If the resulting duration cannot be expressed in the given units
     * without overflowing, this method shall return {@link Long#MAX_VALUE}.
     *
     * @param   primaryUnit
     * @see     Long#MAX_VALUE
     * @see     TimeUnit
     */
    // be sufficient to get the portion of this Duration that "fits" into the reminder unit.
    // Example:
    // this(10, 123456789).getReminder(NANOSEC) == 789
    //  this(10, 123456789).getReminder(MICROSEC) == 456789
    public long getRemainder( TimeUnit primaryUnit , TimeUnit reminderUnit ) {

        double sec_factor = reminderUnit.convert(1,TimeUnit.SECONDS);
        double nanosec_factor = reminderUnit.convert(1,TimeUnit.NANOSECONDS);

        double converted = (double)this.sec * sec_factor + (double)this.nanoSec * nanosec_factor;

        double factor =  reminderUnit.convert(1,primaryUnit) ;

        long remainder = (long) (converted - (this.getDuration(primaryUnit)* factor))     ;

        return remainder ;
    }

    /**
     * Adds two <code>Duration</code> instances.
     *
     * @param that the <code>Duration</code> instance that will be
     *              added to this <code>Duration</code>.
     * @return new <code>Duration</code> as result of the summation
     */
    public AbstractTime add(AbstractTime that) throws OverflowException {
        AbstractTime result =  ZERO;

        // overflow cannot occur for nanoseconds addition
        long nanoSec = this.nanoSec + that.nanoSec;

        // check overflow for seconds addition
        if ((double)this.sec + (double)that.sec + nanoSec/Math.pow(10,9) >= (double)Long.MAX_VALUE ) throw new OverflowException();
        else result.sec = this.sec + that.sec + (long)(nanoSec/Math.pow(10,9));

        result.nanoSec = (long) (nanoSec % Math.pow(10,9));

        return result ;


    }

    /**
     * Subtracts two <code>Duration</code> instances.
     *
     * @param that the <code>Duration</code> instance that will be
     *              subtracted to this <code>Duration</code>.
     * @return new <code>Duration</code> as result of the subtraction
     */
    public AbstractTime subtract(AbstractTime that) {

        assert (this.compareTo(that) >= 0 );

        AbstractTime result = ZERO ;

        result.sec = this.sec - that.sec;

        if (this.nanoSec >= that.nanoSec )
            result.nanoSec = this.nanoSec - that.nanoSec;
        else {
            result.nanoSec = (long)Math.pow(10,10)-(that.nanoSec - this.nanoSec) ;
            result.sec = this.sec - (that.sec+1) ;
        }

        return result;
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
     */


    public boolean isInfinite() {
        return (this.compareTo(INFINITE)==0);
    }


    // -----------------------------------------------------------------------
    // Factory Methods
    // -----------------------------------------------------------------------


    /**
     *
     * @return  An unmodifiable {@link AbstractTime} of infinite length.
     */
    public static AbstractTime infinite() {
       return INFINITE ;
    }



    public long getSec() {
        return sec;
    }

    public long getNanoSec() {
        return nanoSec;
    }

    public AbstractTime clone() {
        return new AbstractTime(this.sec, this.nanoSec);
    }




    public int compareTo(AbstractTime that) {
        int c = 0;

        if (this.sec < that.sec) c = -1 ;
        if (this.sec > that.sec) c = 1 ;

        if (this.sec == that.sec) {
            if (this.nanoSec < that.nanoSec) c = -1 ;
            if (this.nanoSec > that.nanoSec ) c =  1 ;
        }

        return c;
    }



}

