package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.data.schedule.JobData;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.JobExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.Set;

/**
 * Public API that allows performing CRUD operations over report schedule jobs.
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
 *
 * AuthorizedClient client = server.newClient(credentials).create();
 * ReportScheduleService service = ReportScheduleService.newService(client);
 * try {
 *     JobSearchCriteria criteria = JobSearchCriteria.builder()
 *             .withLabel("my schedule")
 *             .withLimit(100)
 *             .withOffset(0)
 *             .build();
 *     JobSearchTask searchTask = service.search(criteria);
 *     while (searchTask.hasNext()) {
 *         // Loads by 100 items until reached the end
 *         List<JobUnit> jobUnits = searchTask.nextLookup();
 *     }
 *
 *     RepositoryDestination destination = new RepositoryDestination.Builder()
 *             .withFolderUri("/temp")
 *             .build();
 *     JobForm form = new JobForm.Builder()
 *             .withLabel("my label")
 *             .withDescription("Description")
 *             .withRepositoryDestination(destination)
 *             .withOutputFormats(Collections.singletonList(JobOutputFormat.HTML))
 *             .withBaseOutputFilename("output")
 *             .build();
 *     JobData job = service.createJob(form);
 *
 *     int id = job.getId();
 *     JobForm jobForm = service.readJob(id);
 *
 *     JobForm newForm = jobForm.newBuilder()
 *             .withLabel("New label")
 *             .build();
 *     JobData data = service.updateJob(id, newForm);
 *
 *     Set<Integer> deletedJobIds = service.deleteJobs(Collections.singleton(id));
 * } catch (ServiceException e) {
 *     // handle errors
 * }
 * }
 * </pre>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class ReportScheduleService {

    private final ReportScheduleUseCase mUseCase;

    @TestOnly
    ReportScheduleService(ReportScheduleUseCase useCase) {
        mUseCase = useCase;
    }

    /**
     * Performs search request on the basis of criteria to retrieve jobs
     *
     * @param criteria search options to control search response
     * @return task that wraps in iterator format bundle of search response
     */
    @NotNull
    public JobSearchTask search(@Nullable JobSearchCriteria criteria) {
        if (criteria == null) {
            criteria = JobSearchCriteria.empty();
        }
        return new BaseJobSearchTask(mUseCase, criteria);
    }

    /**
     * Creates new schedule job
     *
     * @param form the metadata that describes details of job
     * @return newly created job data
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public JobData createJob(@NotNull JobForm form) throws ServiceException {
        Preconditions.checkNotNull(form, "Job form should not be null");
        return mUseCase.createJob(form);
    }

    /**
     * Updates job on the basis of form data
     *
     * @param jobId unique identifier of job
     * @param form  the metadata that describes details of job
     * @return updated job data
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public JobData updateJob(int jobId, @NotNull JobForm form) throws ServiceException {
        Preconditions.checkNotNull(form, "Job form should not be null");
        return mUseCase.updateJob(jobId, form);
    }

    /**
     * Reads job form of concrete job schedule
     *
     * @param jobId unique identifier of job
     * @return the metadata that describes details of job
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public JobForm readJob(int jobId) throws ServiceException {
        return mUseCase.readJob(jobId);
    }

    /**
     * Performs batch delete request
     *
     * @param jobIds unique identifier of job
     * @return set of deleted jobs
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public Set<Integer> deleteJobs(@NotNull Set<Integer> jobIds) throws ServiceException {
        Preconditions.checkNotNull(jobIds, "Job ids should not be null");
        Preconditions.checkArgument(!jobIds.isEmpty(), "Job ids should not be empty");
        return mUseCase.deleteJobs(jobIds);
    }

    /**
     * Factory method to create new service
     *
     * @param client authorized network client
     * @return instance of newly created service
     */
    @NotNull
    public static ReportScheduleService newService(@NotNull AuthorizedClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");

        JobSearchCriteriaMapper criteriaMapper = new JobSearchCriteriaMapper();
        ServiceExceptionMapper exceptionMapper = JobExceptionMapper.getInstance();

        InfoCacheManager cacheManager = InfoCacheManager.create(client);

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
