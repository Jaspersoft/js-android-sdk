package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSimpleTriggerEntity;
import com.jaspersoft.android.sdk.service.data.schedule.*;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class JobSimpleTriggerMapper extends BaseTriggerMapper {
    final static JobSimpleTriggerMapper INSTANCE = new JobSimpleTriggerMapper();

    @Override
    public void toTriggerEntity(JobForm form, JobFormEntity entity) {
        Trigger trigger = form.getTrigger();
        EndDate endDate = trigger.getEndDate();

        JobSimpleTriggerEntity simpleTrigger = new JobSimpleTriggerEntity();
        mapCommonTriggerFieldsOnEntity(form, simpleTrigger);

        IntervalRecurrence recurrence = (IntervalRecurrence) trigger.getRecurrence();
        simpleTrigger.setRecurrenceInterval(recurrence.getInterval());
        simpleTrigger.setRecurrenceIntervalUnit(recurrence.getUnit().name());
        simpleTrigger.setCalendarName(trigger.getCalendarName());

        if (endDate == null) {
            simpleTrigger.setOccurrenceCount(-1);
        } else if (endDate instanceof RepeatedEndDate) {
            RepeatedEndDate date = (RepeatedEndDate) endDate;
            simpleTrigger.setOccurrenceCount(date.getOccurrenceCount());
        } else if (endDate instanceof UntilEndDate) {
            mapEndDate((UntilEndDate) endDate, simpleTrigger);
            simpleTrigger.setOccurrenceCount(-1);
        }

        entity.setSimpleTrigger(simpleTrigger);
    }
}
