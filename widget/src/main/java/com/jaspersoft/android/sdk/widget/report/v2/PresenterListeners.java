package com.jaspersoft.android.sdk.widget.report.v2;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class PresenterListeners {

    private ReportPresenter.ProgressListener progressListener = SimpleProgressListener.NULL;
    private ReportPresenter.HyperlinkClickListener hyperlinkClickListener = SimpleHyperlinkClickListener.NULL;
    private ReportPresenter.ErrorListener errorListener = SimpleErrorListener.NULL;

    public ReportPresenter.ProgressListener getProgressListener() {
        return progressListener;
    }

    public ReportPresenter.HyperlinkClickListener getHyperlinkClickListener() {
        return hyperlinkClickListener;
    }

    public ReportPresenter.ErrorListener getErrorListener() {
        return errorListener;
    }

    public void setProgressListener(ReportPresenter.ProgressListener progressListener) {
        if (progressListener == null) {
            progressListener = SimpleProgressListener.NULL;
        }
        this.progressListener = progressListener;
    }

    public void setHyperlinkClickListener(ReportPresenter.HyperlinkClickListener hyperlinkClickListener) {
        if (hyperlinkClickListener == null) {
            hyperlinkClickListener = SimpleHyperlinkClickListener.NULL;
        }
        this.hyperlinkClickListener = hyperlinkClickListener;
    }

    public void setErrorListener(ReportPresenter.ErrorListener errorListener) {
        if (errorListener == null) {
            errorListener = SimpleErrorListener.NULL;
        }
        this.errorListener = errorListener;
    }

    public void reset() {
        progressListener = SimpleProgressListener.NULL;
        hyperlinkClickListener = SimpleHyperlinkClickListener.NULL;
        errorListener = SimpleErrorListener.NULL;
    }

    private static class SimpleProgressListener implements ReportPresenter.ProgressListener {
        private static final ReportPresenter.ProgressListener NULL = new SimpleProgressListener();
    }

    private static class SimpleHyperlinkClickListener implements ReportPresenter.HyperlinkClickListener {
        private static final ReportPresenter.HyperlinkClickListener NULL = new SimpleHyperlinkClickListener();
    }

    private static class SimpleErrorListener implements ReportPresenter.ErrorListener {
        private static final ReportPresenter.ErrorListener NULL = new SimpleErrorListener();
    }
}
