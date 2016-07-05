package com.jaspersoft.android.sdk.widget.report.renderer.hyperlink;

import android.net.Uri;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReferenceHyperlink implements Hyperlink {
    private final Uri reference;

    public ReferenceHyperlink(Uri reference) {
        this.reference = reference;
    }

    public Uri getReference() {
        return reference;
    }
}
