package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class JobSource {
    @Expose
    private final String reportUnitURI;

    JobSource(String reportUnitURI) {
        this.reportUnitURI = reportUnitURI;
    }
}
