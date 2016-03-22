package com.jaspersoft.android.sdk.testkit;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class ListReportParamsCommand {
    private final String mResourceUri;

    public ListReportParamsCommand(String resourceUri) {
        mResourceUri = resourceUri;
    }

    public String getReportUri() {
        return mResourceUri;
    }
}
