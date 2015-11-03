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
import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.export.ExportOutputResource;
import com.jaspersoft.android.sdk.test.TestLogger;
import com.jaspersoft.android.sdk.test.integration.api.utils.DummyTokenProvider;
import com.jaspersoft.android.sdk.test.integration.api.utils.JrsMetadata;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportExportRestApiTest {
    private static final String REPORT_URI = "/public/Samples/Reports/AllAccounts";

    ReportExecutionRestApi mExecApi;
    ReportExportRestApi apiUnderTest;

    private final JrsMetadata mMetadata = JrsMetadata.createMobileDemo2();
    private final DummyTokenProvider mAuthenticator = DummyTokenProvider.create(mMetadata);

    @Before
    public void setup() {
        if (mExecApi == null) {
            mExecApi = new ReportExecutionRestApi.Builder()
                    .baseUrl(mMetadata.getServerUrl())
                    .logger(TestLogger.get(this))
                    .build();
        }

        if (apiUnderTest == null) {
            apiUnderTest = new ReportExportRestApi.Builder()
                    .baseUrl(mMetadata.getServerUrl())
                    .logger(TestLogger.get(this))
                    .build();
        }
    }

    @Test
    public void runExportRequestShouldReturnResult() {
        ReportExecutionDescriptor exec = startExecution();
        ExportExecutionDescriptor execDetails = startExportExecution(exec);
        assertThat(execDetails.getExportId(), is(notNullValue()));
    }

    @Test
    public void checkExportRequestStatusShouldReturnResult() throws IOException {
        ReportExecutionDescriptor exec = startExecution();
        ExportExecutionDescriptor execDetails = startExportExecution(exec);
        ExecutionStatus response = apiUnderTest.checkExportExecutionStatus(mAuthenticator.token(), exec.getExecutionId(), execDetails.getExportId());
        assertThat(response, is(notNullValue()));
    }

    @Test
    public void requestExportOutputShouldReturnResult() {
        ReportExecutionDescriptor exec = startExecution();
        ExportExecutionDescriptor execDetails = startExportExecution(exec);
        ExportOutputResource output = apiUnderTest.requestExportOutput(mAuthenticator.token(), exec.getExecutionId(), execDetails.getExportId());

        assertThat(output.getOutputResource(), is(notNullValue()));
        assertThat(output.getPages(), is("1-2"));
        assertThat(output.isFinal(), is(false));
    }

    /**
     * Helper methods
     */
    @NotNull
    private ExportExecutionDescriptor startExportExecution(ReportExecutionDescriptor exec) {
        ExecutionRequestOptions options = ExecutionRequestOptions.create()
                .withPages("1-2")
                .withOutputFormat("PDF");
        return apiUnderTest.runExportExecution(mAuthenticator.token(), exec.getExecutionId(), options);
    }

    @NotNull
    private ReportExecutionDescriptor startExecution() {
        ReportExecutionRequestOptions executionRequestOptions = ReportExecutionRequestOptions.newRequest(REPORT_URI);
        return mExecApi.runReportExecution(mAuthenticator.token(), executionRequestOptions);
    }
}
