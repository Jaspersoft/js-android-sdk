package com.jaspersoft.android.sdk.widget.report.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.widget.ResourceWebView;
import com.jaspersoft.android.sdk.widget.ResourceWebViewStore;
import com.jaspersoft.android.sdk.widget.report.renderer.RenderState;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportRenderer;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportRendererCallback;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportView extends RelativeLayout {
    private ResourceWebView resourceWebView;
    private ReportRenderer reportRenderer;
    private ReportViewEventListener reportViewEventListener;

    private float scale;
    private RunOptions pendingRunOptions;

    public ReportView(Context context) {
        super(context);
    }

    public ReportView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReportView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ReportView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(AuthorizedClient client, ServerInfo serverInfo) {
        init(client, serverInfo, 1f);
    }

    public void init(AuthorizedClient client, ServerInfo serverInfo, float scale) {
        reportRenderer = ReportRenderer.create(client, resourceWebView, serverInfo);
        this.scale = scale;
    }

    public void setReportViewEventListener(ReportViewEventListener reportViewEventListener) {
        this.reportViewEventListener = reportViewEventListener;
    }

    public void destroy() {
        if (reportRenderer != null) {
            reportRenderer.destroy();
        }
    }

    public void run(RunOptions runOptions) {
        pendingRunOptions = runOptions;
        switch (reportRenderer.getRenderState()) {
            case IDLE:
                reportRenderer.init(scale);
                break;
            case INITED:
                reportRenderer.reset();
                break;
            case RENDERED:
                reportRenderer.reset();
                break;
            case DESTROYED:
                reportRenderer.render(runOptions);
                break;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        create();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (reportRenderer == null) {
            throw new RuntimeException("Report view must be inited before attached to window!");
        }

        reportRenderer.registerReportRendererCallback(new RendererEventListener());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (reportRenderer != null) {
            reportRenderer.unregisterReportRendererCallback();
        }
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        reportRenderer = RenderersStore.INSTANCE.restoreExecutor(ss.reportRendererKey);
        resourceWebView = ResourceWebViewStore.getInstance().getResourceWebView();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parentState = super.onSaveInstanceState();
        SavedState ss = new SavedState(parentState);

        ss.reportRendererKey = RenderersStore.INSTANCE.saveExecutor(reportRenderer);

        ResourceWebViewStore.getInstance().setWebView(resourceWebView);
        removeView(resourceWebView);

        return parentState;
    }

    private void create() {
        resourceWebView = new ResourceWebView(getContext().getApplicationContext());
        addView(resourceWebView);
    }

    private class RendererEventListener implements ReportRendererCallback {
        @Override
        public void onProgressStateChange(boolean inProgress) {
            resourceWebView.setVisibility(inProgress ? View.GONE : View.VISIBLE);
        }

        @Override
        public void onRenderStateChanged(RenderState renderState) {
            switch (renderState) {
                case INITED:
                    if (pendingRunOptions != null) {
                        reportRenderer.render(pendingRunOptions);
                        pendingRunOptions = null;
                    }
                    break;
            }
        }

        @Override
        public void onHyperlinkClicked(Hyperlink hyperlink) {
            if (reportViewEventListener != null) {
                reportViewEventListener.onHyperlinkClicked(hyperlink);
            }
        }

        @Override
        public void onError(ServiceException exception) {
            if (reportViewEventListener != null) {
                reportViewEventListener.onError(exception);
            }
        }
    }

    static class SavedState extends BaseSavedState {
        ReportRendererKey reportRendererKey;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.reportRendererKey = in.readParcelable(ReportRendererKey.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeParcelable(this.reportRendererKey, 0);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
