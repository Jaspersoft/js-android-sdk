package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.schedule.*;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.squareup.okhttp.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.util.*;

/**
 * Public API that allows performing create/read/update/delete operations on report schedules
 *
 * <pre>
 * {@code
 *
 *    Server server = Server.builder()
 *            .withBaseUrl("http://mobiledemo2.jaspersoft.com/jasperserver-pro/")
 *            .build();
 *    Credentials credentials = SpringCredentials.builder()
 *            .withPassword("phoneuser")
 *            .withUsername("phoneuser")
 *            .withOrganization("organization_1")
 *            .build();
 *
 *    AuthorizedClient client = server.newClient(credentials).create();
 *    ReportScheduleRestApi reportScheduleRestApi = client.reportScheduleApi();
 *    String reportUri = "/my/report/uri";
 *
 *    try {
 *        Map<String, Object> params = new HashMap<>();
 *        params.put("reportUri", reportUri);
 *        List<JobUnitEntity> jobUnits = reportScheduleRestApi.searchJob(params);
 *        int jobId = 1000;
 *        JobFormEntity jobForm = reportScheduleRestApi.requestJob(jobId);
 *        JobFormEntity entity = new JobFormEntity();
 *        entity.setSourceUri(reportUri);
 *        JobDescriptor job = reportScheduleRestApi.createJob(entity);
 *        JobDescriptor updateJob = reportScheduleRestApi.updateJob(jobId, entity);
 *        Set<Integer> deletedJobIds = reportScheduleRestApi.deleteJobs(Collections.singleton(jobId));
 *    } catch (IOException e) {
 *        // handle socket issue
 *    } catch (HttpException e) {
 *        // handle network issue
 *    }
 * }
 * </pre>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class ReportScheduleRestApi {
    private final NetworkClient mNetworkClient;

    @TestOnly
    ReportScheduleRestApi(NetworkClient networkClient) {
        mNetworkClient = networkClient;
    }

    /**
     * Performs search by specified criteria for jobs on server side
     *
     * @param searchParams one used to specify concrete requests
     * @return list of jobs as result of search operation
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public List<JobUnitEntity> searchJob(@NotNull Map<String, Object> searchParams) throws IOException, HttpException {
        if (searchParams != null && searchParams.containsKey("example")) {
            throw new IllegalArgumentException("Current version does not support 'example' search parameter");
        }

        HttpUrl url = mNetworkClient.getBaseUrl().newBuilder()
                .addPathSegment("rest_v2")
                .addPathSegment("jobs")
                .build();
        url = QueryMapper.INSTANCE.mapParams(searchParams, url);

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .get()
                .url(url)
                .build();

        Response response = mNetworkClient.makeCall(request);
        if (response.code() == 204) {
            return Collections.emptyList();
        }
        JobsSearchResult searchResult = mNetworkClient.deserializeJson(response, JobsSearchResult.class);
        return Collections.unmodifiableList(searchResult.getJobSummary());
    }

    /**
     * Allows to create new schedule job
     *
     * @param form data that describes format of schedule operation
     * @return descriptor if a job was successfully created
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public JobDescriptor createJob(@NotNull JobFormEntity form) throws IOException, HttpException {
        return alterJob(form, null);
    }

    /**
     * Updates the schedule job specified by id
     *
     * @param jobId unique identifier associated with job
     * @param form  data that describes format of schedule operation
     * @return descriptor if a job was successfully updated
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public JobDescriptor updateJob(int jobId, @NotNull JobFormEntity form) throws IOException, HttpException {
        return alterJob(form, jobId);
    }

    private JobDescriptor alterJob(JobFormEntity form, Integer id) throws IOException, HttpException {
        Preconditions.checkNotNull(form, "Job form should not be null");

        String suffix = id == null ? "" : "/" + id;
        HttpUrl url = mNetworkClient.getBaseUrl()
                .resolve("rest_v2/jobs" + suffix);

        MediaType mediaType = MediaType.parse("application/job+json; charset=UTF-8");
        RequestBody jsonRequestBody = mNetworkClient.createRequestBody(form, mediaType);
        Request.Builder request = new Request.Builder()
                .addHeader("Accept", "application/job+json; charset=UTF-8")
                .url(url);

        if (id == null) {
            request.put(jsonRequestBody);
        } else {
            request.post(jsonRequestBody);
        }

        Response response = mNetworkClient.makeCall(request.build());
        return mNetworkClient.deserializeJson(response, JobDescriptor.class);
    }

    /**
     * Reads particular job form data
     *
     * @param jobId unique identifier associated with job
     * @return data that describes format of schedule operation
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public JobFormEntity requestJob(int jobId) throws IOException, HttpException {
        HttpUrl url = mNetworkClient.getBaseUrl().resolve("rest_v2/jobs/" + jobId);
        Request request = new Request.Builder()
                .get()
                .addHeader("Accept", "application/job+json; charset=UTF-8")
                .url(url)
                .build();
        Response response = mNetworkClient.makeCall(request);
        return mNetworkClient.deserializeJson(response, JobFormEntity.class);
    }

    /**
     * Deletes jobs in batch format
     *
     * @param jobIds set of job ids that are targeted for deletion
     * @return ids set of deleted jobs
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public Set<Integer> deleteJobs(@NotNull Set<Integer> jobIds) throws IOException, HttpException {
        Preconditions.checkNotNull(jobIds, "Job ids should not be null");
        Preconditions.checkArgument(!jobIds.isEmpty(), "Job ids should not be empty");

        HttpUrl.Builder urlBuilder = mNetworkClient.getBaseUrl().resolve("rest_v2/jobs")
                .newBuilder();
        for (Integer jobId : jobIds) {
            urlBuilder.addQueryParameter("id", String.valueOf(jobId));
        }

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .url(urlBuilder.build())
                .delete()
                .build();

        Response response = mNetworkClient.makeCall(request);
        JobIdsList jobIdsList = mNetworkClient.deserializeJson(response, JobIdsList.class);
        return Collections.unmodifiableSet(jobIdsList.getJobId());
    }
}
