package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(JUnitParamsRunner.class)
public class JobSearchTaskFactoryTest {
    @Mock
    ReportScheduleUseCase mUseCase;
    private JobSearchTaskFactory factoryUnderTest;
    private JobSearchCriteria searchCriteria;
    private JobSearchTask searchTask;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    @Parameters({
            "5.5", "6.1.1"
    })
    public void should_use_legacy_setup_for_jrs_lower_than(String version) throws Exception {
        givenCriteriaWithLabel();
        givenTaskFactory();

        whenCreatesSearchTask(version);

        thenShouldCreateTaskOfType(MemoryFilterSearchTask.class);
    }

    @Test
    @Parameters({
            "5.5", "6.1.1"
    })
    public void should_not_use_legacy_setup_for_criteria_without_query(String version) throws Exception {
        givenCriteriaWithoutLabel();
        givenTaskFactory();

        whenCreatesSearchTask(version);

        thenShouldCreateTaskOfType(RestFilterSearchTask.class);
    }

    @Test
    @Parameters({
            "6.2", "6.3"
    })
    public void should_use_query_capable_setup_for_jrs_greater_than(String version) throws Exception {
        givenCriteriaWithLabel();
        givenTaskFactory();

        whenCreatesSearchTask(version);

        thenShouldCreateTaskOfType(RestFilterSearchTask.class);
    }


    private void thenShouldCreateTaskOfType(Class<? extends JobSearchTask> type) {
        assertThat(searchTask, is(instanceOf(type)));
    }

    private void whenCreatesSearchTask(String version) {
        searchTask = factoryUnderTest.create(ServerVersion.valueOf(version));
    }

    private void givenCriteriaWithLabel() {
        searchCriteria = JobSearchCriteria.builder()
                .withLabel("label")
                .build();
    }

    private void givenCriteriaWithoutLabel() {
        searchCriteria = JobSearchCriteria.builder().build();
    }

    private void givenTaskFactory() {
        factoryUnderTest = new JobSearchTaskFactory(mUseCase, searchCriteria);
    }
}