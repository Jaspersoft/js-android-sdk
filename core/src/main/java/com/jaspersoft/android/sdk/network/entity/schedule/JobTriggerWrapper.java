package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class JobTriggerWrapper {
    @Expose
    private JobSimpleTriggerEntity simpleTrigger;
    @Expose
    private JobCalendarTriggerEntity calendarTrigger;

    public JobSimpleTriggerEntity getSimpleTrigger() {
        return simpleTrigger;
    }

    public JobCalendarTriggerEntity getCalendarTrigger() {
        return calendarTrigger;
    }

    public void setSimpleTrigger(JobSimpleTriggerEntity simpleTrigger) {
        this.simpleTrigger = simpleTrigger;
    }

    public void setCalendarTrigger(JobCalendarTriggerEntity calendarTrigger) {
        this.calendarTrigger = calendarTrigger;
    }
}
