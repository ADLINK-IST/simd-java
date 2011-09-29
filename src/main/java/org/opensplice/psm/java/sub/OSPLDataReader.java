package org.opensplice.psm.java.sub;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.omg.dds.core.DDSException;
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
import org.omg.dds.runtime.DDSRuntime;
import org.omg.dds.sub.*;
import org.omg.dds.sub.QueryCondition;
import org.omg.dds.sub.ReadCondition;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.sub.Subscriber;
import org.omg.dds.topic.PublicationBuiltinTopicData;
import org.omg.dds.topic.TopicDescription;
import org.opensplice.psm.java.core.OSPLInstanceHandle;
import org.opensplice.psm.java.core.policy.PolicyConverter;
import org.opensplice.psm.java.core.policy.QoSConverter;
import org.opensplice.psm.java.topic.OSPLTopic;

public class OSPLDataReader<TYPE> implements DataReader<TYPE> {

    final private OSPLTopic<TYPE> topic;
    final private OSPLSubscriber subscriber;
    private DataReaderListener<TYPE> listener = null;
    private DDS.DataReader peer = null;
    private DataReaderQos qos;

    // Remove fields below
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
    // endRemove


    private Class<?> dataSeqClass;
    private Field    valueField;
    private long copyCache;



    public OSPLDataReader(TopicDescription<TYPE> topic, Subscriber subscriber, DataReaderQos qos) {
        this.topic = (OSPLTopic<TYPE>) topic;
        this.subscriber = (OSPLSubscriber) subscriber;
        this.qos = qos;
        this.peer = createReader();
        try {
            DDS.TypeSupport ts = this.topic.getTypeSupport();
            Class<?> tsClass = ts.getClass();
            Method m = tsClass.getDeclaredMethod("get_copyCache");
            Long r = (Long)m.invoke(ts);
            this.copyCache = r.longValue();
            String typeName = this.topic.getType().getName();
            this.dataSeqClass = Class.forName(typeName + "SeqHolder");
            this.valueField = dataSeqClass.getField("value");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DDS.DataReader createReader() {
        DDS.DataReader dr = null;
        DDS.DataReaderQosHolder toQosH= new DDS.DataReaderQosHolder();
        subscriber.getPeer().get_default_datareader_qos(toQosH);
        DDS.DataReaderQos toQos = QoSConverter.convert(this.qos, toQosH.value);

        dr =
                this.subscriber.getPeer().create_datareader(
                        this.topic.getPeer(),
                        toQos, null,
                        DDS.ANY_STATUS.value);

        assert (dr != null);
        return dr;
    }


    public void setListener(DataReaderListener<TYPE> thelistener) {
        listener = thelistener;
        if (thelistener == null) {
            peer.set_listener(null, 0);
        } else {
            MyDataReaderListener mylistener = new MyDataReaderListener(this,
                    thelistener);
            peer.set_listener(mylistener, DDS.ANY_STATUS.value);
        }
    }


    public void enable() {
        if (peer != null) {
            peer.enable();
        }
    }

    /**
     * This operation allows access to the {@link org.omg.dds.core.StatusCondition} associated
     * with the Entity. The returned condition can then be added to a
     * {@link org.omg.dds.core.WaitSet} so that the application can wait for specific status
     * changes that affect the Entity.
     */
    public StatusCondition<DataReader<TYPE>> getStatusCondition() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This operation retrieves the list of communication statuses in the
     * Entity that are 'triggered.' That is, the list of statuses whose value
     * has changed since the last time the application read the status.
     * <p/>
     * When the entity is first created or if the entity is not enabled, all
     * communication statuses are in the "untriggered" state so the list
     * returned will be empty.
     * <p/>
     * The list of statuses returned refers to the statuses that are
     * triggered on the Entity itself and does not include statuses that
     * apply to contained entities.
     *
     * @param statuses a container for the resulting statuses; its
     *                 contents will be overwritten by the result of
     *                 this operation.
     * @return the argument as a convenience in order to facilitate call
     *         chaining.
     */
    public Collection<Class<? extends Status<?>>> getStatusChanges(Collection<Class<? extends Status<?>>> statuses) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public InstanceHandle getInstanceHandle() {
        return new OSPLInstanceHandle(peer.get_instance_handle());
    }

    /**
     * Halt communication and dispose the resources held by this Entity.
     * <p/>
     * Closing an Entity implicitly closes all of its contained objects, if
     * any. For example, closing a Publisher also closes all of its contained
     * DataWriters.
     * <p/>
     * An Entity cannot be closed if it has any unclosed dependent objects,
     * not including contained objects. These include the following:
     * <p/>
     * <ul>
     * <li>A {@link org.omg.dds.topic.Topic} cannot be closed if it is still in use by any
     * {@link org.omg.dds.topic.ContentFilteredTopic}s or {@link org.omg.dds.topic.MultiTopic}s.</li>
     * <li>A Topic cannot be closed if any {@link org.omg.dds.pub.DataWriter}s or
     * {@link org.omg.dds.sub.DataReader} is still using it.</li>
     * <li>A DataReader cannot be closed if it has any outstanding loans
     * as a result of a call to {@link org.omg.dds.sub.DataReader#read()},
     * {@link org.omg.dds.sub.DataReader#take()}, or one of the variants thereof.
     * </li>
     * </ul>
     * <p/>
     * The deletion of a {@link org.omg.dds.pub.DataWriter} will automatically unregister all
     * instances. Depending on the settings of the
     * {@link org.omg.dds.core.policy.WriterDataLifecycle}, the deletion of the DataWriter
     * may also dispose all instances.
     *
     * @throws org.omg.dds.core.PreconditionNotMetException
     *          if close is called on an
     *          Entity with unclosed dependent object(s), not including
     *          contained objects.
     * @see org.omg.dds.topic.TopicDescription#close()
     */
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Indicates that references to this object may go out of scope but that
     * the application expects to look it up again later. Therefore, the
     * Service must consider this object to be still in use and may not
     * close it automatically.
     */
    public void retain() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    /**
     * @return the type parameter if this object's class.
     */
    public Class<TYPE> getType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ReadCondition<TYPE> createReadCondition() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This operation creates a ReadCondition. The returned ReadCondition
     * will be attached and belong to the DataReader.
     *
     * @param state the state for the Sample, Instance and View.
     */
    public ReadCondition<TYPE> createReadCondition(ReadState state) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This operation creates a QueryCondition. The returned QueryCondition
     * will be attached and belong to the DataReader. It will trigger on any
     * sample state, view state, or instance state.
     *
     * @param queryExpression The returned condition will only trigger on
     *                        samples that pass this content-based filter expression.
     * @param queryParameters A set of parameter values for the
     *                        queryExpression.
     * @see #createQueryCondition(org.omg.dds.sub.ReadState, String, java.util.List)
     */
    public QueryCondition<TYPE> createQueryCondition(String queryExpression, List<String> queryParameters) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This operation creates a QueryCondition. The returned QueryCondition
     * will be attached and belong to the DataReader.
     *
     * @param state           The state for the Sample, Instance and View.
     * @param queryExpression The returned condition will only trigger on
     *                        samples that pass this content-based filter expression.
     * @param queryParameters A set of parameter values for the
     *                        queryExpression.
     * @see #createQueryCondition(String, java.util.List)
     */
    public QueryCondition<TYPE> createQueryCondition(ReadState state, String queryExpression, List<String> queryParameters) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This operation closes all the entities that were created by means of
     * the "create" operations on the DataReader. That is, it closes all
     * contained ReadCondition and QueryCondition objects.
     *
     * @throws org.omg.dds.core.PreconditionNotMetException
     *          if the any of the contained
     *          entities is in a state where it cannot be closed.
     */
    public void closeContainedEntities() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @return the TopicDescription associated with the DataReader. This is
     *         the same TopicDescription that was used to create the
     *         DataReader.
     */
    public TopicDescription<TYPE> getTopicDescription() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This operation allows access to the SAMPLE_REJECTED communication
     * status.
     *
     * @return the input status, as a convenience to facilitate chaining.
     * @see org.omg.dds.core.status
     */
    public SampleRejected getSampleRejectedStatus() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This operation allows access to the LIVELINESS_CHANGED communication
     * status.
     *
     * @return the input status, as a convenience to facilitate chaining.
     * @see org.omg.dds.core.status
     */
    public LivelinessChanged getLivelinessChangedStatus() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This operation allows access to the REQUESTED_DEADLINE_MISSED
     * communication status.
     *
     * @return the input status, as a convenience to facilitate chaining.
     * @see org.omg.dds.core.status
     */
    public RequestedDeadlineMissed getRequestedDeadlineMissedStatus() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This operation allows access to the REQUESTED_INCOMPATIBLE_QOS
     * communication status.
     *
     * @return the input status, as a convenience to facilitate chaining.
     * @see org.omg.dds.core.status
     */
    public RequestedIncompatibleQos getRequestedIncompatibleQosStatus() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This operation allows access to the SUBSCRIPTION_MATCHED communication
     * status.
     *
     * @return the input status, as a convenience to facilitate chaining.
     * @see org.omg.dds.core.status
     */
    public SubscriptionMatched getSubscriptionMatchedStatus() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This operation allows access to the SAMPLE_LOST communication status.
     * <p/>
     * result.
     *
     * @return the input status, as a convenience to facilitate chaining.
     * @see org.omg.dds.core.status
     */
    public SampleLost getSampleLostStatus() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void waitForHistoricalData(Duration maxWait) throws TimeoutException {
        peer.wait_for_historical_data(PolicyConverter.convert(maxWait));
    }


    public void waitForHistoricalData(long maxWait, TimeUnit unit)
            throws TimeoutException {
        Duration wait = new Duration(maxWait, unit);
        waitForHistoricalData(wait);
    }

    /**
     * This operation retrieves the list of publications currently
     * "associated" with the DataReader; that is, publications that have a
     * matching {@link org.omg.dds.topic.Topic} and compatible QoS that the application has not
     * indicated should be "ignored" by means of
     * {@link org.omg.dds.domain.DomainParticipant#ignorePublication(org.omg.dds.core.InstanceHandle)}.
     * <p/>
     * The handles returned in the 'publicationHandles' list are the ones
     * that are used by the DDS implementation to locally identify the
     * corresponding matched DataWriter entities. These handles match the
     * ones that appear in {@link org.omg.dds.sub.Sample#getInstanceHandle()} when reading
     * the "DCPSPublications" built-in topic.
     * <p/>
     * The operation may fail if the infrastructure does not locally maintain
     * the connectivity information.
     *
     * @param publicationHandles a container, into which this method
     *                           will place its result.
     * @return publicationHandles, as a convenience to facilitate chaining.
     * @see #getMatchedPublicationData(org.omg.dds.topic.PublicationBuiltinTopicData, org.omg.dds.core.InstanceHandle)
     */
    public Collection<InstanceHandle> getMatchedPublications(Collection<InstanceHandle> publicationHandles) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This operation retrieves information on a publication that is
     * currently "associated" with the DataReader; that is, a publication
     * with a matching {@link org.omg.dds.topic.Topic} and compatible QoS that the application
     * has not indicated should be "ignored" by means of
     * {@link org.omg.dds.domain.DomainParticipant#ignorePublication(org.omg.dds.core.InstanceHandle)}.
     * <p/>
     * The operation {@link #getMatchedPublications(java.util.Collection)} can be used
     * to find the publications that are currently matched with the
     * DataReader.
     *
     * @param publicationData   a container, into which this method
     *                          will place its result.
     * @param publicationHandle a handle to the publication, the
     *                          data of which is to be retrieved.
     * @return subscriptionData, as a convenience to facilitate chaining.
     * @throws IllegalArgumentException      if the publicationHandle does
     *                                       not correspond to a publication currently associated with the
     *                                       DataReader.
     * @throws UnsupportedOperationException if the infrastructure does
     *                                       not hold the information necessary to fill in the
     *                                       publicationData.
     * @see #getMatchedPublications(java.util.Collection)
     */
    public PublicationBuiltinTopicData getMatchedPublicationData(PublicationBuiltinTopicData publicationData, InstanceHandle publicationHandle) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     *
     * @return a non-null unmodifiable iterator over loaned samples.
     */
    public Iterator<TYPE> read() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Iterator<TYPE> read(ReadState state) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void read(List<Sample<TYPE>> samples) {
        //TODO: Should throw the appropriate exception when the ret-code is not 0.
        try {
            samples.clear();
            Object data = this.dataSeqClass.newInstance();
            DDS.SampleInfoSeqHolder info =
                    new DDS.SampleInfoSeqHolder();
            int rv =
                org.opensplice.dds.dcps.FooDataReaderImpl.read(
                    this.peer,
                    this.copyCache,
                    data,
                    info,
                    DDS.LENGTH_UNLIMITED.value,
                    DDS.NOT_READ_SAMPLE_STATE.value,
                    DDS.ANY_VIEW_STATE.value,
                    DDS.ALIVE_INSTANCE_STATE.value);
            TYPE vals[] = (TYPE[])this.valueField.get(data);
            for (int i = 0; i < vals.length; ++i) {
                Sample<TYPE> s = new OSPLSample<TYPE>(vals[i], info.value[i]);
                samples.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void read(List<Sample<TYPE>> samples, int maxSamples, ReadState state) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     *
     * @return a non-null unmodifiable iterator over loaned samples.
     */
    public Iterator<TYPE> take() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Iterator<TYPE> take(ReadState state) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void take(List<Sample<TYPE>> samples) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void take(List<Sample<TYPE>> samples, int maxSamples, ReadState state) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     *
     * @return a non-null unmodifiable iterator over loaned samples.
     */
    public Iterator<TYPE> read(ReadCondition<TYPE> typeReadCondition) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void read(List<Sample<TYPE>> samples, ReadCondition<TYPE> typeReadCondition) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void read(List<Sample<TYPE>> samples, int maxSamples, ReadCondition<TYPE> typeReadCondition) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     *
     * @return a non-null unmodifiable iterator over loaned samples.
     */
    public Iterator<TYPE> take(ReadCondition<TYPE> typeReadCondition) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void take(List<Sample<TYPE>> samples, ReadCondition<TYPE> typeReadCondition) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void take(List<Sample<TYPE>> samples, int maxSamples, ReadCondition<TYPE> typeReadCondition) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     *
     * @return true if data was read or false if no data was available.
     */
    public boolean readNext(Sample<TYPE> typeSample) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @return true if data was taken or false if no data was available.
     */
    public boolean takeNext(Sample<TYPE> typeSample) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     *
     * @return a non-null unmodifiable iterator over loaned samples.
     */
    public Iterator<TYPE> read(InstanceHandle handle) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     *
     * @return a non-null unmodifiable iterator over loaned samples.
     */
    public Iterator<TYPE> read(InstanceHandle handle, ReadState state) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void read(List<Sample<TYPE>> samples, InstanceHandle handle) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void read(List<Sample<TYPE>> samples, InstanceHandle handle, int maxSamples, ReadState state) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     *
     * @return a non-null unmodifiable iterator over loaned samples.
     */
    public Iterator<TYPE> take(InstanceHandle handle) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     *
     * @return a non-null unmodifiable iterator over loaned samples.
     */
    public Iterator<TYPE> take(InstanceHandle handle, ReadState state) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void take(List<Sample<TYPE>> samples, InstanceHandle handle) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void take(List<Sample<TYPE>> samples, InstanceHandle handle, int maxSamples, ReadState state) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     *
     * @return a non-null unmodifiable iterator over loaned samples.
     */
    public Iterator<TYPE> readNext(InstanceHandle previousHandle) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     *
     * @return a non-null unmodifiable iterator over loaned samples.
     */
    public Iterator<TYPE> readNext(InstanceHandle previousHandle, ReadState state) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void readNext(List<Sample<TYPE>> samples, InstanceHandle previousHandle) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void readNext(List<Sample<TYPE>> samples, InstanceHandle previousHandle, int maxSamples, ReadState state) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     *
     * @return a non-null unmodifiable iterator over loaned samples.
     */
    public Iterator<TYPE> takeNext(InstanceHandle previousHandle) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     *
     * @return a non-null unmodifiable iterator over loaned samples.
     */
    public Iterator<TYPE> takeNext(InstanceHandle previousHandle, ReadState state) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void takeNext(List<Sample<TYPE>> samples, InstanceHandle previousHandle) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void takeNext(List<Sample<TYPE>> samples, InstanceHandle previousHandle, int maxSamples, ReadState state) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     *
     * @return a non-null unmodifiable iterator over loaned samples.
     */
    public Iterator<TYPE> readNext(InstanceHandle previousHandle, ReadCondition<TYPE> typeReadCondition) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void readNext(List<Sample<TYPE>> samples, InstanceHandle previousHandle, ReadCondition<TYPE> typeReadCondition) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void readNext(List<Sample<TYPE>> samples, InstanceHandle previousHandle, int maxSamples, ReadCondition<TYPE> typeReadCondition) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     *
     * @return a non-null unmodifiable iterator over loaned samples.
     */
    public Iterator<TYPE> takeNext(InstanceHandle previousHandle, ReadCondition<TYPE> typeReadCondition) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void takeNext(List<Sample<TYPE>> samples, InstanceHandle previousHandle, ReadCondition<TYPE> typeReadCondition) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * TODO: Add JavaDoc.
     * <p/>
     * Copy samples into the provided collection, overwriting any samples that
     * might already be present.
     */
    public void takeNext(List<Sample<TYPE>> samples, InstanceHandle previousHandle, int maxSamples, ReadCondition<TYPE> typeReadCondition) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This operation can be used to retrieve the instance key that
     * corresponds to an instance handle. The operation will only fill the
     * fields that form the key inside the keyHolder instance.
     *
     * @param keyHolder a container, into which this method shall
     *                  place its result.
     * @param handle    a handle indicating the instance whose value
     *                  this method should get.
     * @return keyHolder, as a convenience to facilitate chaining.
     * @throws IllegalArgumentException if the {@link org.omg.dds.core.InstanceHandle}
     *                                  does not correspond to an existing data object known to the
     *                                  DataReader. If the implementation is not able to check
     *                                  invalid handles, then the result in this situation is
     *                                  unspecified.
     */
    public TYPE getKeyValue(TYPE keyHolder, InstanceHandle handle) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This operation takes as a parameter an instance and returns a handle
     * that can be used in subsequent operations that accept an instance
     * handle as an argument. The instance parameter is only used for the
     * purpose of examining the fields that define the key.
     * <p/>
     * This operation does not register the instance in question. If the
     * instance has not been previously registered, or if for any other
     * reason the Service is unable to provide an instance handle, the
     * Service will return a nil handle.
     *
     * @param handle    a container, into which this method shall place its
     *                  result.
     * @param keyHolder a sample of the instance whose handle this
     *                  method should look up.
     * @return handle, as a convenience to facilitate chaining.
     */
    public InstanceHandle lookupInstance(InstanceHandle handle, TYPE keyHolder) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public DataReaderListener<TYPE> getListener() {
        return this.listener;
    }

    //public void setListener(DataReaderListener<TYPE> typeDataReaderListener) {
//        this.listener = listener;
  //  }

     // ------------------------------------------------------------------------------------
    // Helper Listener
    // ------------------------------------------------------------------------------------
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

    public void returnLoan(Object dataList, DDS.SampleInfoSeqHolder infoList) {
        try {
            returnLoanParameters[0] = dataList;
            returnLoanParameters[0] = infoList;
            return_loan.invoke(peer, returnLoanParameters);
        } catch (InvocationTargetException ie) {

        } catch (IllegalAccessException iae) {

        }
    }
   // ------------------------------------------------------------------------------------

    ///////////////////////////////////////////////////////////////////////////////////////
}
