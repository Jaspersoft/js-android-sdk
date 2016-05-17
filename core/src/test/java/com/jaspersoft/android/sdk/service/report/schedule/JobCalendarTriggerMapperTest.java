package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobCalendarTriggerEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.service.data.schedule.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

public class JobCalendarTriggerMapperTest {
    private final JobFormFactory formFactory = new JobFormFactory();
    private final JobFormEntityFactory formEntityFactory = new JobFormEntityFactory();

    private JobCalendarTriggerMapper mapperUnderTest;
    private JobFormEntity formEntity;

    @Before
    public void setUp() throws Exception {
        mapperUnderTest = JobCalendarTriggerMapper.INSTANCE;
        formEntity = formEntityFactory.givenNewJobFormEntity();
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

        mapperUnderTest.toTriggerEntity(form, formEntity);

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

        JobForm form =  formFactory.givenJobFormBuilderWithValues()
                .withTrigger(trigger)
                .build();

        mapperUnderTest.toTriggerEntity(form, formEntity);

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

        mapperUnderTest.toTriggerEntity(form, formEntity);

        JobCalendarTriggerEntity calendarTrigger = formEntity.getCalendarTrigger();
        assertThat(calendarTrigger.getMonths(), hasItems(Calendar.JANUARY + 1, Calendar.FEBRUARY + 1));
        assertThat(calendarTrigger.getHours(), is("1"));
        assertThat(calendarTrigger.getMinutes(), is("2"));
        assertThat(calendarTrigger.getDaysType(), is("MONTH"));
        assertThat(calendarTrigger.getWeekDays(), is(empty()));
        assertThat(calendarTrigger.getMonthDays(), is("1"));
    }
}