package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class JobOutputFormats {
    @Expose
    private final List<String> outputFormat;

    JobOutputFormats(List<String> outputFormat) {
        this.outputFormat = outputFormat;
    }
}
