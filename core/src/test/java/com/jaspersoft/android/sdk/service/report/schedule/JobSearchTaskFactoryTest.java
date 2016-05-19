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

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        JobSearchCriteria searchCriteria = JobSearchCriteria.empty();
        factoryUnderTest = new JobSearchTaskFactory(mUseCase, searchCriteria);
    }

    @Test
    @Parameters({
            "5.5", "5.6", "5.6.1", "6.0", "6.0.1", "6.1", "6.1.1"
    })
    public void should_use_legacy_setup_for_jrs_lower_than(String version) throws Exception {
        JobSearchTask task = factoryUnderTest.create(ServerVersion.valueOf(version));
        assertThat(task, is(instanceOf(LegacyJobSearchTask.class)));
    }

    @Test
    @Parameters({
            "6.2", "6.3"
    })
    public void should_use_query_capable_setup_for_jrs_greater_than(String version) throws Exception {
        JobSearchTask task = factoryUnderTest.create(ServerVersion.valueOf(version));
        assertThat(task, is(instanceOf(JobSearch62Task.class)));
    }
}