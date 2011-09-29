package org.opensplice.psm.java.sub;

import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.Time;
import org.omg.dds.sub.InstanceState;
import org.omg.dds.sub.Sample;
import org.omg.dds.sub.SampleState;
import org.omg.dds.sub.ViewState;
import org.opensplice.psm.java.core.OSPLInstanceHandle;
import org.opensplice.psm.java.core.policy.PolicyConverter;

import DDS.NEW_VIEW_STATE;
import DDS.READ_SAMPLE_STATE;

public class OSPLSample<TYPE> implements Sample<TYPE> {

    private final TYPE value;
    private final DDS.SampleInfo sampleInfo;

    public OSPLSample(TYPE theValue, DDS.SampleInfo sampleInfo) {
        value = theValue;
        this.sampleInfo = sampleInfo;
        
    }

     
    public TYPE getData() {
        return value;
    }

    
    public SampleState getSampleState() {
        SampleState sampleState;
        if (sampleInfo.sample_state == READ_SAMPLE_STATE.value) {
            sampleState = SampleState.READ;
        } else {
            sampleState = SampleState.NOT_READ;
        }
        return sampleState;
    }

    
    public ViewState getViewState() {
        ViewState viewState;
        if (sampleInfo.view_state == NEW_VIEW_STATE.value) {
            viewState = ViewState.NEW;
        } else {
            viewState = ViewState.NOT_NEW;
        }
        return viewState;
    }

    
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

    
    public Time getSourceTimestamp() {
        return PolicyConverter.convert(sampleInfo.source_timestamp);
    }

    
    public InstanceHandle getInstanceHandle() {
        return new OSPLInstanceHandle(sampleInfo.instance_handle);
    }

    
    public InstanceHandle getPublicationHandle() {
        return new OSPLInstanceHandle(sampleInfo.publication_handle);
    }

    
    public int getDisposedGenerationCount() {
        return sampleInfo.disposed_generation_count;
    }

    
    public int getNoWritersGenerationCount() {
        return sampleInfo.no_writers_generation_count;
    }

    
    public int getSampleRank() {
        return sampleInfo.sample_rank;
    }

    
    public int getGenerationRank() {
        return sampleInfo.generation_rank;
    }

    
    public int getAbsoluteGenerationRank() {
        return sampleInfo.absolute_generation_rank;
    }

    public OSPLSample<TYPE> clone() {
        return new OSPLSample<TYPE>(value, sampleInfo);
    }



    public static final class SampleIterator<TYPE> implements Iterator<TYPE> {

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

        
        public boolean hasNext() {
            return list.length > index;
        }

        
        public boolean hasPrevious() {
            return index > 0;
        }

        
        public Sample<TYPE> next() {
            Sample<TYPE> sample = new OSPLSample<TYPE>(list[index],
                                                       sampleInfoList.value[index]);
            index++;
            return sample;
        }

        
        public int nextIndex() {
            return ++index;
        }

        
        public Sample<TYPE> previous() {
            index--;
            Sample<TYPE> sample = new OSPLSample<TYPE>(list[index],
                                                       sampleInfoList.value[index]);
            return sample;
        }

        
        public int previousIndex() {
            return --index;
        }


        public void returnLoan() {

            reader.returnLoan(sampleDataList, sampleInfoList);
        }

        
        public void remove() {
            throw new RuntimeException("remove sample not supported");
        }

        
        public void set(Sample<TYPE> o) {
            throw new RuntimeException("Set sample not supported");
        }

        
        public void add(Sample<TYPE> o) {
            throw new RuntimeException("Add sample not supported");
        }

    }
}
