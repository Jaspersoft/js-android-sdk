package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.5
 */
final class ProxyJobSearchTask extends JobSearchTask {
    private final InfoCacheManager mCacheManager;
    private final JobSearchTaskFactory mJobSearchTaskFactory;
    private JobSearchTask internalTask;

    public ProxyJobSearchTask(InfoCacheManager cacheManager, JobSearchTaskFactory jobSearchTaskFactory) {
        mCacheManager = cacheManager;
        mJobSearchTaskFactory = jobSearchTaskFactory;
    }

    @NotNull
    @Override
    public List<JobUnit> nextLookup() throws ServiceException {
        if (internalTask == null) {
            ServerInfo info = mCacheManager.getInfo();
            ServerVersion version = info.getVersion();
            internalTask = mJobSearchTaskFactory.create(version);
        }
        return internalTask.nextLookup();
    }

    @Override
    public boolean hasNext() {
        if (internalTask == null) {
            return false;
        }
        return internalTask.hasNext();
    }
}
