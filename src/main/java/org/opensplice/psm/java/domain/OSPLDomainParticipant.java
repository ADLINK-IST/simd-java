package org.opensplice.psm.java.domain;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.omg.dds.core.Duration;
import org.omg.dds.core.InstanceHandle;
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.Time;
import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.domain.DomainParticipantListener;
import org.omg.dds.pub.Publisher;
import org.omg.dds.pub.PublisherListener;
import org.omg.dds.pub.PublisherQos;
import org.omg.dds.sub.Subscriber;
import org.omg.dds.sub.SubscriberListener;
import org.omg.dds.sub.SubscriberQos;
import org.omg.dds.topic.ContentFilteredTopic;
import org.omg.dds.topic.MultiTopic;
import org.omg.dds.topic.ParticipantBuiltinTopicData;
import org.omg.dds.topic.Topic;
import org.omg.dds.topic.TopicBuiltinTopicData;
import org.omg.dds.topic.TopicDescription;
import org.omg.dds.topic.TopicListener;
import org.omg.dds.topic.TopicQos;
import org.omg.dds.type.TypeSupport;
import org.opensplice.psm.java.core.OSPLInstanceHandle;
import org.opensplice.psm.java.core.policy.PolicyConverter;
import org.opensplice.psm.java.pub.OSPLPublisher;
import org.opensplice.psm.java.sub.OSPLSubscriber;
import org.opensplice.psm.java.topic.OSPLTopic;

import DDS.Time_tHolder;

public class OSPLDomainParticipant implements DomainParticipant {

    final private DDS.DomainParticipant peer;

    private DomainParticipantListener dlistener = null;

    public OSPLDomainParticipant(
            DDS.DomainParticipant participant) {
        peer = participant;
    }

    public DDS.DomainParticipant getPeer() {
        return peer;
    }

    /**
     * This operation creates a Publisher with default QoS policies and no
     * PublisherListener.
     * <p/>
     * The created Publisher belongs to the DomainParticipant that is its
     * factory.
     *
     * @see #createPublisher(org.omg.dds.pub.PublisherQos,
     *      org.omg.dds.pub.PublisherListener, java.util.Collection)
     */
    public Publisher createPublisher() {
        // TODO: This code should be rewritten so to fetch the QoS from the
        // QosProvider.
        DDS.PublisherQosHolder holder = new DDS.PublisherQosHolder();
        peer.get_default_publisher_qos(holder);
        DDS.Publisher publisher = peer.create_publisher(
                holder.value, null, 0);
        return new OSPLPublisher(publisher, new PublisherQos(), this);
    }

    /**
     * This operation creates a Publisher.
     * <p/>
     * The created Publisher belongs to the DomainParticipant that is its
     * factory.
     *
     * @param qos
     *            The desired QoS policies. If the specified QoS policies are
     *            not consistent, the operation will fail and no Publisher will
     *            be created.
     * @param listener
     *            The listener to be attached.
     * @param statuses
     *            Of which status changes the listener should be notified. A
     *            null collection signifies all status changes.
     * @see #createPublisher()
     */
    public Publisher createPublisher(PublisherQos qos,
            PublisherListener listener,
            Collection<Class<? extends Status<?>>> statuses) {
        throw new RuntimeException("Not implemented");
    }



    /**
     * This operation creates a Subscriber with default QoS policies and no
     * SubscriberListener.
     * <p/>
     * The created Subscriber belongs to the DomainParticipant that is its
     * factory.
     *
     * @see #createSubscriber(org.omg.dds.sub.SubscriberQos,
     *      org.omg.dds.sub.SubscriberListener, java.util.Collection)
     */
    public Subscriber createSubscriber() {
        DDS.SubscriberQosHolder holder = new DDS.SubscriberQosHolder();
        peer.get_default_subscriber_qos(holder);
        // holder.value.partition.name = new String[] {partition};
        DDS.Subscriber subscriber = peer.create_subscriber(
                holder.value, null, 0);
        subscriber.enable();
        OSPLSubscriber subscriberOpensplice = new OSPLSubscriber(subscriber);
        return subscriberOpensplice;
    }

    /**
     * This operation creates a Subscriber.
     * <p/>
     * The created Subscriber belongs to the DomainParticipant that is its
     * factory.
     *
     * @param qos
     *            The desired QoS policies. If the specified QoS policies are
     *            not consistent, the operation will fail and no Subscriber will
     *            be created.
     * @param listener
     *            The listener to be attached.
     * @param statuses
     *            Of which status changes the listener should be notified. A
     *            null collection signifies all status changes.
     * @see #createSubscriber()
     */
    public Subscriber createSubscriber(SubscriberQos qos,
            SubscriberListener listener,
            Collection<Class<? extends Status<?>>> statuses) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation allows access to the built-in Subscriber. Each
     * DomainParticipant contains several built-in
     * {@link org.omg.dds.topic.Topic} objects as well as corresponding
     * {@link org.omg.dds.sub.DataReader} objects to access them. All these
     * DataReader objects belong to a single built-in Subscriber.
     * <p/>
     * The built-in Topics are used to communicate information about other
     * DomainParticipant, Topic, {@link org.omg.dds.sub.DataReader}, and
     * {@link org.omg.dds.pub.DataWriter} objects.
     */
    public Subscriber getBuiltinSubscriber() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation creates a Topic with default QoS policies and no
     * TopicListener.
     * <p/>
     * The created Topic belongs to the DomainParticipant that is its factory.
     *
     * @param topicName
     *            The name of the new Topic.
     * @param type
     *            The type of all samples to be published and subscribed over
     *            the new Topic. The Service will attempt to locate an
     *            appropriate {@link org.omg.dds.type.TypeSupport} instance
     *            based on this type.
     */
    public <TYPE> Topic<TYPE> createTopic(String topicName, Class<TYPE> type) {
        return new OSPLTopic<TYPE>(this, topicName, type);
    }

    public <TYPE> Topic<TYPE> createTopic(String topicName, Class<TYPE> type, TopicQos qos) {
        return new OSPLTopic<TYPE>(this, topicName, type, qos);
    }
    /**
     * This operation creates a Topic with the desired QoS policies and attaches
     * to it the specified TopicListener.
     * <p/>
     * The created Topic belongs to the DomainParticipant that is its factory.
     *
     * @param topicName
     *            The name of the new Topic.
     * @param type
     *            The type of all samples to be published and subscribed over
     *            the new Topic. The Service will attempt to locate an
     *            appropriate {@link org.omg.dds.type.TypeSupport} instance
     *            based on this type.
     * @param qos
     *            The desired QoS policies. If the specified QoS policies are
     *            not consistent, the operation will fail and no Publisher will
     *            be created.
     * @param listener
     *            The listener to be attached.
     * @param statuses
     *            Of which status changes the listener should be notified. A
     *            null collection signifies all status changes.
     */
    public <TYPE> Topic<TYPE> createTopic(String topicName, Class<TYPE> type,
                                    TopicQos qos, TopicListener<TYPE> listener,
                                    Collection<Class<? extends Status<?>>> statuses) {
        throw new RuntimeException("Not implemented");
    }


    /**
     * This operation creates a Topic with default QoS policies and no
     * TopicListener.
     * <p/>
     * The created Topic belongs to the DomainParticipant that is its factory.
     *
     * @param topicName
     *            The name of the new Topic.
     * @param type
     *            A {@link org.omg.dds.type.TypeSupport} representing the type
     *            of all samples to be published and subscribed over the new
     *            Topic.
     */
    public <TYPE> Topic<TYPE> createTopic(String topicName,
            TypeSupport<TYPE> type) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation creates a Topic with the desired QoS policies and attaches
     * to it the specified TopicListener.
     * <p/>
     * The created Topic belongs to the DomainParticipant that is its factory.
     *
     * @param topicName
     *            The name of the new Topic.
     * @param type
     *            A {@link org.omg.dds.type.TypeSupport} representing the type
     *            of all samples to be published and subscribed over the new
     *            Topic.
     * @param qos
     *            The desired QoS policies. If the specified QoS policies are
     *            not consistent, the operation will fail and no Publisher will
     *            be created.
     * @param listener
     *            The listener to be attached.
     * @param statuses
     *            Of which status changes the listener should be notified. A
     *            null collection signifies all status changes.
     */
    public <TYPE> Topic<TYPE> createTopic(String topicName,
            TypeSupport<TYPE> type, TopicQos qos, TopicListener<TYPE> listener,
            Collection<Class<? extends Status<?>>> statuses) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation gives access to an existing (or ready to exist) enabled
     * Topic, based on its name. The operation takes as arguments the name of
     * the Topic and a timeout.
     * <p/>
     * If a Topic of the same name already exists, it gives access to it,
     * otherwise it waits (blocks the caller) until another mechanism creates it
     * (or the specified timeout occurs). This other mechanism can be another
     * thread, a configuration tool, or some other middleware service. Note that
     * the Topic is a local object that acts as a 'proxy' to designate the
     * global concept of topic. Middleware implementations could choose to
     * propagate topics and make remotely created topics locally available.
     * <p/>
     * A Topic obtained by means of findTopic must also be closed by means of
     * {@link org.omg.dds.topic.Topic#close()} so that the local resources can
     * be released. If a Topic is obtained multiple times by means of findTopic
     * or {@link #createTopic(String, Class)}, it must also be closed that same
     * number of times.
     * <p/>
     * Regardless of whether the middleware chooses to propagate topics, the
     * {@link org.omg.dds.topic.Topic#close()} operation disposes of only the
     * local proxy.
     *
     * @return a non-null Topic.
     * @throws java.util.concurrent.TimeoutException
     *             if the specified timeout elapses and no suitable Topic could
     *             be found.
     * @see #findTopic(String, long, java.util.concurrent.TimeUnit)
     */
    public <TYPE> Topic<TYPE> findTopic(String topicName, Duration timeout)
            throws TimeoutException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation gives access to an existing (or ready to exist) enabled
     * Topic, based on its name. The operation takes as arguments the name of
     * the Topic and a timeout.
     * <p/>
     * If a Topic of the same name already exists, it gives access to it,
     * otherwise it waits (blocks the caller) until another mechanism creates it
     * (or the specified timeout occurs). This other mechanism can be another
     * thread, a configuration tool, or some other middleware service. Note that
     * the Topic is a local object that acts as a 'proxy' to designate the
     * global concept of topic. Middleware implementations could choose to
     * propagate topics and make remotely created topics locally available.
     * <p/>
     * A Topic obtained by means of findTopic must also be closed by means of
     * {@link org.omg.dds.topic.Topic#close()} so that the local resources can
     * be released. If a Topic is obtained multiple times by means of findTopic
     * or {@link #createTopic(String, Class)}, it must also be closed that same
     * number of times.
     * <p/>
     * Regardless of whether the middleware chooses to propagate topics, the
     * {@link org.omg.dds.topic.Topic#close()} operation disposes of only the
     * local proxy.
     *
     * @return a non-null Topic.
     * @throws java.util.concurrent.TimeoutException
     *             if the specified timeout elapses and no suitable Topic could
     *             be found.
     * @see #findTopic(String, org.omg.dds.core.Duration)
     */
    public <TYPE> Topic<TYPE> findTopic(String topicName, long timeout,
            TimeUnit unit) throws TimeoutException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation gives access to an existing locally-created
     * TopicDescription based on its name. The operation takes as argument the
     * name of the TopicDescription.
     * <p/>
     * If a TopicDescription of the same name already exists, it gives access to
     * it, otherwise it returns null. The operation never blocks.
     * <p/>
     * The operation may be used to locate any locally-created
     * {@link org.omg.dds.topic.Topic},
     * {@link org.omg.dds.topic.ContentFilteredTopic}, or
     * {@link org.omg.dds.topic.MultiTopic} object.
     * <p/>
     * Unlike {@link #findTopic(String, org.omg.dds.core.Duration)}, the
     * operation searches only among the locally created topics. Therefore, it
     * should never create a new TopicDescription. The TopicDescription returned
     * does not require an extra
     * {@link org.omg.dds.topic.TopicDescription#close()}. It is still possible
     * to close the TopicDescription returned by this method, provided it has no
     * readers or writers, but then it is really closed and subsequent lookups
     * will fail.
     * <p/>
     * If the operation fails to locate a TopicDescription, it returns null.
     *
     * @param <TYPE>
     *            The type of all samples subscribed to with the
     *            TopicDescription.
     * @param name
     *            The name of the TopicDescription to look up.
     */
    public <TYPE> TopicDescription<TYPE> lookupTopicDescription(String name) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation creates a ContentFilteredTopic. A ContentFilteredTopic can
     * be used to do content-based subscriptions.
     *
     * @param <TYPE>
     *            The type of all samples subscribed to with the new
     *            ContentFilteredTopic. It may be the same as the type of the
     *            relatedTopic or any supertype of that type.
     * @param name
     *            The name of the new ContentFilteredTopic.
     * @param relatedTopic
     *            The related Topic being subscribed to. The
     *            ContentFilteredTopic only relates to samples published under
     *            this Topic, filtered according to their content.
     * @param filterExpression
     *            A logical expression that involves the values of some of the
     *            data fields in the sample.
     * @param expressionParameters
     *            Parameters to the filterExpression.
     */
    public <TYPE> ContentFilteredTopic<TYPE> createContentFilteredTopic(
            String name, Topic<? extends TYPE> relatedTopic,
            String filterExpression, List<String> expressionParameters) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation creates a MultiTopic. A MultiTopic can be used to
     * subscribe to multiple topics and combine/filter the received data into a
     * resulting type. In particular, MultiTopic provides a content-based
     * subscription mechanism.
     *
     * @param <TYPE>
     *            The type of all samples subscribed to with the new MultiTopic.
     * @param name
     *            The name of the new MultiTopic.
     * @param typeName
     *            The resulting type. The Service will attempt to locate an
     *            appropriate {@link org.omg.dds.type.TypeSupport} instance
     *            based on this type.
     * @param subscriptionExpression
     *            The list of topics and the logic used to combine filter and
     *            re-arrange the information from each Topic.
     * @param expressionParameters
     *            Parameters to the filterExpression.
     */
    public <TYPE> MultiTopic<TYPE> createMultiTopic(String name,
            String typeName, String subscriptionExpression,
            List<String> expressionParameters) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation deletes all the entities that were created by means of the
     * "create" operations on the DomainParticipant. That is, it deletes all
     * contained {@link org.omg.dds.pub.Publisher},
     * {@link org.omg.dds.sub.Subscriber}, {@link org.omg.dds.topic.Topic},
     * {@link org.omg.dds.topic.ContentFilteredTopic}, and
     * {@link org.omg.dds.topic.MultiTopic} objects.
     * <p/>
     * Prior to deleting each contained entity, this operation will recursively
     * call the corresponding closeContainedEntities operation on each contained
     * entity (if applicable). This pattern is applied recursively. In this
     * manner the operation closeContainedEntities on the DomainParticipant will
     * end up deleting all the entities recursively contained in the
     * DomainParticipant, that is also the {@link org.omg.dds.pub.DataWriter},
     * {@link org.omg.dds.sub.DataReader}, as well as the
     * {@link org.omg.dds.sub.QueryCondition} and
     * {@link org.omg.dds.sub.ReadCondition} objects belonging to the contained
     * DataReaders.
     * <p/>
     * Once closeContainedEntities returns successfully, the application may
     * delete the DomainParticipant knowing that it has no contained entities.
     *
     * @throws org.omg.dds.core.PreconditionNotMetException
     *             if any of the contained entities is in a state where it
     *             cannot be closed.
     */
    public void closeContainedEntities() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation allows an application to instruct the Service to locally
     * ignore a remote domain participant. From that point onwards, the Service
     * will locally behave as if the remote participant did not exist. This
     * means it will ignore any {@link org.omg.dds.topic.Topic}, publication, or
     * subscription that originates on that domain participant.
     * <p/>
     * This operation can be used, in conjunction with the discovery of remote
     * participants offered by means of the "DCPSParticipant" built-in Topic, to
     * provide, for example, access control. Application data can be associated
     * with a DomainParticipant by means of the
     * {@link org.omg.dds.core.policy.UserData}. This application data is
     * propagated as a field in the built-in topic and can be used by an
     * application to implement its own access control policy.
     * <p/>
     * The domain participant to ignore is identified by the handle argument.
     * This handle is the one that appears in the {@link org.omg.dds.sub.Sample}
     * retrieved when reading the data samples available for the built-in
     * DataReader to the "DCPSParticipant" topic. The built-in
     * {@link org.omg.dds.sub.DataReader} is read with the same read/take
     * operations used for any DataReader.
     * <p/>
     * This operation is not required to be reversible. The Service offers no
     * means to reverse it.
     *
     * @throws org.omg.dds.core.OutOfResourcesException
     *             if the Service is unable to ignore the indicated participant
     *             because an internal resource has been exhausted.
     */
    public void ignoreParticipant(InstanceHandle handle) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation allows an application to instruct the Service to locally
     * ignore a {@link org.omg.dds.topic.Topic}. This means it will locally
     * ignore any publication or subscription to the Topic.
     * <p/>
     * This operation can be used to save local resources when the application
     * knows that it will never publish or subscribe to data under certain
     * topics.
     * <p/>
     * The Topic to ignore is identified by the handle argument. This handle is
     * the one that appears in the {@link org.omg.dds.sub.Sample} retrieved when
     * reading the data samples from the built-in
     * {@link org.omg.dds.sub.DataReader} to the "DCPSTopic" topic.
     * <p/>
     * This operation is not required to be reversible. The Service offers no
     * means to reverse it.
     *
     * @throws org.omg.dds.core.OutOfResourcesException
     *             if the Service is unable to ignore the indicated topic
     *             because an internal resource has been exhausted.
     */
    public void ignoreTopic(InstanceHandle handle) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation allows an application to instruct the Service to locally
     * ignore a remote publication; a publication is defined by the association
     * of a topic name, and user data and partition set on the
     * {@link org.omg.dds.pub.Publisher}. After this call, any data written
     * related to that publication will be ignored.
     * <p/>
     * The {@link org.omg.dds.pub.DataWriter} to ignore is identified by the
     * handle argument. This handle is the one that appears in the
     * {@link org.omg.dds.sub.Sample} retrieved when reading the data samples
     * from the built-in {@link org.omg.dds.sub.DataReader} to the
     * "DCPSPublication" topic.
     * <p/>
     * This operation is not required to be reversible. The Service offers no
     * means to reverse it.
     *
     * @throws org.omg.dds.core.OutOfResourcesException
     *             if the Service is unable to ignore the indicated publication
     *             because an internal resource has been exhausted.
     */
    public void ignorePublication(InstanceHandle handle) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation allows an application to instruct the Service to locally
     * ignore a remote subscription; a subscription is defined by the
     * association of a topic name, and user data and partition set on the
     * {@link org.omg.dds.sub.Subscriber}. After this call, any data received
     * related to that subscription will be ignored.
     * <p/>
     * The {@link org.omg.dds.sub.DataReader} to ignore is identified by the
     * handle argument. This handle is the one that appears in the
     * {@link org.omg.dds.sub.Sample} retrieved when reading the data samples
     * from the built-in DataReader to the "DCPSSubscription" topic.
     * <p/>
     * This operation is not required to be reversible. The Service offers no
     * means to reverse it.
     *
     * @throws org.omg.dds.core.OutOfResourcesException
     *             if the Service is unable to ignore the indicated subscription
     *             because an internal resource has been exhausted.
     */
    public void ignoreSubscription(InstanceHandle handle) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation retrieves the domain ID used to create the
     * DomainParticipant. The domain ID identifies the DDS domain to which the
     * DomainParticipant belongs. Each DDS domain represents a separate data
     * "communication plane" isolated from other domains.
     */
    public int getDomainId() {
        //TODO: Fixme and return the right id
        // return peer.get_domain_id();
        return 0;
    }

    /**
     * This operation manually asserts the liveliness of the DomainParticipant.
     * This is used in combination with the
     * {@link org.omg.dds.core.policy.Liveliness} to indicate to the Service
     * that the entity remains active.
     * <p/>
     * This operation needs to only be used if the DomainParticipant contains
     * {@link org.omg.dds.pub.DataWriter} entities with the
     * {@link org.omg.dds.core.policy.Liveliness#getKind()} set to
     * {@link org.omg.dds.core.policy.Liveliness.Kind#MANUAL_BY_PARTICIPANT} and
     * it only affects the liveliness of those DataWriter entities. Otherwise,
     * it has no effect.
     * <p/>
     * <b>Note</b> - Writing data via the
     * {@link org.omg.dds.pub.DataWriter#write(Object)} operation on a
     * DataWriter asserts liveliness on the DataWriter itself and its
     * DomainParticipant. Consequently the use of assertLiveliness is only
     * needed if the application is not writing data regularly.
     */
    public void assertLiveliness() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation retrieves the default value of the Publisher QoS, that is,
     * the QoS policies which will be used for newly created
     * {@link org.omg.dds.pub.Publisher} entities in the case where the QoS
     * policies are defaulted in the {@link #createPublisher()} operation.
     * <p/>
     * The values retrieved will match the set of values specified on the last
     * successful call to
     * {@link #setDefaultPublisherQos(org.omg.dds.pub.PublisherQos)}, or else,
     * if the call was never made, the default values identified by the DDS
     * specification.
     *
     * @see #setDefaultPublisherQos(org.omg.dds.pub.PublisherQos)
     */
    public PublisherQos getDefaultPublisherQos() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation sets a default value of the Publisher QoS policies, which
     * will be used for newly created {@link org.omg.dds.pub.Publisher} entities
     * in the case where the QoS policies are defaulted in the
     * {@link #createPublisher()} operation.
     *
     * @throws org.omg.dds.core.InconsistentPolicyException
     *             if the resulting policies are not self consistent; in that
     *             case, the operation will have no effect.
     * @see #getDefaultPublisherQos()
     */
    public void setDefaultPublisherQos(PublisherQos qos) {
        throw new RuntimeException("Not implemented");
    }


    /**
     * This operation retrieves the default value of the Subscriber QoS, that
     * is, the QoS policies which will be used for newly created
     * {@link org.omg.dds.sub.Subscriber} entities in the case where the QoS
     * policies are defaulted in the {@link #createSubscriber()} operation.
     * <p/>
     * The values retrieved will match the set of values specified on the last
     * successful call to
     * {@link #setDefaultSubscriberQos(org.omg.dds.sub.SubscriberQos)}, or else,
     * if the call was never made, the default values identified by the DDS
     * specification.
     *
     * @see #setDefaultSubscriberQos(org.omg.dds.sub.SubscriberQos)
     */
    public SubscriberQos getDefaultSubscriberQos() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation sets a default value of the Subscriber QoS policies that
     * will be used for newly created {@link org.omg.dds.sub.Subscriber}
     * entities in the case where the QoS policies are defaulted in the
     * {@link #createSubscriber()} operation.
     *
     * @throws org.omg.dds.core.InconsistentPolicyException
     *             if the resulting policies are not self consistent; in that
     *             case, the operation will have no effect.
     * @see #getDefaultSubscriberQos()
     */
    public void setDefaultSubscriberQos(SubscriberQos qos) {
        throw new RuntimeException("Not implemented");
    }


    /**
     * This operation retrieves the default value of the Topic QoS, that is, the
     * QoS policies which will be used for newly created
     * {@link org.omg.dds.topic.Topic} entities in the case where the QoS
     * policies are defaulted in the {@link #createTopic(String, Class)}
     * operation.
     * <p/>
     * The values retrieved will match the set of values specified on the last
     * successful call to
     * {@link #setDefaultTopicQos(org.omg.dds.topic.TopicQos)}, or else, if the
     * call was never made, the default values identified by the DDS
     * specification.
     *
     * @see #setDefaultTopicQos(org.omg.dds.topic.TopicQos)
     */
    public TopicQos getDefaultTopicQos() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation sets a default value of the Topic QoS policies, which will
     * be used for newly created {@link org.omg.dds.topic.Topic} entities in the
     * case where the QoS policies are defaulted in the
     * {@link #createTopic(String, Class)} operation.
     *
     * @throws org.omg.dds.core.InconsistentPolicyException
     *             if the resulting policies are not self consistent; in that
     *             case, the operation will have no effect.
     * @see #getDefaultTopicQos()
     */
    public void setDefaultTopicQos(TopicQos qos) {
        throw new RuntimeException("Not implemented");
    }



    /**
     * This operation retrieves the list of DomainParticipants that have been
     * discovered in the domain and that the application has not indicated
     * should be "ignored" by means of the
     * {@link #ignoreParticipant(org.omg.dds.core.InstanceHandle)} operation.
     *
     * @param participantHandles
     *            a container, into which this method will place handles to the
     *            discovered participants.
     * @return participantHandles, as a convenience to facilitate chaining.
     * @throws UnsupportedOperationException
     *             If the infrastructure does not locally maintain the
     *             connectivity information.
     */
    public Collection<InstanceHandle> getDiscoveredParticipants(
            Collection<InstanceHandle> participantHandles) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation retrieves information on a DomainParticipant that has been
     * discovered on the network. The participant must be in the same domain as
     * the participant on which this operation is invoked and must not have been
     * "ignored" by means of the
     * {@link #ignoreParticipant(org.omg.dds.core.InstanceHandle)} operation.
     * <p/>
     * Use the operation
     * {@link #getDiscoveredParticipants(java.util.Collection)} to find the
     * DomainParticipants that are currently discovered.
     *
     * @param participantData
     *            a container, into which this method will store the participant
     *            data.
     * @param participantHandle
     *            a handle to the participant, the data of which is to be
     *            retrieved.
     * @return participantData, as a convenience to facilitate chaining.
     * @throws org.omg.dds.core.PreconditionNotMetException
     *             if the participantHandle does not correspond to a
     *             DomainParticipant such as is described above.
     * @throws UnsupportedOperationException
     *             If the infrastructure does not locally maintain the
     *             connectivity information.
     */
    public ParticipantBuiltinTopicData getDiscoveredParticipantData(
            ParticipantBuiltinTopicData participantData,
            InstanceHandle participantHandle) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation retrieves the list of {@link org.omg.dds.topic.Topic}s
     * that have been discovered in the domain and that the application has not
     * indicated should be "ignored" by means of the
     * {@link #ignoreTopic(org.omg.dds.core.InstanceHandle)} operation.
     *
     * @param topicHandles
     *            a container, into which this method will place handles to the
     *            discovered topics.
     * @return topicHandles, as a convenience to facilitate chaining.
     * @throws UnsupportedOperationException
     *             If the infrastructure does not locally maintain the
     *             connectivity information.
     */
    public Collection<InstanceHandle> getDiscoveredTopics(
            Collection<InstanceHandle> topicHandles) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation retrieves information on a {@link org.omg.dds.topic.Topic}
     * that has been discovered on the network. The topic must be in the same
     * domain as the participant on which this operation is invoked and must not
     * have been "ignored" by means of the
     * {@link #ignoreTopic(org.omg.dds.core.InstanceHandle)} operation.
     * <p/>
     * Use the operation {@link #getDiscoveredTopics(java.util.Collection)} to
     * find the Topics that are currently discovered.
     *
     * @param topicData
     *            a container, into which this method will store the participant
     *            data.
     * @param topicHandle
     *            a handle to the topic, the data of which is to be retrieved.
     * @return topicData, as a convenience to facilitate chaining.
     * @throws org.omg.dds.core.PreconditionNotMetException
     *             if the topicHandle does not correspond to a Topic such as is
     *             described above.
     * @throws UnsupportedOperationException
     *             If the infrastructure does not locally maintain the
     *             connectivity information or if the infrastructure does not
     *             hold the information necessary to fill in the topicData.
     */
    public TopicBuiltinTopicData getDiscoveredTopicData(
            TopicBuiltinTopicData topicData, InstanceHandle topicHandle) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation checks whether or not the given handle represents an
     * {@link org.omg.dds.core.Entity} that was created from the
     * DomainParticipant. The containment applies recursively. That is, it
     * applies both to entities ({@link org.omg.dds.topic.TopicDescription},
     * {@link org.omg.dds.pub.Publisher}, or {@link org.omg.dds.sub.Subscriber})
     * created directly using the DomainParticipant as well as entities created
     * using a contained Publisher or Subscriber as the factory, and so forth.
     * <p/>
     * The instance handle for an Entity may be obtained from built-in topic
     * data, from various statuses, or from the Entity operation
     * {@link org.omg.dds.core.Entity#getInstanceHandle()}.
     */
    public boolean containsEntity(InstanceHandle handle) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation returns the current value of the time that the service
     * uses to time stamp data writes and to set the reception time stamp for
     * the data updates it receives.
     *
     * @param currentTime
     *            a container for the current time, which the Service will
     *            overwrite with the result of this operation.
     * @return currentTime, as a convenience to facilitate chaining.
     */
    public Time getCurrentTime(Time currentTime) {
        Time_tHolder holder = new Time_tHolder();
        peer.get_current_time(holder);
        return PolicyConverter.convert(holder.value);
    }

    public void setListener(DomainParticipantListener listener) {
        this.dlistener = listener;
    }

    public DomainParticipantListener getListener() {
        return this.dlistener;
    }

    /**
     * This operation enables the Entity. Entity objects can be created either
     * enabled or disabled. This is controlled by the value of the
     * {@link org.omg.dds.core.policy.EntityFactory} on the corresponding
     * factory for the Entity.
     * <p/>
     * The default setting of {@link org.omg.dds.core.policy.EntityFactory} is
     * such that, by default, it is not necessary to explicitly call enable on
     * newly created entities.
     * <p/>
     * The enable operation is idempotent. Calling enable on an already enabled
     * Entity has no effect.
     * <p/>
     * If an Entity has not yet been enabled, the following kinds of operations
     * may be invoked on it:
     * <p/>
     * <ul>
     * <li>Operations to set or get an Entity's QoS policies (including default
     * QoS policies) and listener</li>
     * <li>{@link #getStatusCondition()}</li>
     * <li>'factory' operations and {@link #close()}</li>
     * <li>{@link #getStatusChanges(java.util.Collection)} and other get status
     * operations (although the status of a disabled entity never changes)</li>
     * <li>'lookup' operations</li>
     * </ul>
     * <p/>
     * Other operations may explicitly state that they may be called on disabled
     * entities; those that do not will fail with
     * {@link org.omg.dds.core.NotEnabledException}.
     * <p/>
     * It is legal to delete an Entity that has not been enabled by calling
     * {@link #close()}. Entities created from a factory that is disabled are
     * created disabled regardless of the setting of
     * {@link org.omg.dds.core.policy.EntityFactory}.
     * <p/>
     * Calling enable on an Entity whose factory is not enabled will fail with
     * {@link org.omg.dds.core.PreconditionNotMetException}.
     * <p/>
     * If
     * {@link org.omg.dds.core.policy.EntityFactory}
     * is set to auto-enable entities, the enable operation on the factory will automatically enable
     * all entities created from the factory.
     * <p/>
     * The Listeners associated with an entity are not called until the entity
     * is enabled. {@link org.omg.dds.core.Condition}s associated with an entity
     * that is not enabled are "inactive," that is, have a triggerValue ==
     * false.
     */
    public void enable() {
        peer.enable();
    }

    /**
     * This operation allows access to the
     * {@link org.omg.dds.core.StatusCondition} associated with the Entity. The
     * returned condition can then be added to a
     * {@link org.omg.dds.core.WaitSet} so that the application can wait for
     * specific status changes that affect the Entity.
     */
    public StatusCondition<DomainParticipant> getStatusCondition() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * This operation retrieves the list of communication statuses in the Entity
     * that are 'triggered.' That is, the list of statuses whose value has
     * changed since the last time the application read the status.
     * <p/>
     * When the entity is first created or if the entity is not enabled, all
     * communication statuses are in the "untriggered" state so the list
     * returned will be empty.
     * <p/>
     * The list of statuses returned refers to the statuses that are triggered
     * on the Entity itself and does not include statuses that apply to
     * contained entities.
     *
     * @param statuses
     *            a container for the resulting statuses; its contents will be
     *            overwritten by the result of this operation.
     * @return the argument as a convenience in order to facilitate call
     *         chaining.
     */
    public Collection<Class<? extends Status<?>>> getStatusChanges(
            Collection<Class<? extends Status<?>>> statuses) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @return the {@link org.omg.dds.core.InstanceHandle} that represents the
     *         Entity.
     */
    public InstanceHandle getInstanceHandle() {
        return new OSPLInstanceHandle(
                peer.get_instance_handle());
    }

    /**
     * Halt communication and dispose the resources held by this Entity.
     * <p/>
     * Closing an Entity implicitly closes all of its contained objects, if any.
     * For example, closing a Publisher also closes all of its contained
     * DataWriters.
     * <p/>
     * An Entity cannot be closed if it has any unclosed dependent objects, not
     * including contained objects. These include the following:
     * <p/>
     * <ul>
     * <li>A {@link org.omg.dds.topic.Topic} cannot be closed if it is still in
     * use by any {@link org.omg.dds.topic.ContentFilteredTopic}s or
     * {@link org.omg.dds.topic.MultiTopic}s.</li>
     * <li>A Topic cannot be closed if any {@link org.omg.dds.pub.DataWriter}s
     * or {@link org.omg.dds.sub.DataReader} is still using it.</li>
     * <li>A DataReader cannot be closed if it has any outstanding loans as a
     * result of a call to {@link org.omg.dds.sub.DataReader#read()},
     * {@link org.omg.dds.sub.DataReader#take()}, or one of the variants
     * thereof.</li>
     * </ul>
     * <p/>
     * The deletion of a {@link org.omg.dds.pub.DataWriter} will automatically
     * unregister all instances. Depending on the settings of the
     * {@link org.omg.dds.core.policy.WriterDataLifecycle}, the deletion of the
     * DataWriter may also dispose all instances.
     * 
     * @throws org.omg.dds.core.PreconditionNotMetException
     *             if close is called on an Entity with unclosed dependent
     *             object(s), not including contained objects.
     * @see org.omg.dds.topic.TopicDescription#close()
     */
    public void close() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Indicates that references to this object may go out of scope but that the
     * application expects to look it up again later. Therefore, the Service
     * must consider this object to be still in use and may not close it
     * automatically.
     */
    public void retain() {
        throw new RuntimeException("Not implemented");
    }
}
