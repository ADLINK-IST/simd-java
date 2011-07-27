package org.opensplice.psm.java.sub;

import java.util.Collection;

import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.status.Status;
import org.omg.dds.sub.DataReader;
import org.omg.dds.sub.DataReaderListener;
import org.omg.dds.sub.DataReaderQos;
import org.omg.dds.sub.InstanceState;
import org.omg.dds.sub.SampleState;
import org.omg.dds.sub.SubscriberListener;
import org.omg.dds.sub.SubscriberQos;
import org.omg.dds.sub.ViewState;
import org.omg.dds.topic.TopicDescription;
import org.omg.dds.topic.TopicQos;
import org.omg.dds.type.builtin.BytesDataReader;
import org.omg.dds.type.builtin.KeyedBytes;
import org.omg.dds.type.builtin.KeyedBytesDataReader;
import org.omg.dds.type.builtin.KeyedString;
import org.omg.dds.type.builtin.KeyedStringDataReader;
import org.omg.dds.type.builtin.StringDataReader;
import org.opensplice.psm.java.core.OSPLInstanceHandle;
import org.opensplice.psm.java.domain.OSPLDomainParticipant;

public class OSPLSubscriber implements org.omg.dds.sub.Subscriber {

    private DDS.Subscriber subscriber = null;
    private SubscriberListener thelistener = null;
    private OSPLDomainParticipant participant = null;
    private SubscriberQos theQos = null;

    public OSPLSubscriber(DDS.Subscriber impl) {
        subscriber = impl;
    }

    public DDS.Subscriber getSubscriber() {
        return subscriber;
    }

    @Override
    public void enable() {
        if (subscriber != null) {
            subscriber.enable();
        }
    }

    @Override
    public StatusCondition<org.omg.dds.sub.Subscriber> getStatusCondition() {
        return null;
    }

    @Override
    public InstanceHandle getInstanceHandle() {
        return new OSPLInstanceHandle(subscriber.get_instance_handle());
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

    @Override
    public void retain() {
        // TODO Auto-generated method stub
    }

    @Override
    public <TYPE> DataReader<TYPE> createDataReader(TopicDescription<TYPE> topic) {
        DataReader<TYPE> reader = new OSPLDataReader<TYPE>(topic, this);
        return reader;
    }

    @Override
    public <TYPE> DataReader<TYPE> createDataReader(
            TopicDescription<TYPE> topic, DataReaderQos qos,
            DataReaderListener<TYPE> listener,
            Collection<Class<? extends Status<?>>> statuses) {
        DataReader<TYPE> reader = createDataReader(topic);
        //reader.setListener(listener);
        return reader;
    }

    @Override
    public <TYPE> DataReader<TYPE> createDataReader(
            TopicDescription<TYPE> topic, String qosLibraryName,
            String qosProfileName, DataReaderListener<TYPE> listener,
            Collection<Class<? extends Status<?>>> statuses) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BytesDataReader createBytesDataReader(TopicDescription<byte[]> topic) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BytesDataReader createBytesDataReader(
            TopicDescription<byte[]> topic, DataReaderQos qos,
            DataReaderListener<byte[]> listener,
            Collection<Class<? extends Status<?>>> statuses) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BytesDataReader createBytesDataReader(
            TopicDescription<byte[]> topic, String qosLibraryName,
            String qosProfileName, DataReaderListener<byte[]> listener,
            Collection<Class<? extends Status<?>>> statuses) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KeyedBytesDataReader createKeyedBytesDataReader(
            TopicDescription<KeyedBytes> topic) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KeyedBytesDataReader createKeyedBytesDataReader(
            TopicDescription<KeyedBytes> topic, DataReaderQos qos,
            DataReaderListener<KeyedBytes> listener,
            Collection<Class<? extends Status<?>>> statuses) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KeyedBytesDataReader createKeyedBytesDataReader(
            TopicDescription<KeyedBytes> topic, String qosLibraryName,
            String qosProfileName, DataReaderListener<KeyedBytes> listener,
            Collection<Class<? extends Status<?>>> statuses) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringDataReader createStringDataReader(
            TopicDescription<String> topic) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringDataReader createStringDataReader(
            TopicDescription<String> topic, DataReaderQos qos,
            DataReaderListener<String> listener,
            Collection<Class<? extends Status<?>>> statuses) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringDataReader createStringDataReader(
            TopicDescription<String> topic, String qosLibraryName,
            String qosProfileName, DataReaderListener<String> listener,
            Collection<Class<? extends Status<?>>> statuses) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KeyedStringDataReader createKeyedStringDataReader(
            TopicDescription<KeyedString> topic) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KeyedStringDataReader createKeyedStringDataReader(
            TopicDescription<KeyedString> topic, DataReaderQos qos,
            DataReaderListener<KeyedString> listener,
            Collection<Class<? extends Status<?>>> statuses) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KeyedStringDataReader createKeyedStringDataReader(
            TopicDescription<KeyedString> topic, String qosLibraryName,
            String qosProfileName, DataReaderListener<KeyedString> listener,
            Collection<Class<? extends Status<?>>> statuses) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <TYPE> DataReader<TYPE> lookupDataReader(String topicName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <TYPE> DataReader<TYPE> lookupDataReader(
            TopicDescription<TYPE> topicName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BytesDataReader lookupBytesDataReader(
            TopicDescription<byte[]> topicName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KeyedBytesDataReader lookupKeyedBytesDataReader(
            TopicDescription<KeyedBytes> topicName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringDataReader lookupStringDataReader(
            TopicDescription<String> topicName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KeyedStringDataReader lookupKeyedStringDataReader(
            TopicDescription<KeyedString> topicName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void closeContainedEntities() {
        // TODO Auto-generated method stub

    }

    @Override
    public Collection<DataReader<?>> getDataReaders(
            Collection<DataReader<?>> readers) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<DataReader<?>> getDataReaders(
            Collection<DataReader<?>> readers,
            Collection<SampleState> sampleStates,
            Collection<ViewState> viewStates,
            Collection<InstanceState> instanceStates) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void notifyDataReaders() {
        // TODO Auto-generated method stub

    }

    @Override
    public void beginAccess() {
        // TODO Auto-generated method stub

    }

    @Override
    public void endAccess() {
        // TODO Auto-generated method stub
    }

    @Override
    public DataReaderQos getDefaultDataReaderQos() {
        DDS.DataReaderQosHolder holder = new DDS.DataReaderQosHolder();
        subscriber.get_default_datareader_qos(holder);
        return new OSPLDataReaderQos(holder.value);
    }

    @Override
    public void setDefaultDataReaderQos(DataReaderQos qos) {
        subscriber.set_default_datareader_qos(((OSPLDataReaderQos)qos).getQos());
    }

    @Override
    public void copyFromTopicQos(DataReaderQos dst, TopicQos src) {
//    	TODO implement OSPLTopicQos
//        DDS.DataReaderQosHolder holder = new DDS.DataReaderQosHolder();
//    	subscriber.copy_from_topic_qos(holder, ((OSPLTopicQos)src).getQos());
//    	subscriber.set_default_datareader_qos(holder.value);
    }

	@Override
	public Collection<Class<? extends Status<?>>> getStatusChanges(
			Collection<Class<? extends Status<?>>> statuses) {
		// TODO Auto-generated method stub
		return null;
	}

}
