package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Set;

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
    @Mock
    JobForm jobForm;

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
        scheduleService.createJob(jobForm);
        verify(mUseCase).createJob(jobForm);
    }

    @Test
    public void should_delegate_job_deletion() throws Exception {
        Set<Integer> jobIds = Collections.singleton(1);
        scheduleService.deleteJobs(jobIds);
        verify(mUseCase).deleteJobs(jobIds);
    }

    @Test
    public void should_reject_null_job_form_for_create() throws Exception {
        expected.expectMessage("Job form should not be null");
        expected.expect(NullPointerException.class);
        scheduleService.createJob(null);
    }

    @Test
    public void should_reject_null_job_form_for_update() throws Exception {
        expected.expectMessage("Job form should not be null");
        expected.expect(NullPointerException.class);
        scheduleService.updateJob(1, null);
    }

    @Test
    public void should_reject_null_job_ids() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Job ids should not be null");
        scheduleService.deleteJobs(null);
    }

    @Test
    public void should_reject_empty_job_ids() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Job ids should not be empty");
        scheduleService.deleteJobs(Collections.<Integer>emptySet());
    }
}