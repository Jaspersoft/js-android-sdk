/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

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
import static org.hamcrest.Matchers.*;

/**
 * @author Tom Koptel
 * @since 2.3
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

        readJob(service, job.getId());

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

        JobAlert alert = new JobAlert.Builder()
                .withSubject("True")
                .withRecipients(Collections.singleton("a@a.com"))
                .withRecipientType(JobAlert.RecipientType.OWNER)
                .withJobState(JobAlert.JobState.ALL)
                .withIncludeReportJobInfo(true)
                .withMessageTextWhenJobFails("failed")
                .withIncludeStackTrace(true)
                .build();
        RepositoryDestination destination = new RepositoryDestination.Builder()
                .withFolderUri("/temp")
                .build();
        JobSource.Builder source = new JobSource.Builder()
                .withUri(bundle.getUri());

        JobMailNotification notification = new JobMailNotification.Builder()
                .withSubject("sy")
                .build();

        JobForm.Builder formBuilder = new JobForm.Builder()
                .withJobAlert(alert)
                .withLabel("my label")
                .withDescription("Description")
                .withRepositoryDestination(destination)
                .withOutputFormats(Collections.singletonList(JobOutputFormat.HTML))
                .withBaseOutputFilename("output")
                .withMailNotification(notification);

        formBuilder.withJobSource(source.build());
        return formBuilder;
    }

    private List<JobUnit> searchJob(ReportScheduleService service) throws ServiceException {
        JobSearchCriteria criteria = JobSearchCriteria.builder()
                .withLabel("my label")
                .build();
        JobSearchTask search = service.search(criteria);
        List<JobUnit> units = search.nextLookup();
        assertThat(units, is(not(empty())));

        return units;
    }

    private void readJob(ReportScheduleService service, int jobId) throws ServiceException {
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
