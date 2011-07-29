package org.omg.dds.core;

import org.omg.dds.core.Value;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;


public abstract class AbstractTime implements Value, Comparable<AbstractTime>, Serializable
{

    protected static final int SEC_MAX = 0x7fffffff;
    protected static final int NSEC_MAX = (int)Math.pow(10, 9);

    protected int sec = 0;
    protected int nanoSec = 0;


    public AbstractTime(long d, TimeUnit unit) {
        assert (d >= 0 );
        long sec = unit.convert(d, TimeUnit.SECONDS);
        long nsec = unit.convert(d, TimeUnit.NANOSECONDS) - (sec * NSEC_MAX);

        // If sec is negative that means we've gone out of
        // the allowable max time.
        if (this.sec < 0)
            throw new OverflowException("Time Out of Bounds");

        this.sec = (int)sec;
        this.nanoSec = (int)nsec;
    }

    public AbstractTime(int sec, int nanoSec) {
        assert (nanoSec >= 0 && sec >= 0);

        this.nanoSec = (nanoSec < NSEC_MAX) ?  nanoSec : (nanoSec % NSEC_MAX);
        this.sec = sec + this.nanoSec/NSEC_MAX;
        // If sec is negative that means we've gone out of
        // the allowable max time.
        if (this.sec < 0)
            throw new OverflowException("Time Out of Bounds");
    }

    public AbstractTime() { }

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
        long d = inThisUnit.convert(this.sec, TimeUnit.SECONDS)
                + inThisUnit.convert(this.nanoSec, TimeUnit.NANOSECONDS);
        return d;
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

        long pu = primaryUnit.convert(this.sec, TimeUnit.SECONDS)
                + primaryUnit.convert(this.nanoSec,TimeUnit.NANOSECONDS);

        long sec = TimeUnit.SECONDS.convert(pu, primaryUnit);
        long nsec =TimeUnit.NANOSECONDS.convert(pu, primaryUnit) - sec*NSEC_MAX;

        long remainder = this.nanoSec - nsec;

        return reminderUnit.convert(remainder, TimeUnit.NANOSECONDS);
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

    public int getSec() {
        return sec;
    }

    public int getNanoSec() {
        return nanoSec;
    }

}

