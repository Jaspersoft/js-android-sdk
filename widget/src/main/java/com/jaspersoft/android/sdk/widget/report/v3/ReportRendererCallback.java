package com.jaspersoft.android.sdk.widget.report.v3;

import com.jaspersoft.android.sdk.service.exception.ServiceException;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface ReportRendererCallback {
    void onRenderStateChanged(RenderState renderState);
    void onError(ServiceException exception);

    class SimpleReportRendererCallback implements ReportRendererCallback{
        @Override
        public void onRenderStateChanged(RenderState renderState) {

        }

        @Override
        public void onError(ServiceException exception) {

        }
    }
}
