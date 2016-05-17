package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSimpleTriggerEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JobNoneTriggerMapperTest {

    private JobNoneTriggerMapper mapperUnderTest;

    private final JobFormFactory formFactory = new JobFormFactory();
    private final JobFormEntityFactory formEntityFactory = new JobFormEntityFactory();

    @Before
    public void setUp() throws Exception {
        mapperUnderTest = JobNoneTriggerMapper.INSTANCE;
    }

    @Test
    public void should_map_on_entity() throws Exception {
        JobFormEntity formEntity = formEntityFactory.givenNewJobFormEntity();
        JobForm form = formFactory.givenJobFormBuilderWithValues()
                .withStartDate(null) // immediate start type
                .build();

        mapperUnderTest.toTriggerEntity(form, formEntity);

        JobSimpleTriggerEntity simpleTrigger = formEntity.getSimpleTrigger();
        assertThat(simpleTrigger.getOccurrenceCount(), is(1));
        assertThat(simpleTrigger.getRecurrenceInterval(), is(1));
        assertThat(simpleTrigger.getRecurrenceIntervalUnit(), is("DAY"));
        assertThat(simpleTrigger.getTimezone(), is(formFactory.provideTimeZone().getID()));
        assertThat(simpleTrigger.getStartType(), is(1));
    }

    @Test
    public void should_map_start_date_on_entity() throws Exception {
        JobFormEntity formEntity = formEntityFactory.givenNewJobFormEntity();
        JobForm form = formFactory.givenJobFormWithValues();

        mapperUnderTest.toTriggerEntity(form, formEntity);

        JobSimpleTriggerEntity simpleTrigger = formEntity.getSimpleTrigger();
        assertThat(simpleTrigger.getStartType(), is(2));
        assertThat(simpleTrigger.getStartDate(), is(formFactory.provideStartDateSrc()));
    }
}