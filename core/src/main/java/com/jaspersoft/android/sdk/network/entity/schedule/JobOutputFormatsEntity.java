package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

import java.util.Collection;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobOutputFormatsEntity {
    @Expose
    private Collection<String> outputFormat;

    public Collection<String> getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(Collection<String> outputFormat) {
        this.outputFormat = outputFormat;
    }
}
