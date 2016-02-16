package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.ReportTestBundle;
import com.jaspersoft.android.sdk.service.data.schedule.*;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class ReportScheduleServiceTest {
    @ClassRule
    public static JrsEnvironmentRule sEnv = new JrsEnvironmentRule();

    @Test
    @Parameters(method = "reports")
    public void schedule_service_should_create_job(ReportTestBundle bundle) throws Exception {
        ReportScheduleService service = ReportScheduleService.newService(bundle.getClient());

        JobData job = createJob(bundle, service);

        updateJob(bundle, service, job);

        readJob(service, job);

        List<JobUnit> jobUnits = searchJob(service);

        deleteJobs(service, jobUnits);
    }

    private void updateJob(ReportTestBundle bundle, ReportScheduleService service, JobData job) throws ServiceException {
        JobForm form = createForm(bundle)
                .withDescription("Updated")
                .build();

        JobData response = service.updateJob(job.getId(), form);
        assertThat(response, is(notNullValue()));
    }

    private JobData createJob(ReportTestBundle bundle, ReportScheduleService service) throws ServiceException {
        JobForm form = createForm(bundle).build();

        JobData job = service.createJob(form);
        assertThat(job, is(notNullValue()));

        return job;
    }

    private JobForm.Builder createForm(ReportTestBundle bundle) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 5);

        JobSimpleTrigger trigger = new JobSimpleTrigger.Builder()
                .withOccurrenceCount(2)
                .withRecurrenceInterval(2)
                .withRecurrenceIntervalUnit(RecurrenceIntervalUnit.DAY)
                .build();

        RepositoryDestination destination = new RepositoryDestination.Builder()
                .withFolderUri("/temp")
                .build();
        JobSource.Builder source = new JobSource.Builder()
                .withUri(bundle.getUri());

        JobForm.Builder formBuilder = new JobForm.Builder()
                .withLabel("my label")
                .withDescription("Description")
                .withRepositoryDestination(destination)
                .addOutputFormat(JobOutputFormat.HTML)
                .withBaseOutputFilename("output")
                .withSimpleTrigger(trigger);
        if (bundle.hasParams()) {
            source.withParameters(bundle.getParams());
        }
        formBuilder.withJobSource(source.build());
        return formBuilder;
    }

    private List<JobUnit> searchJob(ReportScheduleService service) throws ServiceException {
        JobSearchCriteria criteria = JobSearchCriteria.builder()
                .withLabel("my label")
                .build();
        JobSearchTask search = service.search(criteria);
        List<JobUnit> units = search.nextLookup();
        assertThat(units, is(notNullValue()));

        return units;
    }

    private void readJob(ReportScheduleService service, JobData job) throws ServiceException {
        int jobId = job.getId();
        JobForm jobForm = service.readJob(jobId);
        assertThat(jobForm, is(notNullValue()));
    }

    private void deleteJobs(ReportScheduleService service, List<JobUnit> jobs) throws ServiceException {
        Set<Integer> ids = new HashSet<>(jobs.size());
        for (JobUnit job : jobs) {
            ids.add(job.getId());
        }
        service.deleteJobs(ids);
    }

    private Object[] reports() {
        return sEnv.listReports();
    }
}
