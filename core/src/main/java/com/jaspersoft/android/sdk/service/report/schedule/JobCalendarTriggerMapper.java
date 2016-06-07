/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobCalendarTriggerEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.service.data.schedule.*;

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class JobCalendarTriggerMapper extends BaseTriggerMapper {
    private static final Integer[] ALL_WEEK_DAYS = {
            Calendar.SUNDAY,
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY
    };
    final static JobCalendarTriggerMapper INSTANCE = new JobCalendarTriggerMapper();

    @Override
    public void mapFormOnEntity(JobForm form, JobFormEntity entity) {
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

    @Override
    public void mapEntityOnForm(JobForm.Builder form, JobFormEntity entity) {
        JobCalendarTriggerEntity calendarTrigger = entity.getCalendarTrigger();

        CalendarRecurrence.Builder recurrenceBuilder = new CalendarRecurrence.Builder()
                .withMinutes(MinutesTimeFormat.parse(calendarTrigger.getMinutes()))
                .withHours(HoursTimeFormat.parse(calendarTrigger.getHours()))
                .withMonths(mapMonthsToData(calendarTrigger.getMonths()));

        String daysType = calendarTrigger.getDaysType();
        if ("ALL".equals(daysType)) {
            recurrenceBuilder.withDaysInWeek(ALL_WEEK_DAYS);
        } else if ("WEEK".equals(daysType)) {
            recurrenceBuilder.withDaysInWeek(calendarTrigger.getWeekDays());
        } else if ("MONTH".equals(daysType)) {
            recurrenceBuilder.withDaysInMonth(DaysInMonth.valueOf(calendarTrigger.getMonthDays()));
        }

        CalendarRecurrence calendarRecurrence = recurrenceBuilder.build();
        Trigger.CalendarTriggerBuilder triggerBuilder = new Trigger.Builder()
                .withCalendarName(calendarTrigger.getCalendarName())
                .withRecurrence(calendarRecurrence);

        Date endDate = null;
        String endDateString = calendarTrigger.getEndDate();
        if (endDateString != null) {
            endDate = parseDate(endDateString);
        }

        if (endDate != null) {
            triggerBuilder.withEndDate(new UntilEndDate(endDate));
        }

        Trigger trigger = triggerBuilder.build();
        form.withTrigger(trigger);
    }

    private Integer[] mapMonthsToData(Set<Integer> monthSet) {
        List<Integer> monthList = new ArrayList<>(monthSet);
        int size = monthSet.size();
        Integer[] result = new Integer[size];

        for (int i = 0; i < size; i++) {
            result[i] = monthList.get(i) - 1;
        }

        return result;
    }

    private Set<Integer> mapMonthsToEntity(Set<Integer> dataMonths) {
        HashSet<Integer> entityMonths = new HashSet<>();
        for (Integer dataMonth : dataMonths) {
            entityMonths.add(dataMonth + 1);
        }
        return entityMonths;
    }
}
