package com.jaspersoft.android.sdk.widget.report.v2;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.internal.Dispatcher;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class InitState extends PresenterState {
    protected InitState(Context context, Dispatcher dispatcher) {
        super(context, dispatcher);
    }

    @Override
    public void run(RunOptions options) {
        executeCommand(
                getContext().provideCommandFactory()
                        .createEngineInitCommand(options)
        );
        dispatchProgress(0);
    }

    @Override
    public void update(List<ReportParameter> parameters) {
        throw new IllegalStateException("Could not update presenter. Presenter still not initialized.");
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void navigate(ReportQuery query) {
        throw new IllegalStateException("Could not navigate in presenter. Presenter still not initialized.");
    }

    @Override
    public void refresh() {
        throw new IllegalStateException("Could not refresh in presenter. Presenter still not initialized.");
    }

    @Subscribe
    public void onEngineTransitEvent(TransitToEngineSystemEvent engineEvent) {
        double code = engineEvent.getCode();
        RunOptions runOptions = engineEvent.getOptions();

        PresenterState nextState = getContext().provideStateFactory()
                .createEngineInitState(code);
        swapState(nextState);
        nextState.run(runOptions);
    }
}
