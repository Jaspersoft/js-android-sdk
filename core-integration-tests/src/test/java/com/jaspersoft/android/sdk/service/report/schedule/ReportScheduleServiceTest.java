package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.ReportTestBundle;
import com.jaspersoft.android.sdk.service.data.schedule.*;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Collections;

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

        JobData job = service.createJob(formBuilder.build());
        assertThat(job, is(notNullValue()));

        JobSearchCriteria criteria = JobSearchCriteria.builder()
                .withLabel("my label")
                .build();
        JobSearchTask search = service.search(criteria);
        assertThat(search.nextLookup(), is(notNullValue()));

        service.deleteJobs(Collections.singleton(job.getId()));
    }

    private Object[] reports() {
        return sEnv.listReports();
    }
}
