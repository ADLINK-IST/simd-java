package org.opensplice.psm.java.pub;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import DDS.TypeSupport;
import org.omg.dds.core.*;
import org.omg.dds.core.status.LivelinessLost;
import org.omg.dds.core.status.OfferedDeadlineMissed;
import org.omg.dds.core.status.OfferedIncompatibleQos;
import org.omg.dds.core.status.PublicationMatched;
import org.omg.dds.core.status.Status;
import org.omg.dds.pub.DataWriter;
import org.omg.dds.pub.DataWriterListener;
import org.omg.dds.pub.DataWriterQos;
import org.omg.dds.pub.Publisher;
import org.omg.dds.topic.SubscriptionBuiltinTopicData;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicDescription;
import org.opensplice.psm.java.topic.OSPLTopic;

import DDS.ANY_STATUS;

public class OSPLDataWriter<TYPE> implements DataWriter<TYPE> {
    final private OSPLTopic<TYPE> topic;
    final private OSPLPublisher publisher;
    private DDS.DataWriter peer = null;
    private OSPLDataWriterQos qos = null;
    private DataWriterListener<TYPE> listener = null;
    private long copyCache = 0;


    public OSPLDataWriter(
            TopicDescription<TYPE> topic, Publisher publisher,
            DataWriterQos qos) {

        this.topic = (OSPLTopic<TYPE>) topic;
        this.publisher = (OSPLPublisher) publisher;
        if (qos != null) {
            this.qos = (OSPLDataWriterQos) qos;
        } else {
            this.qos = (OSPLDataWriterQos) publisher.getDefaultDataWriterQos();
        }
        createWriter();
        try {
            TypeSupport ts = this.topic.getTypeSupport();
            Class<?> tsClass = ts.getClass();
            Method m = tsClass.getDeclaredMethod("get_copyCache");
            Long r = (Long)m.invoke(ts);
            this.copyCache = r.longValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Get the underlying class for a type, or null if the type is a variable
     * type.
     * @param type
     *            the type
     * @return the underlying class
     */
    public static Class<?> getClass(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type)
                    .getGenericComponentType();
            Class<?> componentClass = getClass(componentType);
            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private void createWriter() {
        peer =
                publisher.getPublisher().create_datawriter(
                        topic.getPeer(),
                        qos.getQos(),
                        null,
                        ANY_STATUS.value);


        assert (peer != null);
    }

    public void enable() {
        if (peer != null) {
            peer.enable();
        }
    }

    public StatusCondition<DataWriter<TYPE>> getStatusCondition() {
        // TODO Auto-generated method stub
        return null;
    }


    public InstanceHandle getInstanceHandle() {
        // TODO Auto-generated method stub
        return null;
    }


    public void close() {
        // peer.close();
    }


    public void retain() {
        // TODO Auto-generated method stub
    }


    public Class<TYPE> getType() {
        return topic.getType();
    }


    public <OTHER> DataWriter<OTHER> cast() {
        // TODO Auto-generated method stub
        return null;
    }


    public Topic<TYPE> getTopic() {
        return topic;
    }


    public void waitForAcknowledgments(Duration maxWait)
            throws TimeoutException {
        // TODO Auto-generated method stub
    }

    public void waitForAcknowledgments(long maxWait, TimeUnit unit)
            throws TimeoutException {
        // TODO Auto-generated method stub
    }


    public void assertLiveliness() {
        // TODO Auto-generated method stub

    }


    public Collection<InstanceHandle> getMatchedSubscriptions(
            Collection<InstanceHandle> subscriptionHandles) {
        // TODO Auto-generated method stub
        return null;
    }


    public SubscriptionBuiltinTopicData getMatchedSubscriptionData(
            SubscriptionBuiltinTopicData subscriptionData,
            InstanceHandle subscriptionHandle) {
        // TODO Auto-generated method stub
        return null;
    }


    public InstanceHandle registerInstance(TYPE instanceData)
            throws TimeoutException {
        // TODO Auto-generated method stub
        return null;
    }


    public InstanceHandle registerInstance(TYPE instanceData,
            Time sourceTimestamp) throws TimeoutException {
        // TODO Auto-generated method stub
        return null;
    }


    public InstanceHandle registerInstance(TYPE instanceData,
            long sourceTimestamp, TimeUnit unit) throws TimeoutException {
        // TODO Auto-generated method stub
        return null;
    }


    public void unregisterInstance(InstanceHandle handle)
            throws TimeoutException {
        // TODO Auto-generated method stub

    }


    public void unregisterInstance(InstanceHandle handle, TYPE instanceData)
            throws TimeoutException {
        // TODO Auto-generated method stub

    }


    public void unregisterInstance(InstanceHandle handle, TYPE instanceData,
            Time sourceTimestamp) throws TimeoutException {
        // TODO Auto-generated method stub

    }


    public void unregisterInstance(InstanceHandle handle, TYPE instanceData,
            long sourceTimestamp, TimeUnit unit) throws TimeoutException {
        // TODO Auto-generated method stub

    }


    public void write(TYPE sample) throws TimeoutException {
        org.opensplice.dds.dcps.FooDataWriterImpl.write
                (this.peer,
                        this.copyCache,
                        sample,
                        DDS.HANDLE_NIL.value);

    }


    public void write(TYPE instanceData, Time sourceTimestamp)
            throws TimeoutException {
        // TODO Auto-generated method stub

    }


    public void write(TYPE instanceData, long sourceTimestamp, TimeUnit unit)
            throws TimeoutException {
        // TODO Auto-generated method stub

    }


    public void write(TYPE instanceData, InstanceHandle handle)
            throws TimeoutException {
        // TODO Auto-generated method stub

    }


    public void write(TYPE instanceData, InstanceHandle handle,
            Time sourceTimestamp) throws TimeoutException {
        // TODO Auto-generated method stub

    }


    public void write(TYPE instanceData, InstanceHandle handle,
            long sourceTimestamp, TimeUnit unit) throws TimeoutException {
        // TODO Auto-generated method stub

    }


    public void dispose(InstanceHandle instanceHandle) throws TimeoutException {
    }


    public void dispose(InstanceHandle instanceHandle, TYPE instanceData)
            throws TimeoutException {
        org.opensplice.dds.dcps.FooDataWriterImpl.dispose(this.peer,copyCache, instanceData, 0);
    }


    public void dispose(InstanceHandle instanceHandle, TYPE instanceData,
            Time sourceTimestamp) throws TimeoutException {
        // TODO Auto-generated method stub

    }


    public void dispose(InstanceHandle instanceHandle, TYPE instanceData,
            long sourceTimestamp, TimeUnit unit) throws TimeoutException {
        // TODO Auto-generated method stub

    }


    public TYPE getKeyValue(TYPE keyHolder, InstanceHandle handle) {
        // TODO Auto-generated method stub
        return null;
    }


    public Collection<Class<? extends Status<?>>> getStatusChanges(
            Collection<Class<? extends Status<?>>> statuses) {
        // TODO Auto-generated method stub
        return null;
    }


    public LivelinessLost getLivelinessLostStatus() {
        // TODO Auto-generated method stub
        return null;
    }


    public OfferedDeadlineMissed getOfferedDeadlineMissedStatus() {
        // TODO Auto-generated method stub
        return null;
    }


    public OfferedIncompatibleQos getOfferedIncompatibleQosStatus() {
        // TODO Auto-generated method stub
        return null;
    }


    public PublicationMatched getPublicationMatchedStatus() {
        // TODO Auto-generated method stub
        return null;
    }


    public InstanceHandle lookupInstance(InstanceHandle handle, TYPE keyHolder) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setListener(DataWriterListener<TYPE> typeDataWriterListener) {
        this.listener = listener;
    }

    public DataWriterListener<TYPE> getListener() {
        return this.listener;
    }

}
