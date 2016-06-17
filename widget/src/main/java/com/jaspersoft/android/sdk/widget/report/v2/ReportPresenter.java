package com.jaspersoft.android.sdk.widget.report.v2;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.widget.WindowError;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ReportPresenter {
    private final ReportClient reportClient;
    private final PresenterKey key;
    private final PresenterState.Context context;

    public ReportPresenter(
            ReportClient reportClient,
            PresenterKey key,
            PresenterState.Context context
    ) {
        this.reportClient = reportClient;
        this.key = key;
        this.context = context;
    }

    public PresenterKey getKey() {
        return key;
    }

    public ReportPresenter registerProgressListener(ProgressListener progressListener) {
        context.getListeners().setProgressListener(progressListener);
        return this;
    }

    public ReportPresenter registerHyperlinkClickListener(HyperlinkClickListener hyperlinkClickListener) {
        context.getListeners().setHyperlinkClickListener(hyperlinkClickListener);
        return this;
    }

    public ReportPresenter registerErrorListener(ErrorListener errorListener) {
        context.getListeners().setErrorListener(errorListener);
        return this;
    }

    public void run(RunOptions options) {
        context.getCurrentState().run(options);
    }

    public void update(List<ReportParameter> parameters) {
        context.getCurrentState().update(parameters);
    }

    public boolean isRunning() {
        return context.getCurrentState().isRunning();
    }

    public void navigate(ReportQuery query) {
        context.getCurrentState().navigate(query);
    }

    public void refresh() {
        context.getCurrentState().refresh();
    }

    public void resume() {
        context.getCurrentState().resume();
    }

    public void pause() {
        context.getCurrentState().pause();
    }

    public void removeCallbacks() {
        context.getListeners().reset();
    }

    public void destroy() {
        reportClient.removePresenter(this);
        context.getCurrentState().destroy();
    }

    public interface ProgressListener {
        void onProgressChanged(int newProgress);
    }

    public interface HyperlinkClickListener {
    }

    public interface ErrorListener {
        void onSdkError(ServiceException exception);

        void onWebWindowError(WindowError windowError);
    }

    public interface PropertyCallback<P> {
        void onResult(P property);
    }
}
