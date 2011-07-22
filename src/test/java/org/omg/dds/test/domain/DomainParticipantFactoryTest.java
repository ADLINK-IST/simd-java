package org.omg.dds.test.domain;

import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.domain.DomainParticipantFactory;
import org.junit.Test;

public class DomainParticipantFactoryTest {
    @Test
    public void testResolveParticipantFactory() {
        DomainParticipantFactory dpf = DomainParticipantFactory.getInstance();
        assert (dpf != null);
    }

    @Test
    public void testCreateDomainParticipant() {
        DomainParticipantFactory dpf = DomainParticipantFactory.getInstance();
        DomainParticipant dp = dpf.createParticipant();
        assert (dp != null);
    }

}
