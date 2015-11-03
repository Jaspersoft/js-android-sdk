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

import com.jaspersoft.android.sdk.network.api.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionSearchResponse;
import com.jaspersoft.android.sdk.test.TestLogger;
import com.jaspersoft.android.sdk.test.integration.api.utils.DummyTokenProvider;
import com.jaspersoft.android.sdk.test.integration.api.utils.JrsMetadata;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private final String REPORT_URI = "/public/Samples/Reports/ProfitDetailReport";

    ReportExecutionRestApi apiUnderTest;

    private final JrsMetadata mMetadata = JrsMetadata.createMobileDemo2();
    private final DummyTokenProvider mAuthenticator = DummyTokenProvider.create(mMetadata);

    @Before
    public void setup() {
        if (apiUnderTest == null) {
            apiUnderTest = new ReportExecutionRestApi.Builder()
                    .baseUrl(mMetadata.getServerUrl())
                    .logger(TestLogger.get(this))
                    .build();
        }
    }

    @Test
    public void shouldStartReportExecution() {
        ReportExecutionDescriptor response = startExecution();
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatus(), is(notNullValue()));
    }

    /**
     * TODO: TEST IS FLAKY provide workaround
     */
    @Ignore
    public void shouldCancelReportExecution() throws InterruptedException {
        ReportExecutionDescriptor response = startExecution();
        boolean cancelled = apiUnderTest.cancelReportExecution(mAuthenticator.token(), response.getExecutionId());
        assertThat(cancelled, is(true));
    }

    @Test
    public void shouldReturnReportExecutionDetails() throws IOException {
        ReportExecutionDescriptor executionResponse = startExecution();

        String executionId = executionResponse.getExecutionId();
        ReportExecutionDescriptor response = apiUnderTest.requestReportExecutionDetails(mAuthenticator.token(), executionResponse.getExecutionId());
        assertThat(response.getExecutionId(), is(executionId));
    }

    @Test
    public void shouldCheckReportExecutionStatus() throws IOException {
        ReportExecutionDescriptor executionResponse = startExecution();

        ExecutionStatus response = apiUnderTest.requestReportExecutionStatus(mAuthenticator.token(), executionResponse.getExecutionId());
        assertThat(response.getStatus(), is(notNullValue()));
    }

    /**
     * TODO: API is broken requires investigation before release
     */
    @Ignore
    public void searchForExecutionShouldReturnResult() throws IOException {
        ReportExecutionDescriptor executionResponse = startExecution();

        Map<String, String> params = new HashMap<>();
        params.put("reportURI", executionResponse.getReportURI());

        ReportExecutionSearchResponse response  = apiUnderTest.searchReportExecution(mAuthenticator.token(), params);
        assertThat(response.getItems(), is(not(empty())));
    }

    @Test
    public void updateOfParametersForExecutionShouldReturnResult() {
        List<Map<String, Set<String>>> list = new ArrayList<>();

        Map<String, Set<String>> reportParameter = new HashMap<>();
        reportParameter.put("ProductFamily", new HashSet<String>(Collections.singletonList("Drink")));
        list.add(reportParameter);

        ReportExecutionDescriptor executionResponse = startExecution();
        boolean success = apiUnderTest.updateReportExecution(mAuthenticator.token(), executionResponse.getExecutionId(), list);
        assertThat(success, is(true));
    }

    /**
     * Helper methods
     */
    @NotNull
    private ReportExecutionDescriptor startExecution() {
        return startExecution(REPORT_URI);
    }

    @NotNull
    private ReportExecutionDescriptor startExecution(String uri) {
        ReportExecutionRequestOptions executionRequestOptions = ReportExecutionRequestOptions.newRequest(uri);
        Map<String, Set<String>> params = new HashMap<>();
        params.put("ProductFamily", new HashSet<String>(Collections.singletonList("Food")));
        executionRequestOptions.withParameters(params);

        return apiUnderTest.runReportExecution(mAuthenticator.token(), executionRequestOptions);
    }
}
