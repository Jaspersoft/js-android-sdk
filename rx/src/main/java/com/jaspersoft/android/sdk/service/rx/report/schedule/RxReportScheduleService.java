package com.jaspersoft.android.sdk.service.rx.report.schedule;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.JobData;
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
    public static RxReportScheduleService newService(@NotNull AuthorizedClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");
        ReportScheduleService scheduleService = ReportScheduleService.newService(client);
        return new RxReportScheduleService(scheduleService);
    }
}
