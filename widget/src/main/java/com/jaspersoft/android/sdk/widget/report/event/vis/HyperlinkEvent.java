package com.jaspersoft.android.sdk.widget.report.event.vis;

import com.jaspersoft.android.sdk.widget.report.event.Event;
import com.jaspersoft.android.sdk.widget.report.hyperlink.Hyperlink;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class HyperlinkEvent implements Event {
    private final Hyperlink hyperlink;

    HyperlinkEvent(Hyperlink hyperlink) {
        this.hyperlink = hyperlink;
    }

    public Hyperlink getHyperlink() {
        return hyperlink;
    }
}
