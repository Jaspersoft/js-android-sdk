package com.jaspersoft.android.sdk.widget.report;

import com.jaspersoft.android.sdk.service.exception.ServiceException;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface ReportRendererCallback {
    void onProgressStateChange(boolean inProgress);
    void onRenderStateChanged(RenderState renderState);
    void onError(ServiceException exception);

    class SimpleReportRendererCallback implements ReportRendererCallback{

        @Override
        public void onProgressStateChange(boolean inProgress) {

        }

        @Override
        public void onRenderStateChanged(RenderState renderState) {

        }

        @Override
        public void onError(ServiceException exception) {

        }
    }
}
