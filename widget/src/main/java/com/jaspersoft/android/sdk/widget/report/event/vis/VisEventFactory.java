package com.jaspersoft.android.sdk.widget.report.event.vis;

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
}
