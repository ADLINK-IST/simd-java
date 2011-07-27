package org.opensplice.psm.java.sub;

import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.Time;
import org.omg.dds.sub.InstanceState;
import org.omg.dds.sub.Sample;
import org.omg.dds.sub.SampleState;
import org.omg.dds.sub.ViewState;
import org.opensplice.psm.java.core.OSPLInstanceHandle;
import org.opensplice.psm.java.core.policy.OSPL;

import DDS.NEW_VIEW_STATE;
import DDS.READ_SAMPLE_STATE;

public class OSPLSample<TYPE> implements Sample<TYPE> {

    private final TYPE value;
    private final DDS.SampleInfo sampleInfo;

    public OSPLSample(DDS.SampleInfo sampleInfo, TYPE theValue) {
        value = theValue;
        this.sampleInfo = sampleInfo;
        
    }

     @Override
    public TYPE getData() {
        return value;
    }

    @Override
    public SampleState getSampleState() {
        SampleState sampleState;
        if (sampleInfo.sample_state == READ_SAMPLE_STATE.value) {
            sampleState = SampleState.READ;
        } else {
            sampleState = SampleState.NOT_READ;
        }
        return sampleState;
    }

    @Override
    public ViewState getViewState() {
        ViewState viewState;
        if (sampleInfo.view_state == NEW_VIEW_STATE.value) {
            viewState = ViewState.NEW;
        } else {
            viewState = ViewState.NOT_NEW;
        }
        return viewState;
    }

    @Override
    public InstanceState getInstanceState() {
        InstanceState instanceState;
        switch (sampleInfo.instance_state) {
        case DDS.ALIVE_INSTANCE_STATE.value:
            instanceState = InstanceState.ALIVE;
            break;
        case DDS.NOT_ALIVE_INSTANCE_STATE.value:
            instanceState = InstanceState.NOT_ALIVE_DISPOSED;
            break;
        case DDS.NOT_ALIVE_NO_WRITERS_INSTANCE_STATE.value:
            instanceState = InstanceState.NOT_ALIVE_NO_WRITERS;
            break;
        default:
            instanceState = InstanceState.ALIVE;

        }
        return instanceState;
    }

    @Override
    public Time getSourceTimestamp() {
        return OSPL.convert(sampleInfo.source_timestamp);
    }

    @Override
    public InstanceHandle getInstanceHandle() {
        return new OSPLInstanceHandle(sampleInfo.instance_handle);
    }

    @Override
    public InstanceHandle getPublicationHandle() {
        return new OSPLInstanceHandle(sampleInfo.publication_handle);
    }

    @Override
    public int getDisposedGenerationCount() {
        return sampleInfo.disposed_generation_count;
    }

    @Override
    public int getNoWritersGenerationCount() {
        return sampleInfo.no_writers_generation_count;
    }

    @Override
    public int getSampleRank() {
        return sampleInfo.sample_rank;
    }

    @Override
    public int getGenerationRank() {
        return sampleInfo.generation_rank;
    }

    @Override
    public int getAbsoluteGenerationRank() {
        return sampleInfo.absolute_generation_rank;
    }

    public OSPLSample<TYPE> clone() {
        return new OSPLSample<TYPE>(
        		sampleInfo, value);
    }

    public static class SampleIterator<TYPE> implements Iterator<TYPE> {

        final private OSPLDataReader<TYPE> reader;
        private int index = 0;
        final private DDS.SampleInfoSeqHolder sampleInfoList;
        final private Object sampleDataList;
        final private TYPE[] list;

        public SampleIterator(OSPLDataReader<TYPE> theReader,
                Object dataList,
                TYPE[] thelist, DDS.SampleInfoSeqHolder theSampleInfoList) {
            reader = theReader;
            list = thelist;
            sampleDataList = dataList;
            sampleInfoList = theSampleInfoList;
        }

        @Override
        public boolean hasNext() {
            return list.length > index;
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public Sample<TYPE> next() {
            Sample<TYPE> sample = new OSPLSample<TYPE>(
                    sampleInfoList.value[index], list[index]);
            index++;
            return sample;
        }

        @Override
        public int nextIndex() {
            return ++index;
        }

        @Override
        public Sample<TYPE> previous() {
            index--;
            Sample<TYPE> sample = new OSPLSample<TYPE>(
                    sampleInfoList.value[index], list[index]);
            return sample;
        }

        @Override
        public int previousIndex() {
            return --index;
        }

        @Override
        public void returnLoan() {
            reader.returnLoan(sampleDataList, sampleInfoList);
        }

        @Override
        public void remove() {
            throw new RuntimeException("remove sample not supported");
        }

        @Override
        public void set(Sample<TYPE> o) {
            throw new RuntimeException("Set sample not supported");
        }

        @Override
        public void add(Sample<TYPE> o) {
            throw new RuntimeException("Add sample not supported");
        }

    }
}
