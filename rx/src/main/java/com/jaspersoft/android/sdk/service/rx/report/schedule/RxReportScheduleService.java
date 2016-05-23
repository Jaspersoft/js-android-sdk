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

package com.jaspersoft.android.sdk.service.rx.report.schedule;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.data.schedule.JobData;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.report.schedule.JobSearchCriteria;
import com.jaspersoft.android.sdk.service.report.schedule.JobSearchTask;
import com.jaspersoft.android.sdk.service.report.schedule.ReportScheduleService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import rx.Observable;
import rx.functions.Func0;

import java.util.Set;

/**
 * Public API that allows performing CRUD operations over report schedule jobs.
 * All responses wrapped as Rx {@link rx.Observable}.
 *
 * <pre>
 * {@code
 *
 * Server server = Server.builder()
 *         .withBaseUrl("http://mobiledemo2.jaspersoft.com/jasperserver-pro/")
 *         .build();
 *
 * Credentials credentials = SpringCredentials.builder()
 *         .withPassword("phoneuser")
 *         .withUsername("phoneuser")
 *         .withOrganization("organization_1")
 *         .build();
 *
 * final Action1<Throwable> errorHandler = new Action1<Throwable>() {
 *     &#064;
 *     public void call(Throwable throwable) {
 *         // handle error
 *     }
 * };
 *
 * AuthorizedClient client = server.newClient(credentials).create();
 * final RxReportScheduleService service = RxReportScheduleService.newService(client);
 *
 * JobSearchCriteria criteria = JobSearchCriteria.builder()
 *         .withLabel("my schedule")
 *         .withLimit(100)
 *         .withOffset(0)
 *         .build();
 * RxJobSearchTask searchTask = service.search(criteria);
 * while (searchTask.hasNext()) {
 *     // Loads by 100 items until reached the end
 *     searchTask.nextLookup().subscribe(new Action1<List<JobUnit>>() {
 *         &#064;
 *         public void call(List<JobUnit> jobUnits) {
 *             // success
 *         }
 *     }, errorHandler);
 * }
 *
 * RepositoryDestination destination = new RepositoryDestination.Builder()
 *         .withFolderUri("/temp")
 *         .build();
 * final JobForm form = new JobForm.Builder()
 *         .withLabel("my label")
 *         .withDescription("Description")
 *         .withRepositoryDestination(destination)
 *         .withOutputFormats(Collections.singletonList(JobOutputFormat.HTML))
 *         .withBaseOutputFilename("output")
 *         .build();
 *
 * service.createJob(form).subscribe(new Action1<JobData>() {
 *     &#064;
 *     public void call(JobData jobData) {
 *         int id = jobData.getId();
 *         service.readJob(id).subscribe(new Action1<JobForm>() {
 *             &#064;
 *             public void call(JobForm form) {
 *                 // success
 *             }
 *         }, errorHandler);
 *
 *         JobForm newForm = form.newBuilder()
 *                 .withLabel("New label")
 *                 .build();
 *         service.updateJob(id, newForm).subscribe(new Action1<JobData>() {
 *             &#064;
 *             public void call(JobData jobData) {
 *                 // success
 *             }
 *         }, errorHandler);
 *
 *         service.deleteJobs(Collections.singleton(id)).subscribe(new Action1<Set<Integer>>() {
 *             &#064;
 *             public void call(Set<Integer> integers) {
 *                 // success
 *             }
 *         }, errorHandler);
 *     }
 * }, errorHandler);
 *
 * }
 * </pre>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class RxReportScheduleService {
    @NotNull
    private final ReportScheduleService mSyncDelegate;

    @TestOnly
    RxReportScheduleService(@NotNull ReportScheduleService syncDelegate) {
        mSyncDelegate = syncDelegate;
    }

    /**
     * Performs search request on the basis of criteria to retrieve jobs
     *
     * @param criteria search options to control search response
     * @return task that wraps in iterator format bundle of search response
     */
    @NotNull
    public RxJobSearchTask search(@Nullable JobSearchCriteria criteria) {
        JobSearchTask searchTask = mSyncDelegate.search(criteria);
        return new RxJobSearchTask(searchTask);
    }

    /**
     * Creates new schedule job
     *
     * @param form the metadata that describes details of job
     * @return newly created job data
     */
    @NotNull
    public Observable<JobData> createJob(@NotNull final JobForm form) {
        Preconditions.checkNotNull(form, "Job form should not be null");

        return Observable.defer(new Func0<Observable<JobData>>() {
            @Override
            public Observable<JobData> call() {
                try {
                    JobData job = mSyncDelegate.createJob(form);
                    return Observable.just(job);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Updates job on the basis of form data
     *
     * @param jobId unique identifier of job
     * @param form  the metadata that describes details of job
     * @return updated job data
     */
    @NotNull
    public Observable<JobData> updateJob(final int jobId, @NotNull final JobForm form) {
        Preconditions.checkNotNull(form, "Job form should not be null");

        return Observable.defer(new Func0<Observable<JobData>>() {
            @Override
            public Observable<JobData> call() {
                try {
                    JobData job = mSyncDelegate.updateJob(jobId, form);
                    return Observable.just(job);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Reads job form of concrete job schedule
     *
     * @param jobId unique identifier of job
     * @return the metadata that describes details of job
     */
    @NotNull
    public Observable<JobForm> readJob(@NotNull final int jobId) {
        return Observable.defer(new Func0<Observable<JobForm>>() {
            @Override
            public Observable<JobForm> call() {
                try {
                    JobForm form = mSyncDelegate.readJob(jobId);
                    return Observable.just(form);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Performs batch delete request
     *
     * @param jobIds unique identifier of job
     * @return set of deleted jobs
     */
    @NotNull
    public Observable<Set<Integer>> deleteJobs(@NotNull final Set<Integer> jobIds) {
        Preconditions.checkNotNull(jobIds, "Job ids should not be null");
        Preconditions.checkArgument(!jobIds.isEmpty(), "Job ids should not be empty");

        return Observable.defer(new Func0<Observable<Set<Integer>>>() {
            @Override
            public Observable<Set<Integer>> call() {
                try {
                    Set<Integer> integers = mSyncDelegate.deleteJobs(jobIds);
                    return Observable.just(integers);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Factory method to create new service
     *
     * @param client authorized network client
     * @return instance of newly created service
     */
    @NotNull
    public static RxReportScheduleService newService(@NotNull AuthorizedClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");
        ReportScheduleService scheduleService = ReportScheduleService.newService(client);
        return new RxReportScheduleService(scheduleService);
    }

    /**
     * TODO javadoc
     */
    public ReportScheduleService toBlocking() {
        return mSyncDelegate;
    }
}
