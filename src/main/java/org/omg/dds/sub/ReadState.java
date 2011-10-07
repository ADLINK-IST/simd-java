package org.omg.dds.sub;

/**
 * This class encapsulates the specific state considered by a read
 * with respect to the Samples, Instances and Views state.
 */
public class ReadState {
    public final SampleState   sampleState;
    public final InstanceState instanceState;
    public final ViewState     viewState;

    // Commonly Used Read Status

    private static final ReadState ANY =
            new ReadState(SampleState.ANY,
                    InstanceState.ANY,
                    ViewState.ANY);

    private static final ReadState NEW_DATA =
            new ReadState(SampleState.NOT_READ,
                    InstanceState.ALIVE,
                    ViewState.ANY);

    private static final ReadState ALL_DATA =
            new ReadState(SampleState.ANY,
                    InstanceState.ALIVE,
                    ViewState.ANY);
    private static final ReadState NEW_INSTANCE =
            new ReadState(SampleState.ANY,
                    InstanceState.ALIVE,
                    ViewState.NEW);

    public ReadState(SampleState s, InstanceState i, ViewState v) {
        this.sampleState = s;
        this.instanceState = i;
        this.viewState = v;
    }
    public ReadState(SampleState s) {
        this.sampleState = s;
        this.instanceState = InstanceState.ANY;
        this.viewState = ViewState.ANY;
    }

    public ReadState(InstanceState i) {
        this.sampleState = SampleState.ANY;
        this.instanceState = i;
        this.viewState = ViewState.ANY;
    }

    public ReadState(ViewState v) {
        this.sampleState = SampleState.ANY;
        this.instanceState = InstanceState.ANY;
        this.viewState = v;
    }

    public static final ReadState Any() {
        return ReadState.ANY;
    }

    public static final ReadState NewData() {
        return ReadState.NEW_DATA;
    }

    public static final ReadState AllData() {
        return ReadState.ALL_DATA;
    }

    public static final ReadState NewInstance() {
        return ReadState.NEW_INSTANCE;
    }
}
