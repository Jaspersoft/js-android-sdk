package com.jaspersoft.android.sdk.widget.report.v2;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.WindowError;
import com.jaspersoft.android.sdk.widget.internal.Dispatcher;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class RestInitState extends PresenterState{
    protected RestInitState(Context context, Dispatcher dispatcher) {
        super(context, dispatcher);
    }

    @Override
    public void run(RunOptions options) {
        executeCommand(
                getContext().provideCommandFactory()
                        .createLoadRestTemplateCommand(options)
        );
        dispatchProgress(33);
    }

    @Override
    public void update(List<ReportParameter> parameters) {
        throw new IllegalStateException("Could not update presenter. Presenter is not running.");
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void navigate(ReportQuery query) {
        throw new IllegalStateException("Could not navigate presenter. Presenter is not running.");
    }

    @Override
    public void refresh() {
        throw new IllegalStateException("Could not refresh presenter. Presenter is not running.");
    }

    @Subscribe
    public void onInflateComplete(InflateCompleteSystemEvent event) {
        dispatchProgress(66);
    }

    @Subscribe
    public void onJavascriptEvent(RestJavascriptEvent event) {
        RestJavascriptEvent.Type type = event.getType();
        switch (type) {
            case WINDOW_ERROR:
                dispatchWindowError(event.firstArg(WindowError.class));
                break;
        }
    }
}
