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

package org.omg.dds.core.policy;

import org.omg.dds.core.Duration;

import java.util.concurrent.TimeUnit;


/**
 * Specifies the behavior of the {@link org.omg.dds.sub.DataReader} with regards to the life
 * cycle of the data instances it manages.
 * 
 * <b>Concerns:</b> {@link org.omg.dds.sub.DataReader}
 * 
 * <b>RxO:</b> N/A
 * 
 * <b>Changeable:</b> Yes
 * 
 * This policy controls the behavior of the DataReader with regards to the
 * lifecycle of the data instances it manages, that is, the data instance
 * that have been received and for which the DataReader maintains some
 * internal resources.
 * 
 * The DataReader internally maintains the samples that have not been taken
 * by the application, subject to the constraints imposed by other QoS
 * policies such as {@link History} and
 * {@link ResourceLimits}.
 * 
 * The DataReader also maintains information regarding the identity,
 * {@link org.omg.dds.sub.ViewState} and {@link org.omg.dds.sub.InstanceState} of data instances even after
 * all samples have been 'taken.' This is needed to properly compute the
 * states when future samples arrive.
 * 
 * Under normal circumstances the DataReader can only reclaim all resources
 * for instances for which there are no writers and for which all samples
 * have been 'taken.' The last sample the DataReader will have taken for that
 * instance will have an instance state of either
 * {@link org.omg.dds.sub.InstanceState#NOT_ALIVE_NO_WRITERS} or
 * {@link org.omg.dds.sub.InstanceState#NOT_ALIVE_DISPOSED} depending on whether the last
 * writer that had ownership of the instance disposed it or not. In the
 * absence of the READER_DATA_LIFECYCLE QoS this behavior could cause
 * problems if the application "forgets" to 'take' those samples. The
 * 'untaken' samples will prevent the DataReader from reclaiming the
 * resources and they would remain in the DataReader indefinitely.
 * 
 * The autoPurgeNoWriterSamplesDelay defines the maximum duration for which
 * the DataReader will maintain information regarding an instance once its
 * instance state becomes NOT_ALIVE_NO_WRITERS. After this time elapses, the
 * DataReader will purge all internal information regarding the instance; any
 * untaken samples will also be lost.
 * 
 * The autoPurgeDisposedSamplesDelay defines the maximum duration for which
 * the DataReader will maintain samples for an instance once its instance
 * state becomes NOT_ALIVE_DISPOSED. After this time elapses, the DataReader
 * will purge all samples for the instance.
 * 
 * @see History
 * @see ResourceLimits
 */
public class ReaderDataLifecycle implements QosPolicy {
    private static final int ID = 17;
    private static final String NAME = "ReaderDataLifecycle";

    final Duration dwPurgeDelay;
    final Duration sPurgeDelay;

    public ReaderDataLifecycle(long writerPurgeDelay, long disposedSamplesPurgeDelay, TimeUnit unit) {
        this.dwPurgeDelay = new Duration(writerPurgeDelay, unit);
        this.sPurgeDelay = new Duration(disposedSamplesPurgeDelay, unit);
    }

    public ReaderDataLifecycle(Duration writerPurgeDelay, Duration disposedSamplesPurgeDelay) {
        this.dwPurgeDelay = writerPurgeDelay;
        this.sPurgeDelay = disposedSamplesPurgeDelay;
    }

    public Duration getAutoPurgeNoWriterSamplesDelay() {
        return this.dwPurgeDelay;
    }

    public Duration getAutoPurgeDisposedSamplesDelay() {
        return this.sPurgeDelay;
    }


    public int getPolicyId() {
        return ID;
    }

    public String getPolicyName() {
        return NAME;
    }

    /**
     * Implementing classes should override <code>equals()</code>.
     */
    public boolean equals(Object that) {
        boolean r = false;
        if (that == this)
            r = true;
        else if (that instanceof ReaderDataLifecycle) {
            ReaderDataLifecycle rdl = (ReaderDataLifecycle)that;
            r = this.dwPurgeDelay.equals(rdl.dwPurgeDelay)
                    && this.sPurgeDelay.equals(rdl.sPurgeDelay);

        }
        return r;
    }
}
