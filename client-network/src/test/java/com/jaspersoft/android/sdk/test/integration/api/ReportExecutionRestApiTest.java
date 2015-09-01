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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.httpclient.FakeHttp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ReportExecutionRestApiTest {

    String mobileDemo2 = "http://mobiledemo2.jaspersoft.com/jasperserver-pro";
    String reportUri = "/public/Samples/Reports/AllAccounts";
    AuthResponse mAuthResponse;
    ReportExecutionRestApi mApi;

    @Before
    public void setup() {
        FakeHttp.getFakeHttpLayer().interceptHttpRequests(false);
    }

    @Test
    public void shouldStartReportExecution() {
        ReportExecutionDetailsResponse response = startExecution();
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatus(), is(notNullValue()));
    }

    @Test
    public void shouldCancelReportExecution() {
        ReportExecutionRestApi api = getApi();
        ReportExecutionDetailsResponse response = startExecution();
        boolean cancelled = api.cancelReportExecution(response.getExecutionId());
        assertThat(cancelled, is(true));
    }

    @Test
    public void shouldReturnReportExecutionDetails() {
        ReportExecutionRestApi api = getApi();
        ReportExecutionDetailsResponse executionResponse = startExecution();

        String executionId = executionResponse.getExecutionId();
        ReportExecutionDetailsResponse response = api.requestReportExecutionDetails(executionResponse.getExecutionId());
        assertThat(response.getExecutionId(), is(executionId));
    }

    @Test
    public void shouldCheckReportExecutionStatus() {
        ReportExecutionRestApi api = getApi();
        ReportExecutionDetailsResponse executionResponse = startExecution();

        ExecutionStatusResponse response = api.requestReportExecutionStatus(executionResponse.getExecutionId());
        assertThat(response.getStatus(), is(notNullValue()));
    }

    @Test
    public void searchForExecutionShouldReturnResult() {
        ReportExecutionRestApi api = getApi();
        ReportExecutionDetailsResponse executionResponse = startExecution();

        Map<String, String> params = new HashMap<>();
        params.put("reportURI", executionResponse.getReportURI());

        ReportExecutionSearchResponse searchResponse = api.searchReportExecution(params);
        assertThat(searchResponse.getItems(), is(not(empty())));
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
        ReportExecutionRequestOptions executionRequestOptions = ReportExecutionRequestOptions.newRequest(reportUri);
        return getApi().runReportExecution(executionRequestOptions);
    }

    private ReportExecutionRestApi getApi() {
        if (mApi == null) {
            mApi = new ReportExecutionRestApi.Builder(mobileDemo2, getAuthResponse().getToken())
                    .setLog(TestLogger.get(this))
                    .setLogLevel(RestApiLogLevel.FULL)
                    .build();
        }
        return mApi;
    }

    private AuthResponse getAuthResponse() {
        if (mAuthResponse == null) {
            AuthenticationRestApi restApi = new AuthenticationRestApi.Builder(mobileDemo2).build();
            mAuthResponse = restApi.authenticate("joeuser", "joeuser", "organization_1", null);
        }
        return mAuthResponse;
    }
}
