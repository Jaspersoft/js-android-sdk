package com.jaspersoft.android.sdk.widget.report.renderer.hyperlink;

import android.net.Uri;

import com.jaspersoft.android.sdk.widget.report.renderer.Destination;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class RemoteHyperlink implements Hyperlink {
    private final Uri resourceUri;
    private final Destination destination;

    public RemoteHyperlink(Uri resourceUri, Destination destination) {
        this.resourceUri = resourceUri;
        this.destination = destination;
    }

    public Destination getDestination() {
        return destination;
    }

    public Uri getResourceUri() {
        return resourceUri;
    }
}
