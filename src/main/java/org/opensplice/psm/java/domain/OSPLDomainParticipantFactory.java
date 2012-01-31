package org.opensplice.psm.java.domain;

import java.lang.reflect.Method;
import java.util.Collection;

import org.omg.dds.core.DDSException;
import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.domain.DomainParticipantFactoryQos;
import org.omg.dds.domain.DomainParticipantListener;
import org.omg.dds.domain.DomainParticipantQos;



public class OSPLDomainParticipantFactory extends
        org.omg.dds.domain.DomainParticipantFactory {

    // Note: The DOMAIN_ID_DEFAULT does not exist anymore on OpenSplice >= v5.7
    private static int myDomain = 0;

    // Holds the right method for creating the participant (e.g. v5 vs. v6 API)
    private Method createParticipantMethod;
    private boolean intDomainID = true;

    public OSPLDomainParticipantFactory() {
        Class cls = DDS.DomainParticipantFactory.class;
        this.createParticipantMethod = null;

        /*
        for (Method m: cls.getMethods()) {
            if (m.getName() == "create_participant") {
                this.createParticipantMethod = m;
            }
        }
        */

        try {

            this.createParticipantMethod =
                    cls.getMethod("create_participant",
                            Integer.TYPE,
                            DDS.DomainParticipantQos.class,
                            DDS.DomainParticipantListener.class,
                            Integer.TYPE);
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
            try {
                this.createParticipantMethod =
                        cls.getMethod("create_participant",
                                String.class,
                                DDS.DomainParticipantQos.class,
                                DDS.DomainParticipantListener.class,
                                Integer.TYPE);
                this.intDomainID = false;
            } catch (NoSuchMethodException e2) {
                System.err.println(">> Cannot find method for creating domain participant");
                System.exit(-1);
            }


        }


    }
    /**
     * Create a new participant in the domain with ID 0 having default QoS and
     * no listener.
     *
     * @see #createParticipant(int)
     * @see #createParticipant(int, org.omg.dds.domain.DomainParticipantQos,
     *      org.omg.dds.domain.DomainParticipantListener, java.util.Collection)
     */
    @Override
    public DomainParticipant createParticipant() {
        return createParticipant(myDomain);
    }

    /**
     * This operation creates a new DomainParticipant object. The
     * DomainParticipant signifies that the calling application intends to join
     * the domain identified by the domainId argument.
     *
     * @see #createParticipant()
     * @see #createParticipant(int, org.omg.dds.domain.DomainParticipantQos,
     *      org.omg.dds.domain.DomainParticipantListener, java.util.Collection)
     */
    @Override
    public DomainParticipant createParticipant(int domainId) {
        DDS.DomainParticipantQosHolder dqos = new DDS.DomainParticipantQosHolder();
        DDS.DomainParticipantFactory.get_instance()
                .get_default_participant_qos(dqos);

        DDS.DomainParticipantFactory dpf =
                DDS.DomainParticipantFactory.get_instance();

        DDS.DomainParticipant participant = null;
        try {
            if (intDomainID) {
            participant =
                    (DDS.DomainParticipant )this.createParticipantMethod.invoke(dpf,
                            new Integer(domainId),
                            dqos.value,
                            null,
                            new Integer(DDS.STATUS_MASK_NONE.value));
            }
            else {
                participant =
                    (DDS.DomainParticipant )this.createParticipantMethod.invoke(dpf,
                            "",
                            dqos.value,
                            null,
                            new Integer(DDS.STATUS_MASK_NONE.value));
            }
        } catch (Exception e) { }

        assert (participant != null);

        return new OSPLDomainParticipant(participant);
    }
        /**
         * This operation creates a new DomainParticipant object having default QoS
         * and no listener. The DomainParticipant signifies that the calling
         * application intends to join the domain identified by the domainId
         * argument.
         *
         * @param statuses
         *            Of which status changes the listener should be notified. A
         *            null collection signifies all status changes.
         * @throws org.omg.dds.core.InconsistentPolicyException
         *             if the specified QoS policies are not consistent.
         * @see #createParticipant()
         * @see #createParticipant(int)
         */
        @Override
        public DomainParticipant createParticipant(int domainId,
        DomainParticipantQos qos, DomainParticipantListener listener,
                Collection<Class<? extends Status<?>>> statuses) {
            return null; // To change body of implemented methods use File |
            // Settings | File Templates.
        }

        /**
         * Create a new domain participant.
         *
         * @param statuses
         *            Of which status changes the listener should be notified. A
         *            null collection signifies all status changes.
         * @see #createParticipant(int, org.omg.dds.domain.DomainParticipantQos,
         *      org.omg.dds.domain.DomainParticipantListener, java.util.Collection)
         */
        @Override
        public DomainParticipant createParticipant(int domainId,
        String qosLibraryName, String qosProfileName,
                DomainParticipantListener listener,
                Collection<Class<? extends Status<?>>> statuses) {
            return null; // To change body of implemented methods use File |
            // Settings | File Templates.
        }

        /**
         * This operation retrieves a previously created DomainParticipant belonging
         * to specified domain ID. If no such DomainParticipant exists, the
         * operation will return null.
         * <p/>
         * If multiple DomainParticipant entities belonging to that domain ID exist,
         * then the operation will return one of them. It is not specified which
         * one.
         */
        @Override
        public DomainParticipant lookupParticipant(int domainId) {
            return null; // To change body of implemented methods use File |
            // Settings | File Templates.
        }

        /**
         * This operation returns the value of the OSPLDomainParticipantFactory QoS
         * policies.
         *
         * @see #setQos(org.omg.dds.domain.DomainParticipantFactoryQos)
         */
        @Override
        public DomainParticipantFactoryQos getQos() {
            return null; // To change body of implemented methods use File |
            // Settings | File Templates.
        }

        /**
         * This operation sets the value of the OSPLDomainParticipantFactory QoS
         * policies. These policies control the behavior of the object, a factory
         * for entities.
         * <p/>
         * Note that despite having QoS, the OSPLDomainParticipantFactory is not an
         * {@link org.omg.dds.core.Entity}.
         *
         * @throws org.omg.dds.core.InconsistentPolicyException
         *             if the resulting policies are not self consistent; in that
         *             case, the operation will have no effect.
         * @see #getQos()
         */
        @Override
        public void setQos(DomainParticipantFactoryQos qos) {
            // To change body of implemented methods use File | Settings | File
            // Templates.
        }

        /**
         * This operation retrieves the default value of the DomainParticipant QoS,
         * that is, the QoS policies which will be used for newly created
         * {@link org.omg.dds.domain.DomainParticipant} entities in the case where
         * the QoS policies are defaulted in the {@link #createParticipant()}
         * operation.
         * <p/>
         * The values retrieved will match the set of values specified on the last
         * successful call to
         * {@link #setDefaultParticipantQos(org.omg.dds.domain.DomainParticipantQos)}
         * , or else, if the call was never made, the default values identified by
         * the DDS specification.
         *
         * @see #setDefaultParticipantQos(org.omg.dds.domain.DomainParticipantQos)
         */
        @Override
        public DomainParticipantQos getDefaultParticipantQos() {
            return null; // To change body of implemented methods use File |
            // Settings | File Templates.
        }

        /**
         * This operation sets a default value of the DomainParticipant QoS policies
         * which will be used for newly created
         * {@link org.omg.dds.domain.DomainParticipant} entities in the case where
         * the QoS policies are defaulted in the {@link #createParticipant()}
         * operation.
         *
         * @throws org.omg.dds.core.InconsistentPolicyException
         *             if the resulting policies are not self consistent; in that
         *             case, the operation will have no effect.
         * @see #getDefaultParticipantQos()
         */
        @Override
        public void setDefaultParticipantQos(DomainParticipantQos qos) {
            // To change body of implemented methods use File | Settings | File
            // Templates.
        }

    }
