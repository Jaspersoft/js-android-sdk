package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSimpleTriggerEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class JobNoneTriggerMapperTest {

    private JobNoneTriggerMapper mapperUnderTest;

    private final JobFormFactory formFactory = new JobFormFactory();

    @Before
    public void setUp() throws Exception {
        mapperUnderTest = JobNoneTriggerMapper.INSTANCE;
    }

    @Test
    public void should_map_on_entity() throws Exception {
        JobFormEntity formEntity = formFactory.givenNewJobFormEntity();
        JobForm form = formFactory.givenJobFormBuilderWithValues()
                .withStartDate(null) // immediate start type
                .build();

        mapperUnderTest.mapFormOnEntity(form, formEntity);

        JobSimpleTriggerEntity simpleTrigger = formEntity.getSimpleTrigger();
        assertThat(simpleTrigger.getOccurrenceCount(), is(1));
        assertThat(simpleTrigger.getRecurrenceInterval(), is(1));
        assertThat(simpleTrigger.getRecurrenceIntervalUnit(), is("DAY"));
        assertThat(simpleTrigger.getTimezone(), is(formFactory.provideTimeZone().getID()));
        assertThat(simpleTrigger.getStartType(), is(1));
    }

    @Test
    public void should_map_start_date_on_entity() throws Exception {
        JobFormEntity formEntity = formFactory.givenNewJobFormEntity();
        JobForm form = formFactory.givenJobFormWithValues();

        mapperUnderTest.mapFormOnEntity(form, formEntity);

        JobSimpleTriggerEntity simpleTrigger = formEntity.getSimpleTrigger();
        assertThat(simpleTrigger.getStartType(), is(2));
        assertThat(simpleTrigger.getStartDate(), is(formFactory.provideStartDateSrc()));
    }

    @Test
    public void should_map_none_trigger_type_as_simple_one() throws Exception {
        JobForm.Builder formBuilder = formFactory.givenJobFormBuilderWithValues();
        JobFormEntity jobFormEntity = formFactory.givenJobFormEntityWithValues();

        JobSimpleTriggerEntity simpleTrigger = new JobSimpleTriggerEntity();
        simpleTrigger.setOccurrenceCount(1);
        simpleTrigger.setStartDate(formFactory.provideStartDateSrc());
        simpleTrigger.setTimezone(formFactory.provideTimeZone().getID());

        jobFormEntity.setSimpleTrigger(simpleTrigger);

        mapperUnderTest.mapEntityOnForm(formBuilder, jobFormEntity);
        JobForm expected = formBuilder.build();

        assertThat(expected.getStartDate(), is(formFactory.provideStartDate()));
        assertThat(expected.getTimeZone(), is(formFactory.provideTimeZone()));
        assertThat(expected.getTrigger(), is(nullValue()));
    }

}