package com.jaspersoft.android.sdk.widget.dashboard;

import com.google.gson.Gson;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class ReferenceHyperlinkFactory extends DashboardHyperlink.Factory<ReferenceDashboardHyperlink> {

    private final Gson gson = new Gson();

    @Override
    ReferenceDashboardHyperlink createLink(String data) {
        Metadata metadata = gson.fromJson(data, Metadata.class);
        return new ReferenceDashboardHyperlink(metadata.href);
    }

    private static class Metadata {
        private String href;
    }
}
