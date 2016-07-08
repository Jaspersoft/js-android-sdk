package com.jaspersoft.android.sdk.widget.report.renderer;

import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface ReportRendererCallback {
    void onProgressStateChanged(boolean inProgress);
    void onRenderStateChanged(RenderState renderState);
    void onHyperlinkClicked(Hyperlink hyperlink);
    void onCurrentPageChanged(int currentPage);
    void onPagesCountChanged(int totalCount);
    void onError(ServiceException exception);
}
