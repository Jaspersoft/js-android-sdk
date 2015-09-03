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
import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.api.RestApiLogLevel;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatusResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.export.ExportResourceResponse;
import com.jaspersoft.android.sdk.network.entity.export.ReportExportExecutionResponse;
import com.jaspersoft.android.sdk.network.entity.server.AuthResponse;
import com.jaspersoft.android.sdk.test.TestLogger;

import org.junit.Test;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportExportRestApiTest {
    String mobileDemo2 = "http://mobiledemo2.jaspersoft.com/jasperserver-pro";
    String reportUri = "/public/Samples/Reports/AllAccounts";
    AuthResponse mAuthResponse;
    ReportExecutionRestApi mExecApi;
    ReportExportRestApi mExportApi;

    @Test
    public void runExportRequestShouldReturnResult() {
        ReportExecutionDetailsResponse exec = startExecution();
        ReportExportExecutionResponse execDetails = startExportExecution(exec);
        assertThat(execDetails.getExportId(), is(notNullValue()));
    }

    @Test
    public void checkExportRequestStatusShouldReturnResult() throws IOException {
        ReportExecutionDetailsResponse exec = startExecution();
        ReportExportExecutionResponse execDetails = startExportExecution(exec);
        Call<ExecutionStatusResponse> call = getApi().checkExportExecutionStatus(exec.getExecutionId(), execDetails.getExportId());
        Response<ExecutionStatusResponse> response = call.execute();
        assertThat(response, is(notNullValue()));
    }

    @Test
    public void requestExportOutputShouldReturnResult() {
        ReportExecutionDetailsResponse exec = startExecution();
        ReportExportExecutionResponse execDetails = startExportExecution(exec);
        ExportResourceResponse output = getApi().requestExportOutput(exec.getExecutionId(), execDetails.getExportId());
        assertThat(output.getExportInput(), is(notNullValue()));
        assertThat(output.getPages(), is("1-2"));
        assertThat(output.isFinal(), is(false));
    }

    @NonNull
    private ReportExportExecutionResponse startExportExecution(ReportExecutionDetailsResponse exec) {
        ExecutionRequestOptions options = ExecutionRequestOptions.newInstance()
                .withPages("1-2")
                .withOutputFormat("PDF");
        Call<ReportExportExecutionResponse> call = getApi().runExportExecution(exec.getExecutionId(), options);
        try {
            Response<ReportExportExecutionResponse> response = call.execute();
            return response.body();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @NonNull
    private ReportExecutionDetailsResponse startExecution() {
        ReportExecutionRequestOptions executionRequestOptions = ReportExecutionRequestOptions.newRequest(reportUri);
        Call<ReportExecutionDetailsResponse> call = getReportExecApi().runReportExecution(executionRequestOptions);

        try {
            Response<ReportExecutionDetailsResponse> response = call.execute();
            return response.body();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private ReportExportRestApi getApi() {
        if (mExportApi == null) {
            mExportApi = new ReportExportRestApi.Builder(mobileDemo2, getAuthResponse().getToken())
                    .setLog(TestLogger.get(this))
                    .setLogLevel(RestApiLogLevel.FULL)
                    .build();
        }
        return mExportApi;
    }

    private ReportExecutionRestApi getReportExecApi() {
        if (mExecApi == null) {
            mExecApi = new ReportExecutionRestApi.Builder(mobileDemo2, getAuthResponse().getToken())
                    .setLog(TestLogger.get(this))
                    .setLogLevel(RestApiLogLevel.FULL)
                    .build();
        }
        return mExecApi;
    }

    private AuthResponse getAuthResponse() {
        if (mAuthResponse == null) {
            AuthenticationRestApi restApi = new AuthenticationRestApi.Builder(mobileDemo2).build();
            mAuthResponse = restApi.authenticate("joeuser", "joeuser", "organization_1", null);
        }
        return mAuthResponse;
    }
}
