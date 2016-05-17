package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSimpleTriggerEntity;
import com.jaspersoft.android.sdk.service.data.schedule.*;

import java.util.Date;

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

    @Override
    public void toDataForm(JobForm.Builder form, JobFormEntity entity) {
        mapCommonEntityTriggerFieldsOnEntity(form, entity);

        JobSimpleTriggerEntity simpleTrigger = entity.getSimpleTrigger();

        int occurrenceCount = simpleTrigger.getOccurrenceCount();
        Integer recurrenceInterval = simpleTrigger.getRecurrenceInterval();
        String recurrenceIntervalUnit = simpleTrigger.getRecurrenceIntervalUnit();

        Date endDate = null;
        String endDateString = simpleTrigger.getEndDate();
        if (endDateString != null) {
            endDate = parseDate(endDateString);
        }

        IntervalRecurrence recurrence = new IntervalRecurrence.Builder()
                .withInterval(recurrenceInterval)
                .withUnit(RecurrenceIntervalUnit.valueOf(recurrenceIntervalUnit))
                .build();
        Trigger.SimpleTriggerBuilder triggerBuilder = new Trigger.Builder()
                .withCalendarName(simpleTrigger.getCalendarName())
                .withRecurrence(recurrence);

        if (occurrenceCount < 0 && endDate != null) {
            triggerBuilder.withEndDate(new UntilEndDate(endDate));
        } else {
            triggerBuilder.withEndDate(new RepeatedEndDate(occurrenceCount));
        }

        Trigger trigger = triggerBuilder.build();
        form.withTrigger(trigger);
    }
}
