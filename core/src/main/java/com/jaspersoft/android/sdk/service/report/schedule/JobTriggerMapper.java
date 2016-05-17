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
class JobTriggerMapper {
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

    private static class InstanceHolder {
        private final static JobTriggerMapper INSTANCE = new JobTriggerMapper(
                JobCalendarTriggerMapper.INSTANCE,
                JobSimpleTriggerMapper.INSTANCE,
                JobNoneTriggerMapper.INSTANCE);
    }

    public static JobTriggerMapper getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void toTriggerEntity(JobForm form, JobFormEntity entity) {
        Trigger trigger = form.getTrigger();

        if (trigger == null) {
            mNoneTriggerMapper.toTriggerEntity(form, entity);
        } else {
            Recurrence recurrence = trigger.getRecurrence();

            if (recurrence instanceof CalendarRecurrence) {
                mCalendarTriggerMapper.toTriggerEntity(form, entity);
            } else {
                mSimpleTriggerMapper.toTriggerEntity(form, entity);
            }
        }
    }

    public void toDataForm(JobForm.Builder form, JobFormEntity entity) {
        JobSimpleTriggerEntity simpleTrigger = entity.getSimpleTrigger();

        if (simpleTrigger == null) {
            mCalendarTriggerMapper.toDataForm(form, entity);
        } else {
            Integer recurrenceInterval = simpleTrigger.getRecurrenceInterval();
            String recurrenceIntervalUnit = simpleTrigger.getRecurrenceIntervalUnit();

            boolean triggerIsSimpleType =
                    (recurrenceInterval != null && recurrenceIntervalUnit != null);

            if (triggerIsSimpleType) {
                mSimpleTriggerMapper.toDataForm(form, entity);
            } else {
                mNoneTriggerMapper.toDataForm(form, entity);
            }
        }
    }
}
