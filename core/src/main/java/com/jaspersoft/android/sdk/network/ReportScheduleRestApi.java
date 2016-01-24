package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.schedule.JobDescriptor;
import com.jaspersoft.android.sdk.network.entity.schedule.JobForm;
import com.jaspersoft.android.sdk.network.entity.schedule.JobUnit;
import com.jaspersoft.android.sdk.network.entity.schedule.JobsSearchResult;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    public List<JobUnit> searchJob(@Nullable Map<String, Object> searchParams) throws IOException, HttpException {
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
        return searchResult.getJobSummary();
    }

    @NotNull
    public JobDescriptor createJob(@NotNull JobForm form) throws IOException, HttpException {
        Preconditions.checkNotNull(form, "Job form should not be null");

        HttpUrl url = mNetworkClient.getBaseUrl().resolve("rest_v2/jobs");

        RequestBody jsonRequestBody = mNetworkClient.createJsonRequestBody(form);
        Request request = new Request.Builder()
                .addHeader("Accept", "application/job+json; charset=UTF-8")
                .addHeader("Content-Type", "application/job+json; charset=UTF-8")
                .put(jsonRequestBody)
                .url(url)
                .build();

        Response response = mNetworkClient.makeCall(request);
        return mNetworkClient.deserializeJson(response, JobDescriptor.class);
    }
}
