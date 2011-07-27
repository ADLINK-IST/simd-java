package org.opensplice.psm.java.pub;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.omg.dds.core.Duration;
import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.Time;
import org.omg.dds.core.status.LivelinessLost;
import org.omg.dds.core.status.OfferedDeadlineMissed;
import org.omg.dds.core.status.OfferedIncompatibleQos;
import org.omg.dds.core.status.PublicationMatched;
import org.omg.dds.core.status.Status;
import org.omg.dds.pub.DataWriter;
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
    private DDS.DataWriter dataWriter = null;
    private Method write = null;
    private Method dispose = null;
    private Object[] writeParameters = null;
    private OSPLDataWriterQos qos = null;

    public OSPLDataWriter(
            TopicDescription<TYPE> theTopic, Publisher thePublisher,
            DataWriterQos theQos) {
        topic = (OSPLTopic<TYPE>) theTopic;
        publisher = (OSPLPublisher) thePublisher;
        if (theQos != null) {
            qos = (OSPLDataWriterQos) theQos;
        } else {
            qos = (OSPLDataWriterQos) publisher.getDefaultDataWriterQos();
        }
        createWriter();
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
        try {
            dataWriter = publisher.getPublisher().create_datawriter(
                    topic.getTopic(), qos.getQos(), null, ANY_STATUS.value);
            Class<?> dataWriterImplClass = Class.forName(topic.getTypeName()
                    + "DataWriterImpl");

            Class<?> partypes[] = new Class[2];
            partypes[1] = Long.TYPE;
            partypes[0] = topic.getType();

            write = dataWriterImplClass.getMethod("write", partypes);
            dispose = dataWriterImplClass.getMethod("dispose", partypes);
            writeParameters = new Object[2];
            writeParameters[1] = new Long(0);
        } catch (Exception e) {
            System.err.println("Could not create writer for: "
                    + topic.getName() + " " + e.getClass().getName() + "."
                    + e.getMessage());
            dataWriter = null;
        }

    }

    public void enable() {
        if (dataWriter != null) {
            dataWriter.enable();
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
        // dataWriter.close();
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


    public void write(TYPE instanceData) throws TimeoutException {
        writeParameters[0] = instanceData;
        try {
            write.invoke(dataWriter, writeParameters);
        } catch (Exception e) {
            System.err.println("Write failed " + e.getMessage());
        }
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
        writeParameters[0] = instanceData;
        try {
            dispose.invoke(dataWriter, writeParameters);
        } catch (Exception e) {
            System.err.println("Write failed " + e.getMessage());
        }
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

}
