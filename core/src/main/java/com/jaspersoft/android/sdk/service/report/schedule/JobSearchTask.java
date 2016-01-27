package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public abstract class JobSearchTask {
    @NotNull
    public abstract List<JobUnit> nextLookup() throws ServiceException;

    public abstract boolean hasNext();
}
