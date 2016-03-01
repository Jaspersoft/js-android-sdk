package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Wraps search results as iterator object.
 *
 * @author Tom Koptel
 * @since 2.3
 */
public abstract class JobSearchTask {
    /**
     * Provides list of jobs on the basis of search criteria
     *
     * @return list of jobs
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public abstract List<JobUnit> nextLookup() throws ServiceException;

    /**
     * Provides flag whether task reached end or not
     *
     * @return true if API able to perform new lookup
     */
    public abstract boolean hasNext();
}
