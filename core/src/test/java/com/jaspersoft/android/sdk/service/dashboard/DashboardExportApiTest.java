/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.service.dashboard;

import com.jaspersoft.android.sdk.network.DashboardExportRestApi;
import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecution;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecutionOptions;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.jaspersoft.android.sdk.service.data.dashboard.DashboardExportOptions;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DashboardExportApiTest {
    private static final long WAIT_TIME = 1;
    private static final String EXPORT_ID = "export_id";

    @Mock
    DashboardExportRestApi dashboardExportRestApi;
    @Mock
    ServiceExceptionMapper serviceExceptionMapper;
    @Mock
    DashboardExportOptionsMapper dashboardExportOptionsMapper;
    @Mock
    DashboardExportMapper dashboardExportMapper;
    @Mock
    DashboardExportOptions dashboardExportOptions;
    @Mock
    DashboardExportExecutionOptions options;
    @Mock
    DashboardExportExecution dashboardExportExecution;
    @Mock
    OutputResource outputResource;
    @Mock
    ResourceOutput resourceOutput;
    @Mock
    DashboardExportExecutionStatus dashboardExportExecutionStatus;
    @Mock
    ServiceException serviceException;
    @Mock
    HttpException httpException;
    @Mock
    IOException ioException;

    @Rule
    public ExpectedException expected = none();

    private DashboardExportApi dashboardExportApi;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        dashboardExportApi =
                new DashboardExportApi(dashboardExportRestApi, serviceExceptionMapper, dashboardExportOptionsMapper, dashboardExportMapper);
        when(dashboardExportOptionsMapper.transform(dashboardExportOptions)).thenReturn(options);
    }

    @Test
    public void testStartExport() throws Exception {
        when(dashboardExportRestApi.createExportJob(options)).thenReturn(dashboardExportExecution);

        DashboardExportExecution dashboardExportExecutionTest = dashboardExportApi.start(dashboardExportOptions);
        assertThat(dashboardExportExecutionTest, is(dashboardExportExecution));
    }

    @Test
    public void testStartExportHttpException() throws Exception {
        when(serviceException.getMessage()).thenReturn("Http exception!");
        when(dashboardExportRestApi.createExportJob(options)).thenThrow(httpException);
        when(serviceExceptionMapper.transform(httpException)).thenReturn(serviceException);

        expected.expect(ServiceException.class);
        expected.expectMessage("Http exception!");

        dashboardExportApi.start(dashboardExportOptions);
    }

    @Test
    public void testStartExportIoException() throws Exception {
        when(serviceException.getMessage()).thenReturn("IO exception!");
        when(dashboardExportRestApi.createExportJob(options)).thenThrow(ioException);
        when(serviceExceptionMapper.transform(ioException)).thenReturn(serviceException);

        expected.expect(ServiceException.class);
        expected.expectMessage("IO exception!");

        dashboardExportApi.start(dashboardExportOptions);
    }

    @Test(timeout = 2000)
    public void testStatusAwaitReady() throws Exception {
        when(dashboardExportExecutionStatus.getStatus()).thenReturn("ready");
        when(dashboardExportRestApi.getExportJobStatus(EXPORT_ID)).thenReturn(dashboardExportExecutionStatus);

        dashboardExportApi.awaitReadyStatus(EXPORT_ID, WAIT_TIME);
    }

    @Test(timeout = 2000)
    public void testStatusAwaitFailed() throws Exception {
        when(dashboardExportExecutionStatus.getStatus()).thenReturn("failed");
        when(dashboardExportRestApi.getExportJobStatus(EXPORT_ID)).thenReturn(dashboardExportExecutionStatus);

        try {
            dashboardExportApi.awaitReadyStatus(EXPORT_ID, WAIT_TIME);
            fail("Should fail with service exception");
        } catch (ServiceException ex) {
            assertThat(ex.code(), is((StatusCodes.EXPORT_EXECUTION_FAILED)));
        }
    }

    @Test
    public void testDownloadExport() throws Exception {
        when(dashboardExportRestApi.getExportJobResult(EXPORT_ID)).thenReturn(outputResource);
        when(dashboardExportMapper.transform(outputResource)).thenReturn(resourceOutput);

        ResourceOutput result = dashboardExportApi.downloadExport(EXPORT_ID);
        assertThat(result, is(resourceOutput));
    }

    @Test
    public void testDownloadExportHttpException() throws Exception {
        when(serviceException.getMessage()).thenReturn("Http exception!");
        when(dashboardExportRestApi.getExportJobResult(EXPORT_ID)).thenThrow(httpException);
        when(serviceExceptionMapper.transform(httpException)).thenReturn(serviceException);

        expected.expect(ServiceException.class);
        expected.expectMessage("Http exception!");

        dashboardExportApi.downloadExport(EXPORT_ID);
    }

    @Test
    public void testDownloadExportIoException() throws Exception {
        when(serviceException.getMessage()).thenReturn("IO exception!");
        when(dashboardExportRestApi.getExportJobResult(EXPORT_ID)).thenThrow(ioException);
        when(serviceExceptionMapper.transform(ioException)).thenReturn(serviceException);

        expected.expect(ServiceException.class);
        expected.expectMessage("IO exception!");

        dashboardExportApi.downloadExport(EXPORT_ID);
    }
}