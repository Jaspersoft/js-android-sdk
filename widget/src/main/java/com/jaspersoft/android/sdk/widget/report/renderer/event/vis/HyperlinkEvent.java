package com.jaspersoft.android.sdk.widget.report.renderer.event.vis;

import com.jaspersoft.android.sdk.widget.report.renderer.event.Event;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;

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
