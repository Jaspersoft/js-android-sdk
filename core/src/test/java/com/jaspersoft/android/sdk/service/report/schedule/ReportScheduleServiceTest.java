package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReportScheduleServiceTest {
    private ReportScheduleService scheduleService;

    @Mock
    ReportScheduleUseCase mUseCase;

    @Rule
    public ExpectedException expected = none();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        scheduleService = new ReportScheduleService(mUseCase);
    }

    @Test
    public void search_creates_new_task() throws Exception {
        JobSearchTask task1 = scheduleService.search(JobSearchCriteria.empty());
        JobSearchTask task2 = scheduleService.search(JobSearchCriteria.empty());
        assertThat(task1, is(not(task2)));
    }

    @Test
    public void should_reject_null_client() throws Exception {
        expected.expectMessage("Client should not be null");
        expected.expect(NullPointerException.class);

        ReportScheduleService.newService(null);
    }

    @Test
    public void should_delegate_job_creation() throws Exception {
        JobForm jobForm = new JobForm.Builder().build();
        scheduleService.createJob(jobForm);
        verify(mUseCase).createJob(jobForm);
    }

    @Test
    public void should_reject_null_job_form() throws Exception {
        expected.expectMessage("Job form should not be null");
        expected.expect(NullPointerException.class);
        scheduleService.createJob(null);
    }
}