package org.opensplice.psm.java.sub;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.omg.dds.core.Duration;
import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.event.DataAvailableEvent;
import org.omg.dds.core.event.LivelinessChangedEvent;
import org.omg.dds.core.status.LivelinessChanged;
import org.omg.dds.core.status.RequestedDeadlineMissed;
import org.omg.dds.core.status.RequestedIncompatibleQos;
import org.omg.dds.core.status.SampleLost;
import org.omg.dds.core.status.SampleRejected;
import org.omg.dds.core.status.Status;
import org.omg.dds.core.status.SubscriptionMatched;
import org.omg.dds.sub.DataReader;
import org.omg.dds.sub.DataReaderListener;
import org.omg.dds.sub.DataReaderQos;
import org.omg.dds.sub.InstanceState;
import org.omg.dds.sub.QueryCondition;
import org.omg.dds.sub.ReadCondition;
import org.omg.dds.sub.Sample;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.sub.SampleState;
import org.omg.dds.sub.Subscriber;
import org.omg.dds.sub.ViewState;
import org.omg.dds.topic.PublicationBuiltinTopicData;
import org.omg.dds.topic.TopicDescription;
import org.opensplice.psm.java.core.OSPLInstanceHandle;
import org.opensplice.psm.java.core.policy.OSPL;
import org.opensplice.psm.java.topic.OSPLTopic;

import DDS.ANY_STATUS;
import DDS.DataReaderQosHolder;
import DDS.DestinationOrderQosPolicyKind;
import DDS.Duration_t;
import DDS.SampleRejectedStatus;

public class OSPLDataReader<TYPE> implements DataReader<TYPE> {

    final private OSPLTopic<TYPE> topic;
    final private OSPLSubscriber subscriber;
    private DataReaderListener<TYPE> listener = null;
    private DDS.DataReader dataReader = null;

    private Method take = null;
    private Method read = null;
    private Method return_loan = null;

    private Field listValue = null;
    private Field sampleValue = null;
    private Object[] takeParameters = null;
    private Object[] readParameters = null;
    private Object[] returnLoanParameters = null;
    private DDS.SampleInfoSeqHolder infoList;
    private Object dataList;
    private Class<?> listClass;

    private class OSPLDataAvailableEvent<TYPE> extends
            DataAvailableEvent<TYPE> {

		private static final long serialVersionUID = 1L;
		private DataReader<TYPE> drsource;

        protected OSPLDataAvailableEvent(DataReader<TYPE> source) {
            super(source);
            drsource = source;
        }

        
        public DataReader<TYPE> getSource() {
            return drsource;
        }

		
		public DataAvailableEvent<TYPE> clone() {
			DataAvailableEvent<TYPE> theclone = new OSPLDataAvailableEvent<TYPE>(drsource);
			return theclone;
		}
    }

    private class OSPLLivelinessChangedEvent<TYPE> extends
            LivelinessChangedEvent<TYPE> {
        private DataReader<TYPE> drsource;
        private DDS.LivelinessChangedStatus status;

        protected OSPLLivelinessChangedEvent(DataReader<TYPE> source,
                DDS.LivelinessChangedStatus thestatus) {
            //super(source);
            drsource = source;
            status = thestatus;
        }

//        
        public DataReader<TYPE> getSource() {
            return drsource;
        }
    }

    private class MyDataReaderListener implements DDS.DataReaderListener {

        final private DataReaderListener<TYPE> listener;
        final private OSPLDataReader<TYPE> reader;

        public MyDataReaderListener(OSPLDataReader<TYPE> thereader,
                DataReaderListener<TYPE> thelistener) {
            reader = thereader;
            listener = thelistener;
        }

        
        public void on_data_available(DDS.DataReader arg0) {
            DataAvailableEvent<TYPE> event =
                    new OSPLDataAvailableEvent<TYPE>(reader);
            listener.onDataAvailable(event);
        }

        
        public void on_liveliness_changed(DDS.DataReader arg0,
                DDS.LivelinessChangedStatus arg1) {
            LivelinessChangedEvent<TYPE> event =
                    new OSPLLivelinessChangedEvent<TYPE>(reader, arg1);
            listener.onLivelinessChanged(event);
        }

        
        public void on_requested_deadline_missed(DDS.DataReader arg0,
                DDS.RequestedDeadlineMissedStatus arg1) {
            // TODO implement status
            listener.onRequestedDeadlineMissed(null);
        }

        
        public void on_requested_incompatible_qos(DDS.DataReader arg0,
                DDS.RequestedIncompatibleQosStatus arg1) {
            // TODO implement status
            listener.onRequestedIncompatibleQos(null);
        }

        
        public void on_sample_lost(DDS.DataReader arg0,
                DDS.SampleLostStatus arg1) {
            // TODO implement status
            listener.onSampleLost(null);
        }

        
        public void on_sample_rejected(DDS.DataReader arg0,
                DDS.SampleRejectedStatus arg1) {
            // TODO implement status
            listener.onSampleRejected(null);
        }

        
        public void on_subscription_matched(DDS.DataReader arg0,
                DDS.SubscriptionMatchedStatus arg1) {
            // TODO implement status
            listener.onSubscriptionMatched(null);
        }

    }

    public OSPLDataReader(
            TopicDescription<TYPE> theTopic, Subscriber theSubscriber) {
        topic = (OSPLTopic<TYPE>) theTopic;
        subscriber = (OSPLSubscriber) theSubscriber;
        createReader();
    }

    private void createReader() {
        try {
            DataReaderQosHolder holder = new DataReaderQosHolder();
            subscriber.getSubscriber().get_default_datareader_qos(holder);
            DDS.DataReaderQos drQos = holder.value;
            drQos.destination_order.kind =
                    DestinationOrderQosPolicyKind.BY_SOURCE_TIMESTAMP_DESTINATIONORDER_QOS;
            drQos.latency_budget.duration.sec = 0;
            drQos.latency_budget.duration.nanosec = 40000000;

            dataReader = subscriber.getSubscriber().create_datareader(
                    topic.getTopic(), drQos, null, ANY_STATUS.value);

            DataReaderQosHolder drQosHolder = new DataReaderQosHolder();
            dataReader.get_qos(drQosHolder);

            listClass = Class.forName(topic.getTypeName() + "SeqHolder");
            Class<?> partypes[] = new Class[6];
            partypes[0] = listClass;
            partypes[1] = DDS.SampleInfoSeqHolder.class;
            partypes[2] = Integer.TYPE;
            partypes[3] = Integer.TYPE;
            partypes[4] = Integer.TYPE;
            partypes[5] = Integer.TYPE;
            Class<?> dataReaderImplClass = Class.forName(topic.getTypeName()
                    + "DataReaderImpl");
            take = dataReaderImplClass.getMethod("take", partypes);
            read = dataReaderImplClass.getMethod("read", partypes);
            partypes = new Class[2];
            partypes[0] = listClass;
            partypes[1] = DDS.SampleInfoSeqHolder.class;
            return_loan = dataReaderImplClass.getMethod("return_loan",
                    partypes);

            infoList = new DDS.SampleInfoSeqHolder();
            dataList = listClass.newInstance();

            takeParameters = new Object[6];
            takeParameters[0] = dataList;
            takeParameters[1] = infoList;
            takeParameters[2] = new Integer(1000);
            takeParameters[3] = new Integer(DDS.ANY_SAMPLE_STATE.value);
            takeParameters[4] = new Integer(DDS.ANY_VIEW_STATE.value);
            takeParameters[5] = new Integer(DDS.ANY_INSTANCE_STATE.value);

            returnLoanParameters = new Object[2];
            returnLoanParameters[0] = dataList;
            returnLoanParameters[1] = infoList;

            listValue = listClass.getField("value");
            sampleValue = DDS.SampleInfoSeqHolder.class.getField("value");
        } catch (Exception e) {
            System.err.println("Could not create reader for: "
                    + topic.getName() + " " + e.getClass().getName() + "."
                    + e.getMessage());
            dataReader = null;
        }

    }

//    
//    public void setListener(DataReaderListener<TYPE> thelistener) {
//        listener = thelistener;
//        if (thelistener == null) {
//            dataReader.set_listener(null, 0);
//        } else {
//            MyDataReaderListener mylistener = new MyDataReaderListener(this,
//                    thelistener);
//            dataReader.set_listener(mylistener, DDS.ANY_STATUS.value);
//        }
//    }

    
    public void enable() {
    	if (dataReader != null) {
    		dataReader.enable();
    	}
    }

    
    public StatusCondition<DataReader<TYPE>> getStatusCondition() {
        return null;
    }

    
    public InstanceHandle getInstanceHandle() {
        return new OSPLInstanceHandle(dataReader.get_instance_handle());
    }

    
    public void close() {
    }

    
    public void retain() {
        // TODO Auto-generated method stub
    }

    
    public Class<TYPE> getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings({"unchecked"})
    public <OTHER> DataReader<OTHER> cast() {

        return (DataReader<OTHER>)this;
    }

    
    public ReadCondition<TYPE> createReadCondition() {
        // TODO Auto-generated method stub
        return null;
    }

    
    public ReadCondition<TYPE> createReadCondition(
            Collection<SampleState> sampleStates,
            Collection<ViewState> viewStates,
            Collection<InstanceState> instanceStates) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public QueryCondition<TYPE> createQueryCondition(String queryExpression,
            List<String> queryParameters) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public QueryCondition<TYPE> createQueryCondition(
            Collection<SampleState> sampleStates,
            Collection<ViewState> viewStates,
            Collection<InstanceState> instanceStates, String queryExpression,
            List<String> queryParameters) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public void closeContainedEntities() {
        // TODO Auto-generated method stub

    }

    
    public TopicDescription<TYPE> getTopicDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    
    public void waitForHistoricalData(Duration maxWait) throws TimeoutException {
    	dataReader.wait_for_historical_data(OSPL.convert(maxWait));
    }

    
    public void waitForHistoricalData(long maxWait, TimeUnit unit)
            throws TimeoutException {
    	Duration wait = new Duration(unit.convert(maxWait, TimeUnit.SECONDS));
    	waitForHistoricalData(wait);
    }

    
    public Collection<InstanceHandle> getMatchedPublications(
            Collection<InstanceHandle> publicationHandles) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public PublicationBuiltinTopicData getMatchedPublicationData(
            PublicationBuiltinTopicData publicationData,
            InstanceHandle publicationHandle) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public Sample<TYPE> createSample() {
        // TODO Auto-generated method stub
        return null;
    }

    
    public Iterator<TYPE> read() {

        // TODO Auto-generated method stub
        return null;
    }

    
    public Iterator<TYPE> read(Collection<SampleState> sampleStates,
            Collection<ViewState> viewStates,
            Collection<InstanceState> instanceStates) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public void read(List<Sample<TYPE>> samples) {
        // TODO Auto-generated method stub

    }

    
    public void read(List<Sample<TYPE>> samples, int maxSamples,
            Collection<SampleState> sampleStates,
            Collection<ViewState> viewStates,
            Collection<InstanceState> instanceStates) {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings({"unchecked"})
    public Iterator<TYPE> take() {
        Object data;
        try {
            data = listClass.newInstance();
            DDS.SampleInfoSeqHolder sampleInfoHolder = new DDS.SampleInfoSeqHolder();
            private_take_samples(data, sampleInfoHolder, 1000);
            TYPE[] list = (TYPE[]) listValue.get(data);
            OSPLSample.SampleIterator<TYPE> iterator =
                    new OSPLSample.SampleIterator<TYPE>(this, data, list,
                            sampleInfoHolder);
            return iterator;
        } catch (Exception e) {
        }
        return null;
    }

    
    public Iterator<TYPE> take(Collection<SampleState> sampleStates,
            Collection<ViewState> viewStates,
            Collection<InstanceState> instanceStates) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public void take(List<Sample<TYPE>> samples) {

        // TODO Auto-generated method stub

    }

    
    public void take(List<Sample<TYPE>> samples, int maxSamples,
            Collection<SampleState> sampleStates,
            Collection<ViewState> viewStates,
            Collection<InstanceState> instanceStates) {
        // TODO Auto-generated method stub

    }

    
    public Iterator<TYPE> read(ReadCondition<TYPE> condition) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public void read(List<Sample<TYPE>> samples, ReadCondition<TYPE> condition) {
        // TODO Auto-generated method stub

    }

    
    public void read(List<Sample<TYPE>> samples, int maxSamples,
            ReadCondition<TYPE> condition) {
        // TODO Auto-generated method stub

    }

    
    public Iterator<TYPE> take(ReadCondition<TYPE> condition) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public void take(List<Sample<TYPE>> samples, ReadCondition<TYPE> condition) {
        // TODO Auto-generated method stub

    }

    
    public void take(List<Sample<TYPE>> samples, int maxSamples,
            ReadCondition<TYPE> condition) {
        // TODO Auto-generated method stub

    }

    
    public boolean readNext(Sample<TYPE> sample) {
        // TODO Auto-generated method stub
        return false;
    }

    
    public boolean takeNext(Sample<TYPE> sample) {
        // TODO Auto-generated method stub
        return false;
    }

    
    public Iterator<TYPE> read(InstanceHandle handle) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public Iterator<TYPE> read(InstanceHandle handle,
            Collection<SampleState> sampleStates,
            Collection<ViewState> viewStates,
            Collection<InstanceState> instanceStates) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public void read(List<Sample<TYPE>> samples, InstanceHandle handle) {
        // TODO Auto-generated method stub

    }

    
    public void read(List<Sample<TYPE>> samples, InstanceHandle handle,
            int maxSamples, Collection<SampleState> sampleStates,
            Collection<ViewState> viewStates,
            Collection<InstanceState> instanceStates) {
        // TODO Auto-generated method stub

    }

    
    public Iterator<TYPE> take(InstanceHandle handle) {
        return null;
    }

    
    public Iterator<TYPE> take(InstanceHandle handle,
            Collection<SampleState> sampleStates,
            Collection<ViewState> viewStates,
            Collection<InstanceState> instanceStates) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public void take(List<Sample<TYPE>> samples, InstanceHandle handle) {
        // TODO Auto-generated method stub

    }

    
    public void take(List<Sample<TYPE>> samples, InstanceHandle handle,
            int maxSamples, Collection<SampleState> sampleStates,
            Collection<ViewState> viewStates,
            Collection<InstanceState> instanceStates) {
        // TODO Auto-generated method stub

    }

    
    public Iterator<TYPE> readNext(InstanceHandle previousHandle) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public Iterator<TYPE> readNext(InstanceHandle previousHandle,
            Collection<SampleState> sampleStates,
            Collection<ViewState> viewStates,
            Collection<InstanceState> instanceStates) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public void readNext(List<Sample<TYPE>> samples,
            InstanceHandle previousHandle) {
        // TODO Auto-generated method stub

    }

    
    public void readNext(List<Sample<TYPE>> samples,
            InstanceHandle previousHandle, int maxSamples,
            Collection<SampleState> sampleStates,
            Collection<ViewState> viewStates,
            Collection<InstanceState> instanceStates) {
        // TODO Auto-generated method stub

    }

    
    public Iterator<TYPE> takeNext(InstanceHandle previousHandle) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public Iterator<TYPE> takeNext(InstanceHandle previousHandle,
            Collection<SampleState> sampleStates,
            Collection<ViewState> viewStates,
            Collection<InstanceState> instanceStates) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public void takeNext(List<Sample<TYPE>> samples,
            InstanceHandle previousHandle) {
        // TODO Auto-generated method stub

    }

    
    public void takeNext(List<Sample<TYPE>> samples,
            InstanceHandle previousHandle, int maxSamples,
            Collection<SampleState> sampleStates,
            Collection<ViewState> viewStates,
            Collection<InstanceState> instanceStates) {
        // TODO Auto-generated method stub

    }

    
    public Iterator<TYPE> readNext(InstanceHandle previousHandle,
            ReadCondition<TYPE> condition) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public void readNext(List<Sample<TYPE>> samples,
            InstanceHandle previousHandle, ReadCondition<TYPE> condition) {
        // TODO Auto-generated method stub

    }

    
    public void readNext(List<Sample<TYPE>> samples,
            InstanceHandle previousHandle, int maxSamples,
            ReadCondition<TYPE> condition) {
        // TODO Auto-generated method stub

    }

    
    public Iterator<TYPE> takeNext(InstanceHandle previousHandle,
            ReadCondition<TYPE> condition) {
        // TODO Auto-generated method stub
        return null;
    }

    
    public void takeNext(List<Sample<TYPE>> samples,
            InstanceHandle previousHandle, ReadCondition<TYPE> condition) {
        // TODO Auto-generated method stub

    }

    
    public void takeNext(List<Sample<TYPE>> samples,
            InstanceHandle previousHandle, int maxSamples,
            ReadCondition<TYPE> condition) {
        // TODO Auto-generated method stub

    }

    
    public TYPE getKeyValue(TYPE keyHolder, InstanceHandle handle) {
        // TODO Auto-generated method stub
        return null;
    }

    private void private_take_samples(Object dataList,
            DDS.SampleInfoSeqHolder sampleInfo,
            int count) {
        try {
            takeParameters[0] = dataList;
            takeParameters[1] = sampleInfo;
            takeParameters[2] = Integer.valueOf(count);
            take.invoke(dataReader, takeParameters);
        } catch (InvocationTargetException ie) {
        } catch (IllegalAccessException iae) {
        }
    }

    @SuppressWarnings({"unchecked"})
    private void private_take(List<TYPE> list, int count) {
        try {
            takeParameters[2] = Integer.valueOf(count);
            take.invoke(dataReader, takeParameters);
            TYPE[] slist = (TYPE[]) listValue.get(dataList);
            count = slist.length;
            if (count != 0) {
                for (int j = 0; j < count; j++) {
                    list.add(slist[j]);
                }
            }
            return_loan.invoke(dataReader, returnLoanParameters);
        } catch (InvocationTargetException ie) {

        } catch (IllegalAccessException iae) {

        }
    }

    public void returnLoan(Object dataList, DDS.SampleInfoSeqHolder infoList) {
        try {
            returnLoanParameters[0] = dataList;
            returnLoanParameters[0] = infoList;
            return_loan.invoke(dataReader, returnLoanParameters);
        } catch (InvocationTargetException ie) {

        } catch (IllegalAccessException iae) {

        }
    }

	
	public Collection<Class<? extends Status<?>>> getStatusChanges(
			Collection<Class<? extends Status<?>>> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public SampleRejected getSampleRejectedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public LivelinessChanged getLivelinessChangedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public RequestedDeadlineMissed getRequestedDeadlineMissedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public RequestedIncompatibleQos getRequestedIncompatibleQosStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public SubscriptionMatched getSubscriptionMatchedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public SampleLost getSampleLostStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public InstanceHandle lookupInstance(InstanceHandle handle, TYPE keyHolder) {
		// TODO Auto-generated method stub
		return null;
	}

}
