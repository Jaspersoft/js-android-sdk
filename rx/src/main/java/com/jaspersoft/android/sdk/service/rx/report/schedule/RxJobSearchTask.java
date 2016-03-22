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
 * Wraps search results as iterator object.
 * All responses wrapped as Rx {@link rx.Observable}.
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class RxJobSearchTask {
    private final JobSearchTask mSyncDelegate;

    @TestOnly
    RxJobSearchTask(JobSearchTask syncDelegate) {
        mSyncDelegate = syncDelegate;
    }

    /**
     * Provides list of jobs on the basis of search criteria
     *
     * @return list of jobs
     */
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

    /**
     * Provides flag whether task reached end or not
     *
     * @return true if API able to perform new lookup
     */
    public boolean hasNext() {
        return mSyncDelegate.hasNext();
    }
}
