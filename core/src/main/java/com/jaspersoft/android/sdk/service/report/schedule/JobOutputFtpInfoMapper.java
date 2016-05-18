package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class JobOutputFtpInfoMapper extends JobMapper {
    static final JobOutputFtpInfoMapper INSTANCE = new JobOutputFtpInfoMapper();

    @Override
    public void mapFormOnEntity(JobForm form, JobFormEntity entity) {

    }

    @Override
    public void mapEntityOnForm(JobForm.Builder form, JobFormEntity entity) {

    }
}
