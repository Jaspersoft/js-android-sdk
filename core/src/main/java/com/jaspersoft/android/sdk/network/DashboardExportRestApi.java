package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecution;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecutionOptions;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class DashboardExportRestApi {
    private final NetworkClient mNetworkClient;

    DashboardExportRestApi(NetworkClient mNetworkClient) {
        this.mNetworkClient = mNetworkClient;
    }

    @NotNull
    public DashboardExportExecution createExportJob(@NotNull DashboardExportExecutionOptions dashboardExportExecutionOptions) throws IOException, HttpException {
        Utils.checkNotNull(dashboardExportExecutionOptions, "Dashboard export execution options should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("dashboardExecutions")
                .build()
                .resolve(mNetworkClient.getBaseUrl());

        RequestBody jsonRequestBody = mNetworkClient.createJsonRequestBody(dashboardExportExecutionOptions);
        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .post(jsonRequestBody)
                .url(url)
                .build();

        com.squareup.okhttp.Response response = mNetworkClient.makeCall(request);
        return mNetworkClient.deserializeJson(response, DashboardExportExecution.class);
    }

    @NotNull
    public DashboardExportExecutionStatus getExportJobStatus(@NotNull String jobId) throws IOException, HttpException {
        Utils.checkNotNull(jobId, "Dashboard export job id should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("dashboardExecutions")
                .addPath(jobId)
                .addPath("status")
                .build()
                .resolve(mNetworkClient.getBaseUrl());

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response response = mNetworkClient.makeCall(request);
        return mNetworkClient.deserializeJson(response, DashboardExportExecutionStatus.class);
    }

    @NotNull
    public OutputResource getExportJobResult(@NotNull String jobId) throws IOException, HttpException {
        Utils.checkNotNull(jobId, "Dashboard export job id should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("dashboardExecutions")
                .addPath(jobId)
                .addPath("outputResource")
                .build()
                .resolve(mNetworkClient.getBaseUrl());

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response response = mNetworkClient.makeCall(request);

        return new RetrofitOutputResource(response.body());
    }
}
