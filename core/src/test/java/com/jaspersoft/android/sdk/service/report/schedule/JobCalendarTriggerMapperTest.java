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
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

public class JobCalendarTriggerMapperTest {
    private final JobFormFactory formFactory = new JobFormFactory();

    private JobCalendarTriggerMapper mapperUnderTest;
    private JobFormEntity formEntity;

    @Before
    public void setUp() throws Exception {
        mapperUnderTest = JobCalendarTriggerMapper.INSTANCE;
        formEntity = formFactory.givenNewJobFormEntity();
    }

    @Test
    public void should_map_on_entity() throws Exception {
        CalendarRecurrence recurrence = new CalendarRecurrence.Builder()
                .withMonths(Calendar.JANUARY, Calendar.FEBRUARY)
                .withHours(HoursTimeFormat.parse("1"))
                .withMinutes(MinutesTimeFormat.parse("2"))
                .build();

        Trigger trigger = new Trigger.Builder()
                .withCalendarName("Gregorian")
                .withRecurrence(recurrence)
                .withEndDate(new UntilEndDate(formFactory.provideEndDate()))
                .build();

        JobForm form = formFactory.givenJobFormBuilderWithValues()
                .withStartDate(null)
                .withTrigger(trigger)
                .build();

        mapperUnderTest.mapFormOnEntity(form, formEntity);

        JobCalendarTriggerEntity calendarTrigger = formEntity.getCalendarTrigger();
        assertThat(calendarTrigger.getCalendarName(), is("Gregorian"));
        assertThat(calendarTrigger.getEndDate(), is(formFactory.provideEndDateSrc()));
        assertThat(calendarTrigger.getMonths(), hasItems(Calendar.JANUARY + 1, Calendar.FEBRUARY + 1));
        assertThat(calendarTrigger.getHours(), is("1"));
        assertThat(calendarTrigger.getMinutes(), is("2"));
        assertThat(calendarTrigger.getDaysType(), is("ALL"));
        assertThat(calendarTrigger.getWeekDays(), is(empty()));
        assertThat(calendarTrigger.getMonthDays(), is(""));
        assertThat(calendarTrigger.getStartType(), is(1));
    }

    @Test
    public void should_map_with_day_type_days_in_week() throws Exception {
        CalendarRecurrence recurrence = new CalendarRecurrence.Builder()
                .withMonths(Calendar.JANUARY, Calendar.FEBRUARY)
                .withHours(HoursTimeFormat.parse("1"))
                .withMinutes(MinutesTimeFormat.parse("2"))
                .withDaysInWeek(Calendar.MONDAY, Calendar.THURSDAY)
                .build();

        Trigger trigger = new Trigger.Builder()
                .withRecurrence(recurrence)
                .build();

        JobForm form = formFactory.givenJobFormBuilderWithValues()
                .withTrigger(trigger)
                .build();

        mapperUnderTest.mapFormOnEntity(form, formEntity);

        JobCalendarTriggerEntity calendarTrigger = formEntity.getCalendarTrigger();
        assertThat(calendarTrigger.getMonths(), hasItems(Calendar.JANUARY + 1, Calendar.FEBRUARY + 1));
        assertThat(calendarTrigger.getHours(), is("1"));
        assertThat(calendarTrigger.getMinutes(), is("2"));
        assertThat(calendarTrigger.getDaysType(), is("WEEK"));
        assertThat(calendarTrigger.getWeekDays(), hasItems(Calendar.MONDAY, Calendar.THURSDAY));
        assertThat(calendarTrigger.getMonthDays(), is(""));
    }

    @Test
    public void should_map_with_day_type_days_in_month() throws Exception {
        CalendarRecurrence recurrence = new CalendarRecurrence.Builder()
                .withMonths(Calendar.JANUARY, Calendar.FEBRUARY)
                .withHours(HoursTimeFormat.parse("1"))
                .withMinutes(MinutesTimeFormat.parse("2"))
                .withDaysInMonth(DaysInMonth.valueOf("1"))
                .build();

        Trigger trigger = new Trigger.Builder()
                .withRecurrence(recurrence)
                .build();

        JobForm form = formFactory.givenJobFormBuilderWithValues()
                .withTrigger(trigger)
                .build();

        mapperUnderTest.mapFormOnEntity(form, formEntity);

        JobCalendarTriggerEntity calendarTrigger = formEntity.getCalendarTrigger();
        assertThat(calendarTrigger.getMonths(), hasItems(Calendar.JANUARY + 1, Calendar.FEBRUARY + 1));
        assertThat(calendarTrigger.getHours(), is("1"));
        assertThat(calendarTrigger.getMinutes(), is("2"));
        assertThat(calendarTrigger.getDaysType(), is("MONTH"));
        assertThat(calendarTrigger.getWeekDays(), is(empty()));
        assertThat(calendarTrigger.getMonthDays(), is("1"));
    }


    @Test
    public void should_map_calendar_trigger_with_all_type() throws Exception {
        JobCalendarTriggerEntity calendarTrigger = new JobCalendarTriggerEntity();
        calendarTrigger.setMonths(new HashSet<>(Arrays.asList(Calendar.FEBRUARY + 1)));
        calendarTrigger.setMinutes("30");
        calendarTrigger.setHours("3");
        calendarTrigger.setDaysType("ALL");
        calendarTrigger.setEndDate(formFactory.provideEndDateSrc());

        formEntity.setCalendarTrigger(calendarTrigger);

        JobForm.Builder jobFormBuilder = formFactory.givenJobFormBuilderWithValues();
        mapperUnderTest.mapEntityOnForm(jobFormBuilder, formEntity);
        JobForm expected = jobFormBuilder.build();
        Trigger trigger = expected.getTrigger();

        CalendarRecurrence calendarRecurrence = (CalendarRecurrence) trigger.getRecurrence();
        UntilEndDate endDate = (UntilEndDate) trigger.getEndDate();

        assertThat(calendarRecurrence.getMinutes().toString(), is("30"));
        assertThat(calendarRecurrence.getHours().toString(), is("3"));
        assertThat(calendarRecurrence.getMonths(), hasItems(Calendar.FEBRUARY));
        DaysInWeek daysType = (DaysInWeek) calendarRecurrence.getDaysType();
        assertThat(daysType.getDays(), hasItems(Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY));
    }

    @Test
    public void should_map_calendar_trigger_with_week_type() throws Exception {
        JobCalendarTriggerEntity calendarTrigger = new JobCalendarTriggerEntity();
        calendarTrigger.setMonths(new HashSet<>(Arrays.asList(Calendar.FEBRUARY)));
        calendarTrigger.setWeekDays(new HashSet<>(Arrays.asList(Calendar.MONDAY)));
        calendarTrigger.setDaysType("WEEK");

        formEntity.setCalendarTrigger(calendarTrigger);

        JobForm.Builder jobFormBuilder = formFactory.givenJobFormBuilderWithValues();
        mapperUnderTest.mapEntityOnForm(jobFormBuilder, formEntity);
        JobForm expected = jobFormBuilder.build();
        Trigger trigger = expected.getTrigger();

        CalendarRecurrence calendarRecurrence = (CalendarRecurrence) trigger.getRecurrence();

        DaysInWeek daysType = (DaysInWeek) calendarRecurrence.getDaysType();
        assertThat(daysType.getDays(), hasItems(Calendar.MONDAY));
    }

    @Test
    public void should_map_calendar_trigger_with_month_type() throws Exception {
        JobCalendarTriggerEntity calendarTrigger = new JobCalendarTriggerEntity();
        calendarTrigger.setMonths(new HashSet<>(Arrays.asList(Calendar.FEBRUARY)));
        calendarTrigger.setDaysType("MONTH");
        calendarTrigger.setMonthDays("1");

        formEntity.setCalendarTrigger(calendarTrigger);

        JobForm.Builder jobFormBuilder = formFactory.givenJobFormBuilderWithValues();
        mapperUnderTest.mapEntityOnForm(jobFormBuilder, formEntity);
        JobForm expected = jobFormBuilder.build();
        Trigger trigger = expected.getTrigger();

        CalendarRecurrence calendarRecurrence = (CalendarRecurrence) trigger.getRecurrence();

        DaysInMonth daysType = (DaysInMonth) calendarRecurrence.getDaysType();
        assertThat(daysType.toString(), is("1"));
    }
}