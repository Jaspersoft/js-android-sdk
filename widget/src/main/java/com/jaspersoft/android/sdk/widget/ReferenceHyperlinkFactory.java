package com.jaspersoft.android.sdk.widget;

import com.google.gson.Gson;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class ReferenceHyperlinkFactory extends Hyperlink.Factory<ReferenceHyperlink> {

    private final Gson gson = new Gson();

    @Override
    ReferenceHyperlink createLink(String data) {
        Metadata metadata = gson.fromJson(data, Metadata.class);
        return new ReferenceHyperlink(metadata.href);
    }

    private static class Metadata {
        private String href;
    }
}
