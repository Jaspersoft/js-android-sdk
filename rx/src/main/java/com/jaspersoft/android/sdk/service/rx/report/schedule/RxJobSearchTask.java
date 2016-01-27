package com.jaspersoft.android.sdk.service.rx.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.schedule.JobSearchTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import rx.Observable;
import rx.functions.Func0;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class RxJobSearchTask {
    private final JobSearchTask mSyncDelegate;

    @TestOnly
    RxJobSearchTask(JobSearchTask syncDelegate) {
        mSyncDelegate = syncDelegate;
    }

    @NotNull
    public Observable<List<JobUnit>> nextLookup() {
        return Observable.defer(new Func0<Observable<List<JobUnit>>>() {
            @Override
            public Observable<List<JobUnit>> call() {
                try {
                    List<JobUnit> jobUnits = mSyncDelegate.nextLookup();
                    return Observable.just(jobUnits);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
    public Observable<Boolean> hasNext() {
        return Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                return Observable.just(mSyncDelegate.hasNext());
            }
        });
    }
}
