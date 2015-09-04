/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.test.integration.api;

import android.support.annotation.NonNull;

import com.jaspersoft.android.sdk.network.api.AuthenticationRestApi;
import com.jaspersoft.android.sdk.network.api.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.api.RestApiLogLevel;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatusResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionSearchResponse;
import com.jaspersoft.android.sdk.network.entity.server.AuthResponse;
import com.jaspersoft.android.sdk.test.TestLogger;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.Response;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportExecutionRestApiTest {

    private final String MOBILE_DEMO2 = "http://mobiledemo2.jaspersoft.com/jasperserver-pro";
    private final String REPORT_URI1 = "/public/Samples/Reports/AllAccounts";
    private final String REPORT_URI2 = "/public/Samples/Reports/ProfitDetailReport";
    AuthResponse mAuthResponse;
    ReportExecutionRestApi mApi;

    @Test
    public void shouldStartReportExecution() {
        ReportExecutionDetailsResponse response = startExecution();
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatus(), is(notNullValue()));
    }

    /**
     * TODO: TEST IS FLAKY provide workaround
     */
    @Ignore
    public void shouldCancelReportExecution() throws InterruptedException {
        ReportExecutionRestApi api = getApi();
        ReportExecutionDetailsResponse response = startExecution();
        boolean cancelled = api.cancelReportExecution(response.getExecutionId());
        assertThat(cancelled, is(true));
    }

    @Test
    public void shouldReturnReportExecutionDetails() throws IOException {
        ReportExecutionRestApi api = getApi();
        ReportExecutionDetailsResponse executionResponse = startExecution();

        String executionId = executionResponse.getExecutionId();
        Call<ReportExecutionDetailsResponse> call = api.requestReportExecutionDetails(executionResponse.getExecutionId());
        Response<ReportExecutionDetailsResponse> response = call.execute();
        assertThat(response.body().getExecutionId(), is(executionId));
    }

    @Test
    public void shouldCheckReportExecutionStatus() throws IOException {
        ReportExecutionRestApi api = getApi();
        ReportExecutionDetailsResponse executionResponse = startExecution();

        Call<ExecutionStatusResponse> call = api.requestReportExecutionStatus(executionResponse.getExecutionId());
        Response<ExecutionStatusResponse> response = call.execute();
        assertThat(response.body().getStatus(), is(notNullValue()));
    }

    /**
     * TODO: API is broken requires investigation before release
     */
    @Ignore
    public void searchForExecutionShouldReturnResult() throws IOException {
        ReportExecutionRestApi api = getApi();
        ReportExecutionDetailsResponse executionResponse = startExecution();

        Map<String, String> params = new HashMap<>();
        params.put("reportURI", executionResponse.getReportURI());

        Call<ReportExecutionSearchResponse> call = api.searchReportExecution(params);
        Response<ReportExecutionSearchResponse> response = call.execute();
        assertThat(response.body().getItems(), is(not(empty())));
    }

    @Test
    public void updateOfParametersForExecutionShouldReturnResult() {
        ReportExecutionRestApi api = getApi();
        ReportExecutionDetailsResponse executionResponse = startExecution();

        boolean success = api.updateReportExecution(executionResponse.getExecutionId(), Collections.EMPTY_LIST);
        assertThat(success, is(true));
    }

    @NonNull
    private ReportExecutionDetailsResponse startExecution() {
        return startExecution(REPORT_URI1);
    }

    @NonNull
    private ReportExecutionDetailsResponse startExecution(String uri) {
        ReportExecutionRequestOptions executionRequestOptions = ReportExecutionRequestOptions.newRequest(uri);
        Call<ReportExecutionDetailsResponse> call = getApi().runReportExecution(executionRequestOptions);
        try {
            Response<ReportExecutionDetailsResponse> response = call.execute();
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ReportExecutionRestApi getApi() {
        if (mApi == null) {
            mApi = new ReportExecutionRestApi.Builder(MOBILE_DEMO2, getAuthResponse().getToken())
                    .setLog(TestLogger.get(this))
                    .setLogLevel(RestApiLogLevel.FULL)
                    .build();
        }
        return mApi;
    }

    private AuthResponse getAuthResponse() {
        if (mAuthResponse == null) {
            AuthenticationRestApi restApi = new AuthenticationRestApi.Builder(MOBILE_DEMO2).build();
            mAuthResponse = restApi.authenticate("joeuser", "joeuser", "organization_1", null)
                    .toBlocking().first();
        }
        return mAuthResponse;
    }
}
