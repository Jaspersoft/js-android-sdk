package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSimpleTriggerEntity;
import com.jaspersoft.android.sdk.service.data.schedule.CalendarRecurrence;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.Recurrence;
import com.jaspersoft.android.sdk.service.data.schedule.Trigger;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class JobTriggerMapper extends JobMapper {
    final static JobTriggerMapper INSTANCE = new JobTriggerMapper(
            JobCalendarTriggerMapper.INSTANCE,
            JobSimpleTriggerMapper.INSTANCE,
            JobNoneTriggerMapper.INSTANCE);

    private final JobCalendarTriggerMapper mCalendarTriggerMapper;
    private final JobSimpleTriggerMapper mSimpleTriggerMapper;
    private final JobNoneTriggerMapper mNoneTriggerMapper;

    JobTriggerMapper(
            JobCalendarTriggerMapper calendarTriggerMapper,
            JobSimpleTriggerMapper simpleTriggerMapper,
            JobNoneTriggerMapper noneTriggerMapper
    ) {
        mCalendarTriggerMapper = calendarTriggerMapper;
        mSimpleTriggerMapper = simpleTriggerMapper;
        mNoneTriggerMapper = noneTriggerMapper;
    }

    @Override
    public void mapFormOnEntity(JobForm form, JobFormEntity entity) {
        Trigger trigger = form.getTrigger();

        if (trigger == null) {
            mNoneTriggerMapper.mapFormOnEntity(form, entity);
        } else {
            Recurrence recurrence = trigger.getRecurrence();

            if (recurrence instanceof CalendarRecurrence) {
                mCalendarTriggerMapper.mapFormOnEntity(form, entity);
            } else {
                mSimpleTriggerMapper.mapFormOnEntity(form, entity);
            }
        }
    }

    @Override
    public void mapEntityOnForm(JobForm.Builder form, JobFormEntity entity) {
        JobSimpleTriggerEntity simpleTrigger = entity.getSimpleTrigger();

        if (simpleTrigger == null) {
            mCalendarTriggerMapper.mapEntityOnForm(form, entity);
        } else {
            Integer recurrenceInterval = simpleTrigger.getRecurrenceInterval();
            String recurrenceIntervalUnit = simpleTrigger.getRecurrenceIntervalUnit();

            boolean triggerIsSimpleType =
                    (recurrenceInterval != null && recurrenceIntervalUnit != null);

            if (triggerIsSimpleType) {
                mSimpleTriggerMapper.mapEntityOnForm(form, entity);
            } else {
                mNoneTriggerMapper.mapEntityOnForm(form, entity);
            }
        }
    }
}
