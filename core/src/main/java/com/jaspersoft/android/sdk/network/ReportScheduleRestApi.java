package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.schedule.*;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.squareup.okhttp.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportScheduleRestApi {
    private final NetworkClient mNetworkClient;

    @TestOnly
    ReportScheduleRestApi(NetworkClient networkClient) {
        mNetworkClient = networkClient;
    }

    @NotNull
    public List<JobUnitEntity> searchJob(@Nullable Map<String, Object> searchParams) throws IOException, HttpException {
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
        JobsSearchResult searchResult = mNetworkClient.deserializeJson(response, JobsSearchResult.class);
        return Collections.unmodifiableList(searchResult.getJobSummary());
    }

    @NotNull
    public JobDescriptor createJob(@NotNull JobFormEntity form) throws IOException, HttpException {
        Preconditions.checkNotNull(form, "Job form should not be null");

        HttpUrl url = mNetworkClient.getBaseUrl().resolve("rest_v2/jobs");

        MediaType mediaType = MediaType.parse("application/job+json; charset=UTF-8");
        RequestBody jsonRequestBody = mNetworkClient.createRequestBody(form, mediaType);
        Request request = new Request.Builder()
                .put(jsonRequestBody)
                .addHeader("Accept", "application/job+json; charset=UTF-8")
                .url(url)
                .build();

        Response response = mNetworkClient.makeCall(request);
        return mNetworkClient.deserializeJson(response, JobDescriptor.class);
    }

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
