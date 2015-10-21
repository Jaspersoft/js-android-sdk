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

package com.jaspersoft.android.sdk.client;

import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionResponse;
import com.jaspersoft.android.sdk.client.oxm.report.ReportStatus;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class ReportExecutionResponseTest {
    private ReportExecutionResponse reportExecutionResponse;

    @Before
    public void setUp() {
        reportExecutionResponse = new ReportExecutionResponse();
    }

    @Test
    public void test_getReportStatus_for_queued() {
        reportExecutionResponse.setStatus("queued");
        assertThat(reportExecutionResponse.getReportStatus(), is(ReportStatus.queued));
    }

    @Test
    public void test_getReportStatus_for_ready() {
        reportExecutionResponse.setStatus("ready");
        assertThat(reportExecutionResponse.getReportStatus(), is(ReportStatus.ready));
    }

    @Test
    public void test_getReportStatus_for_failed() {
        reportExecutionResponse.setStatus("failed");
        assertThat(reportExecutionResponse.getReportStatus(), is(ReportStatus.failed));
    }

    @Test
    public void test_getReportStatus_for_execution() {
        reportExecutionResponse.setStatus("execution");
        assertThat(reportExecutionResponse.getReportStatus(), is(ReportStatus.execution));
    }

    @Test
    public void test_getReportStatus_for_cancelled() {
        reportExecutionResponse.setStatus("cancelled");
        assertThat(reportExecutionResponse.getReportStatus(), is(ReportStatus.cancelled));
    }

}
