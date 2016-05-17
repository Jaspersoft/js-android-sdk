package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSimpleTriggerEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class JobNoneTriggerMapper extends BaseTriggerMapper {
    final static JobNoneTriggerMapper INSTANCE = new JobNoneTriggerMapper();

    @Override
    public void toTriggerEntity(JobForm form, JobFormEntity entity) {
        JobSimpleTriggerEntity triggerEntity = new JobSimpleTriggerEntity();

        mapCommonTriggerFieldsOnEntity(form, triggerEntity);
        triggerEntity.setOccurrenceCount(1);
        triggerEntity.setRecurrenceInterval(1);
        triggerEntity.setRecurrenceIntervalUnit("DAY");

        entity.setSimpleTrigger(triggerEntity);
    }

    @Override
    public void toDataForm(JobForm.Builder form, JobFormEntity entity) {
        mapCommonEntityTriggerFieldsOnEntity(form, entity);
    }
}
