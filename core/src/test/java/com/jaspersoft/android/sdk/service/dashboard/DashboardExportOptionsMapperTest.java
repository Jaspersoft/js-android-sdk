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

import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecutionOptions;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardParameters;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.dashboard.DashboardExportFormat;
import com.jaspersoft.android.sdk.service.data.dashboard.DashboardExportOptions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DashboardExportOptionsMapperTest {
    private static final String RESOURCE_URI = "res_uri";
    private static final Integer RESOURCE_WIDTH = 1280;
    private static final Integer RESOURCE_HEIGHT = 720;
    private static final DashboardExportFormat RESOURCE_FORMAT = DashboardExportFormat.PDF;
    private static final List<ReportParameter> RESOURCE_PARAMS = new ArrayList<ReportParameter>(){
        {add(new ReportParameter("param1", null));}
    };

    @Mock
    DashboardExportOptions dashboardExportOptions;
    @Mock
    InputStream stream;

    private DashboardExportOptionsMapper mapper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mapper = new DashboardExportOptionsMapper();
    }

    @Test
    public void testTransform() throws Exception {
        DashboardExportExecutionOptions dashboardExportExecutionOptions =
                new DashboardExportExecutionOptions(RESOURCE_URI, RESOURCE_WIDTH, RESOURCE_HEIGHT, "pdf", new DashboardParameters(RESOURCE_PARAMS));

        when(dashboardExportOptions.getUri()).thenReturn(RESOURCE_URI);
        when(dashboardExportOptions.getWidth()).thenReturn(RESOURCE_WIDTH);
        when(dashboardExportOptions.getHeight()).thenReturn(RESOURCE_HEIGHT);
        when(dashboardExportOptions.getFormat()).thenReturn(RESOURCE_FORMAT);
        when(dashboardExportOptions.getParameters()).thenReturn(RESOURCE_PARAMS);

        DashboardExportExecutionOptions options = mapper.transform(dashboardExportOptions);
        assertThat(options, is(equalTo(dashboardExportExecutionOptions)));
    }
}