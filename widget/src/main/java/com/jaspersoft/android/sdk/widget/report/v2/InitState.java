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
    }

    @Override
    public void update(List<ReportParameter> parameters) {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void navigate(ReportQuery query) {

    }

    @Override
    public void refresh() {

    }

    @Subscribe
    public void onEngineTransitEvent(TransitToEngineEvent engineEvent) {
        double code = engineEvent.getCode();
        RunOptions runOptions = engineEvent.getOptions();

        PresenterState nextState = getContext().provideStateFactory()
                .createEngineInitState(code);
        swapState(nextState);
        nextState.run(runOptions);
    }
}
