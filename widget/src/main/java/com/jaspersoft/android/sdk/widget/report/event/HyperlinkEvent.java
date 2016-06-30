package com.jaspersoft.android.sdk.widget.report.event;

import com.jaspersoft.android.sdk.widget.report.hyperlink.Hyperlink;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class HyperlinkEvent implements Event {
    private final Hyperlink hyperlink;

    public HyperlinkEvent(Hyperlink hyperlink) {
        this.hyperlink = hyperlink;
    }

    public Hyperlink getHyperlink() {
        return hyperlink;
    }
}
