package com.jaspersoft.android.sdk.widget.report.v2;

import com.google.gson.Gson;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class JavascriptEventFactory {
    private final Gson gson = new Gson();

    protected final <C> C fromJson(String data, Class<C> type) {
        return gson.fromJson(data, type);
    }
}
