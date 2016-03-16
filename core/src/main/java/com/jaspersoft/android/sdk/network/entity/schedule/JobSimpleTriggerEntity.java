package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobSimpleTriggerEntity extends JobTriggerEntity {
    @Expose
    private int occurrenceCount;
    @Expose
    private Integer recurrenceInterval;
    @Expose
    private String recurrenceIntervalUnit;

    public int getOccurrenceCount() {
        return occurrenceCount;
    }

    public void setOccurrenceCount(int occurrenceCount) {
        this.occurrenceCount = occurrenceCount;
    }

    public Integer getRecurrenceInterval() {
        return recurrenceInterval;
    }

    public void setRecurrenceInterval(Integer recurrenceInterval) {
        this.recurrenceInterval = recurrenceInterval;
    }

    public String getRecurrenceIntervalUnit() {
        return recurrenceIntervalUnit;
    }

    public void setRecurrenceIntervalUnit(String recurrenceIntervalUnit) {
        this.recurrenceIntervalUnit = recurrenceIntervalUnit;
    }
}
