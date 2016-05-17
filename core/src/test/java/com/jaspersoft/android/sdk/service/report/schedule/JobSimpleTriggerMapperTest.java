package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSimpleTriggerEntity;
import com.jaspersoft.android.sdk.service.data.schedule.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JobSimpleTriggerMapperTest {

    private final JobFormFactory formFactory = new JobFormFactory();
    private final JobFormEntityFactory formEntityFactory = new JobFormEntityFactory();
    private JobSimpleTriggerMapper mapperUnderTest;
    private JobFormEntity formEntity;

    @Before
    public void setUp() throws Exception {
        mapperUnderTest = JobSimpleTriggerMapper.INSTANCE;
        formEntity = formEntityFactory.givenNewJobFormEntity();
    }


    @Test
    public void should_map_simple_trigger_with_infinite_value_on_entity() throws Exception {
        IntervalRecurrence recurrence = new IntervalRecurrence.Builder()
                .withInterval(10)
                .withUnit(RecurrenceIntervalUnit.DAY)
                .build();

        Trigger trigger = new Trigger.Builder()
                .withCalendarName("Gregorian")
                .withRecurrence(recurrence)
                .build();

        JobForm form = formFactory.givenJobFormBuilderWithValues()
                .withTrigger(trigger)
                .build();

        mapperUnderTest.toTriggerEntity(form, formEntity);

        JobSimpleTriggerEntity simpleTrigger = formEntity.getSimpleTrigger();
        assertThat(simpleTrigger.getCalendarName(), is("Gregorian"));
        assertThat(simpleTrigger.getOccurrenceCount(), is(-1));
        assertThat(simpleTrigger.getRecurrenceIntervalUnit(), is("DAY"));
        assertThat(simpleTrigger.getRecurrenceInterval(), is(10));
    }

    @Test
    public void should_map_simple_trigger_with_repeated_value_on_entity() throws Exception {
        IntervalRecurrence recurrence = new IntervalRecurrence.Builder()
                .withInterval(10)
                .withUnit(RecurrenceIntervalUnit.DAY)
                .build();

        Trigger trigger = new Trigger.Builder()
                .withRecurrence(recurrence)
                .withEndDate(new RepeatedEndDate(100))
                .build();

        JobForm form = formFactory.givenJobFormBuilderWithValues()
                .withTrigger(trigger)
                .build();

         mapperUnderTest.toTriggerEntity(form, formEntity);

        JobSimpleTriggerEntity simpleTrigger = formEntity.getSimpleTrigger();
        assertThat(simpleTrigger.getOccurrenceCount(), is(100));
        assertThat(simpleTrigger.getRecurrenceIntervalUnit(), is("DAY"));
        assertThat(simpleTrigger.getRecurrenceInterval(), is(10));
    }

    @Test
    public void should_map_simple_trigger_with_until_date_value_on_entity() throws Exception {
        IntervalRecurrence recurrence = new IntervalRecurrence.Builder()
                .withInterval(10)
                .withUnit(RecurrenceIntervalUnit.DAY)
                .build();

        Trigger trigger = new Trigger.Builder()
                .withRecurrence(recurrence)
                .withEndDate(new UntilEndDate(formFactory.provideEndDate()))
                .build();

        JobForm form = formFactory.givenJobFormBuilderWithValues()
                .withTrigger(trigger)
                .build();

         mapperUnderTest.toTriggerEntity(form, formEntity);

        JobSimpleTriggerEntity simpleTrigger = formEntity.getSimpleTrigger();
        assertThat(simpleTrigger.getEndDate(), is(formFactory.provideEndDateSrc()));
        assertThat(simpleTrigger.getOccurrenceCount(), is(-1));
        assertThat(simpleTrigger.getRecurrenceIntervalUnit(), is("DAY"));
        assertThat(simpleTrigger.getRecurrenceInterval(), is(10));
    }
}