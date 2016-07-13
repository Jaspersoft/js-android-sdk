package com.jaspersoft.android.sdk.widget.report.renderer.event.vis;

import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.ErrorMapper;
import com.jaspersoft.android.sdk.widget.report.renderer.event.Event;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class VisEventFactory extends EventFactory {
    public VisEventFactory(ErrorMapper errorMapper) {
        super(errorMapper);
    }

    public final Event createHyperlinkEvent(Hyperlink hyperlink) {
        return new HyperlinkEvent(hyperlink);
    }

    @Override
    public Event createReportExecutedEvent(ReportExecution reportExecution, Destination destination) {
        throw new UnsupportedOperationException("Execute event is not supported by visualize");
    }

    @Override
    public Event createPageExportedEvent(String reportPage, int pageNumber) {
        throw new UnsupportedOperationException("Export event is not supported by visualize");
    }
}
