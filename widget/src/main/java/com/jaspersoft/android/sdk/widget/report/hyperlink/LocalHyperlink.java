package com.jaspersoft.android.sdk.widget.report.hyperlink;

import com.jaspersoft.android.sdk.widget.report.Destination;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class LocalHyperlink implements Hyperlink {
    private final Destination destination;

    public LocalHyperlink(Destination destination) {
        this.destination = destination;
    }

    public Destination getDestination() {
        return destination;
    }
}
