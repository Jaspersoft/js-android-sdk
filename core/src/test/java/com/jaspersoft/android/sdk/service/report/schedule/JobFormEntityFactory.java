package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class JobFormEntityFactory {

    private final JobFormEntity mJobFormEntity;

    public JobFormEntityFactory() {
        mJobFormEntity = new JobFormEntity();
    }

    public JobFormEntity givenNewJobFormEntity() {
        return mJobFormEntity;
    }
}
