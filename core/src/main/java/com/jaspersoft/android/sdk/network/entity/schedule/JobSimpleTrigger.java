package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobSimpleTrigger {
    @Expose
    private final String timezone;
    @Expose
    private final String startType;
    @Expose
    private final String startDate;
    @Expose
    private final int occurrenceCount;

    public JobSimpleTrigger(String timezone, String startType, String startDate, int occurrenceCount) {
        this.timezone = timezone;
        this.startType = startType;
        this.startDate = startDate;
        this.occurrenceCount = occurrenceCount;
    }
}
