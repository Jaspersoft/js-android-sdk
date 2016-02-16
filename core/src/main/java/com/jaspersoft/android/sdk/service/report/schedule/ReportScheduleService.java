package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.data.schedule.JobData;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.DefaultExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.info.InMemoryInfoCache;
import com.jaspersoft.android.sdk.service.internal.info.InfoCache;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportScheduleService {

    private final ReportScheduleUseCase mUseCase;

    @TestOnly
    ReportScheduleService(ReportScheduleUseCase useCase) {
        mUseCase = useCase;
    }

    @NotNull
    public JobSearchTask search(@Nullable JobSearchCriteria criteria) {
        if (criteria == null) {
            criteria = JobSearchCriteria.empty();
        }
        return new BaseJobSearchTask(mUseCase, criteria);
    }

    @NotNull
    public JobData createJob(@NotNull JobForm form) throws ServiceException {
        Preconditions.checkNotNull(form, "Job form should not be null");
        return mUseCase.createJob(form);
    }

    @NotNull
    public JobData updateJob(int id, @NotNull JobForm form) throws ServiceException {
        Preconditions.checkNotNull(form, "Job form should not be null");
        return mUseCase.updateJob(id, form);
    }

    @NotNull
    public JobForm readJob(@NotNull int jobId) throws ServiceException {
        return mUseCase.readJob(jobId);
    }

    @NotNull
    public Set<Integer> deleteJobs(@NotNull Set<Integer> jobIds) throws ServiceException {
        Preconditions.checkNotNull(jobIds, "Job ids should not be null");
        Preconditions.checkArgument(!jobIds.isEmpty(), "Job ids should not be empty");
        return mUseCase.deleteJobs(jobIds);
    }

    @NotNull
    public static ReportScheduleService newService(@NotNull AuthorizedClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");

        JobSearchCriteriaMapper criteriaMapper = new JobSearchCriteriaMapper();
        ServiceExceptionMapper exceptionMapper = new DefaultExceptionMapper();

        InfoCache cache = new InMemoryInfoCache();
        InfoCacheManager cacheManager = InfoCacheManager.create(client, cache);

        JobDataMapper jobDataMapper = new JobDataMapper();
        JobFormMapper jobFormMapper = new JobFormMapper();
        JobUnitMapper jobUnitMapper = new JobUnitMapper();

        ReportScheduleUseCase reportScheduleUseCase = new ReportScheduleUseCase(
                exceptionMapper,
                client.reportScheduleApi(),
                cacheManager,
                criteriaMapper,
                jobDataMapper,
                jobFormMapper,
                jobUnitMapper
        );
        return new ReportScheduleService(reportScheduleUseCase);
    }
}
