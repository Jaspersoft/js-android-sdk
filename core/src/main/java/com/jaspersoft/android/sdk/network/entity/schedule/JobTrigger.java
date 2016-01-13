package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class JobTrigger {
    @Expose
    private final JobSimpleTrigger simpleTrigger;

    JobTrigger(JobSimpleTrigger simpleTrigger) {
        this.simpleTrigger = simpleTrigger;
    }
}
