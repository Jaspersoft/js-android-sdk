package com.jaspersoft.android.sdk.client.oxm.report;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class ReportDataResponse {
    private final boolean isFinal;
    private final String data;

    public ReportDataResponse(boolean isFinal, String data) {
        this.isFinal = isFinal;
        this.data = data;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public String getData() {
        return data;
    }
}
