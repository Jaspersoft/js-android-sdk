/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.test.integration.api;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.export.ExportOutputResource;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportExportRestApiTest {
    private static final String REPORT_URI = "/public/Samples/Reports/AllAccounts";

    private final LazyClient mLazyClient = new LazyClient(JrsMetadata.createMobileDemo2());
    private ReportExecutionRestApi mExecApi;
    private ReportExportRestApi apiUnderTest;

    @Before
    public void setup() {
        if (apiUnderTest == null) {
            AuthorizedClient client = mLazyClient.getAuthorizedClient();
            mExecApi = client.reportExecutionApi();
            apiUnderTest = client.reportExportApi();
        }
    }

    @Test
    public void runExportRequestShouldReturnResult() throws Exception {
        ReportExecutionDescriptor exec = startExecution();
        ExportExecutionDescriptor execDetails = startExportExecution(exec);
        assertThat(execDetails.getExportId(), is(notNullValue()));
    }

    @Test
    public void checkExportRequestStatusShouldReturnResult() throws Exception {
        ReportExecutionDescriptor exec = startExecution();
        ExportExecutionDescriptor execDetails = startExportExecution(exec);
        ExecutionStatus response = apiUnderTest.checkExportExecutionStatus(exec.getExecutionId(), execDetails.getExportId());
        assertThat(response, is(notNullValue()));
    }

    @Test
    public void requestExportOutputShouldReturnResult() throws Exception {
        ReportExecutionDescriptor exec = startExecution();
        ExportExecutionDescriptor execDetails = startExportExecution(exec);
        ExportOutputResource output = apiUnderTest.requestExportOutput(exec.getExecutionId(), execDetails.getExportId());

        assertThat(output.getOutputResource(), is(notNullValue()));
        assertThat(output.getPages(), is("1-2"));
        assertThat(output.isFinal(), is(false));
    }

    /**
     * Helper methods
     */
    @NotNull
    private ExportExecutionDescriptor startExportExecution(ReportExecutionDescriptor exec) throws Exception {
        ExecutionRequestOptions options = ExecutionRequestOptions.create()
                .withPages("1-2")
                .withOutputFormat("PDF");
        return apiUnderTest.runExportExecution(exec.getExecutionId(), options);
    }

    @NotNull
    private ReportExecutionDescriptor startExecution() throws Exception {
        ReportExecutionRequestOptions executionRequestOptions = ReportExecutionRequestOptions.newRequest(REPORT_URI);
        return mExecApi.runReportExecution(executionRequestOptions);
    }
}
