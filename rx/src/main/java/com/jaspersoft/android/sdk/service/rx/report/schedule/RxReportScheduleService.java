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
 * @author Tom Koptel
 * @since 2.0
 */
public class RxReportScheduleService {
    @NotNull
    private final ReportScheduleService mSyncDelegate;

    @TestOnly
    RxReportScheduleService(@NotNull ReportScheduleService syncDelegate) {
        mSyncDelegate = syncDelegate;
    }

    @NotNull
    public RxJobSearchTask search(@Nullable JobSearchCriteria criteria) {
        JobSearchTask searchTask = mSyncDelegate.search(criteria);
        return new RxJobSearchTask(searchTask);
    }

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

    @NotNull
    public Observable<Set<Integer>> deleteJobs(@NotNull final Set<Integer> ids) {
        Preconditions.checkNotNull(ids, "Job ids should not be null");
        Preconditions.checkArgument(!ids.isEmpty(), "Job ids should not be empty");

        return Observable.defer(new Func0<Observable<Set<Integer>>>() {
            @Override
            public Observable<Set<Integer>> call() {
                try {
                    Set<Integer> integers = mSyncDelegate.deleteJobs(ids);
                    return Observable.just(integers);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
    public static RxReportScheduleService newService(@NotNull AuthorizedClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");
        ReportScheduleService scheduleService = ReportScheduleService.newService(client);
        return new RxReportScheduleService(scheduleService);
    }
}
