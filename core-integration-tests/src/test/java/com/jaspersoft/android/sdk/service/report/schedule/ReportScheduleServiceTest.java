package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.ReportTestBundle;
import com.jaspersoft.android.sdk.network.entity.schedule.JobForm;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSimpleTrigger;
import com.jaspersoft.android.sdk.service.data.schedule.JobOutputFormat;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import java.util.TimeZone;

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

    // TODO: Fix after implementing DELETE API
    @Ignore
    @Parameters(method = "reports")
    public void schedule_service_should_create_job(ReportTestBundle bundle) throws Exception {
        ReportScheduleService service = ReportScheduleService.newService(bundle.getClient());

        TimeZone timeZone = TimeZone.getDefault();
        JobSimpleTrigger simpleTrigger = new JobSimpleTrigger();
        simpleTrigger.setTimezone(timeZone.getID());
        simpleTrigger.setStartType(2);
        simpleTrigger.setOccurrenceCount(1);
        simpleTrigger.setStartDate("2016-02-16 10:00");

        JobForm form = new JobForm.Builder()
                .withLabel("my label")
                .withDescription("Description")
                .withRepositoryDestination("/temp")
                .withSource(bundle.getReportUri())
                .addOutputFormat(JobOutputFormat.HTML.toString())
                .withBaseOutputFilename("output")
                .withSimpleTrigger(simpleTrigger)
                .build();
        assertThat(service.createJob(form), is(notNullValue()));

        JobSearchCriteria criteria = JobSearchCriteria.builder()
                .withLabel("my label")
                .build();
        JobSearchTask search = service.search(criteria);
        assertThat(search.nextLookup(), is(notNullValue()));
    }

    private Object[] reports() {
        return sEnv.listReports();
    }
}
