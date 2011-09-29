package org.opensplice.psm.java.core.policy;


import org.omg.dds.core.Duration;
import org.omg.dds.core.policy.ReaderDataLifecycle;
import org.omg.dds.core.policy.TimeBasedFilter;
import org.omg.dds.core.policy.UserData;
import org.omg.dds.runtime.DataReaderPolicyProvider;

public class OSPLDataReaderPolicyProvider extends OSPLTopicPolicyProvider implements DataReaderPolicyProvider {

    private UserData userData = new UserData();
    private TimeBasedFilter timeBasedFilter = new TimeBasedFilter(Duration.infinite());
    private ReaderDataLifecycle readerDataLifecycle = new ReaderDataLifecycle(Duration.zero(), Duration.zero());

    public UserData getUserData() {
        return this.userData;
    }

    public TimeBasedFilter getTimeBasedFilter() {
        return this.timeBasedFilter;
    }

    public ReaderDataLifecycle getReaderDataLifecycle() {
        return this.readerDataLifecycle;
    }
}
