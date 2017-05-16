package com.jaspersoft.android.sdk.widget.report.view;

import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface ReportEventListener {

    public enum ActionType {
        ACTION_TYPE_ALL,
        ACTION_TYPE_CHANGE_CHART_TYPE
    }

    void onActionAvailabilityChanged(ActionType actionType, boolean isAvailable);
    void onHyperlinkClicked(Hyperlink hyperlink);
    void onExternalLinkOpened(String url);
    void onError(ServiceException exception);

    class SimpleReportEventListener implements ReportEventListener {
        @Override
        public void onActionAvailabilityChanged(ActionType actionType, boolean isAvailable) {

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
    }
}
