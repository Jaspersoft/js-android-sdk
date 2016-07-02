package com.jaspersoft.android.sdk.widget.report.event.vis;

import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.ErrorMapper;
import com.jaspersoft.android.sdk.widget.report.event.Event;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.hyperlink.Hyperlink;

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
    public Event createPageExportedEvent(String reportPage) {
        throw new UnsupportedOperationException("Export event is not supported by visualize");
    }

    @Override
    public Event createParamsUpdatedEvent() {
        throw new UnsupportedOperationException("Update params event is not supported by visualize");
    }
}
