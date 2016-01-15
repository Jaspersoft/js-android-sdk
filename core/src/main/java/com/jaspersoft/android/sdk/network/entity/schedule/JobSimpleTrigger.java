package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobSimpleTrigger {
    @Expose
    private String timezone;
    @Expose
    private int startType;
    @Expose
    private String startDate;
    @Expose
    private String stopDate;
    @Expose
    private Integer occurrenceCount;
    @Expose
    private Integer recurrenceInterval;
    @Expose
    private String recurrenceIntervalUnit;

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setStartType(int startType) {
        this.startType = startType;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
    }

    public void setOccurrenceCount(int occurrenceCount) {
        this.occurrenceCount = occurrenceCount;
    }

    public void setRecurrenceInterval(int recurrenceInterval) {
        this.recurrenceInterval = recurrenceInterval;
    }

    public void setRecurrenceIntervalUnit(String recurrenceIntervalUnit) {
        this.recurrenceIntervalUnit = recurrenceIntervalUnit;
    }
}
