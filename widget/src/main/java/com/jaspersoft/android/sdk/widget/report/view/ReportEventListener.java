package com.jaspersoft.android.sdk.widget.report.view;

import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface ReportEventListener {
    void onActionsAvailabilityChanged(boolean isAvailable);
    void onHyperlinkClicked(Hyperlink hyperlink);
    void onExternalLinkOpened(String url);
    void onError(ServiceException exception);
    void onTimeoutWarning();

    class SimpleReportEventListener implements ReportEventListener {
        @Override
        public void onActionsAvailabilityChanged(boolean isAvailable) {

        }

        @Override
        public void onHyperlinkClicked(Hyperlink hyperlink) {

        }

        @Override
        public void onExternalLinkOpened(String url) {

        }

        @Override
        public void onError(ServiceException exception) {

        }

        @Override
        public void onTimeoutWarning() {

        }
    }
}
