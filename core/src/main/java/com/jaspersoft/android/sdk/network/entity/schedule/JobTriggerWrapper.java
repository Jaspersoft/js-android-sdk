package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class JobTriggerWrapper {
    @Expose
    private JobSimpleTriggerEntity simpleTrigger;

    public JobSimpleTriggerEntity getSimpleTrigger() {
        return simpleTrigger;
    }

    public void setSimpleTrigger(JobSimpleTriggerEntity simpleTrigger) {
        this.simpleTrigger = simpleTrigger;
    }
}
