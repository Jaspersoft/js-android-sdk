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

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecution;
import com.jaspersoft.android.sdk.service.data.dashboard.DashboardExportOptions;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DashboardServiceTest {
    private static final String EXPORT_ID = "export_id";

    @Mock
    AuthorizedClient authorizedClient;
    @Mock
    DashboardExportApi dashboardExportApi;
    @Mock
    DashboardExportExecution dashboardExportExecution;
    @Mock
    ServiceException serviceException;
    @Mock
    DashboardExportOptions dashboardExportOptions;
    @Mock
    ResourceOutput resourceOutput;

    @Rule
    public ExpectedException expected = none();

    private DashboardService dashboardService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        dashboardService = new DashboardService(dashboardExportApi);
    }

    @Test
    public void testInstanceCreationNullAuthClient() throws Exception {
        expected.expect(NullPointerException.class);
        DashboardService dashboardServiceTest = DashboardService.newService(null);
    }

    @Test
    public void testInstanceCreation() throws Exception {
        when(authorizedClient.dashboardExportApi()).thenReturn(null);
        DashboardService.newService(authorizedClient);
    }

    @Test
    public void testExport() throws Exception {
        when(dashboardExportApi.start(dashboardExportOptions)).thenReturn(dashboardExportExecution);
        when(dashboardExportApi.downloadExport(EXPORT_ID)).thenReturn(resourceOutput);
        when(dashboardExportExecution.getId()).thenReturn(EXPORT_ID);

        ResourceOutput resourceOutput = dashboardService.export(dashboardExportOptions);

        verify(dashboardExportApi).start(dashboardExportOptions);
        verify(dashboardExportApi).awaitReadyStatus(EXPORT_ID, TimeUnit.SECONDS.toMillis(1));
        verify(dashboardExportApi).downloadExport(EXPORT_ID);

        assertThat(resourceOutput, is(resourceOutput));
    }

    @Test
    public void testShouldThrowServiceException() throws Exception {
        when(dashboardExportApi.start(dashboardExportOptions)).thenThrow(serviceException);

        try {
            dashboardService.export(dashboardExportOptions);
            fail("Should fail with service exception");
        } catch (ServiceException ex) {
            assertThat(ex, is(serviceException));
        }
    }

    @Test
    public void testShouldNotExportWithNullParams() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Dashboard export options should not be null.");
        dashboardService.export(null);
    }
}