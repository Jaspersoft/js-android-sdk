package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobCalendarTriggerEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.service.data.schedule.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class JobCalendarTriggerMapper extends BaseTriggerMapper {
    final static JobCalendarTriggerMapper INSTANCE = new JobCalendarTriggerMapper();

    @Override
    public void toTriggerEntity(JobForm form, JobFormEntity entity) {
        Trigger trigger = form.getTrigger();
        CalendarRecurrence recurrence = (CalendarRecurrence) trigger.getRecurrence();

        JobCalendarTriggerEntity calendarTrigger = new JobCalendarTriggerEntity();
        calendarTrigger.setCalendarName(trigger.getCalendarName());
        mapCommonTriggerFieldsOnEntity(form, calendarTrigger);

        DaysType daysType = recurrence.getDaysType();
        if (daysType == null) {
            calendarTrigger.setDaysType("ALL");
            calendarTrigger.setWeekDays(Collections.<Integer>emptySet());
            calendarTrigger.setMonthDays("");
        } else if (daysType instanceof DaysInWeek) {
            DaysInWeek daysInWeek = (DaysInWeek) daysType;

            calendarTrigger.setWeekDays(daysInWeek.getDays());
            calendarTrigger.setDaysType("WEEK");
            calendarTrigger.setMonthDays("");
        } else if (daysType instanceof DaysInMonth) {
            DaysInMonth type = (DaysInMonth) daysType;

            calendarTrigger.setMonthDays(type.toString());
            calendarTrigger.setDaysType("MONTH");
            calendarTrigger.setWeekDays(Collections.<Integer>emptySet());
        }

        EndDate endDate = trigger.getEndDate();
        if (endDate instanceof UntilEndDate) {
            UntilEndDate date = (UntilEndDate) endDate;
            mapEndDate(date, calendarTrigger);
        }

        calendarTrigger.setMinutes(recurrence.getMinutes().toString());
        calendarTrigger.setHours(recurrence.getHours().toString());
        calendarTrigger.setMonths(mapMonthsToEntity(recurrence.getMonths()));

        entity.setCalendarTrigger(calendarTrigger);
    }

    private Set<Integer> mapMonthsToEntity(Set<Integer> dataMonths) {
        HashSet<Integer> entityMonths = new HashSet<>();
        for (Integer dataMonth : dataMonths) {
            entityMonths.add(dataMonth + 1);
        }
        return entityMonths;
    }
}
