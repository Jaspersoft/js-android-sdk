/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.report.ReportMetadata;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ReportExecutionDescriptor.class})
public class AbstractReportExecutionTest {

    private static final String EXEC_ID = "exec_id";
    private static final String REPORT_URI = "my/uri";
    private static final int TOTAL_PAGES = 9999;

    @Mock
    ReportExecutionApi mReportExecutionApi;
    @Mock
    ReportExecutionDescriptor mReportExecutionDescriptor;

    private AbstractReportExecution abstractReportExecution;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        abstractReportExecution = new AbstractReportExecution(mReportExecutionApi, EXEC_ID, REPORT_URI, 0) {
            @NotNull
            @Override
            public ReportExport export(@NotNull ReportExportOptions options) throws ServiceException {
                return null;
            }

            @NotNull
            @Override
            public ReportExecution updateExecution(@Nullable List<ReportParameter> newParameters) throws ServiceException {
                return null;
            }
        };
        when(mReportExecutionApi.getDetails(anyString())).thenReturn(mReportExecutionDescriptor);
        when(mReportExecutionDescriptor.getTotalPages()).thenReturn(TOTAL_PAGES);
    }

    @Test
    public void testWaitForReportCompletion() throws Exception {
        ReportMetadata metadata = abstractReportExecution.waitForReportCompletion();
        assertThat("Failed to create report metadata with exact total pages", metadata.getUri(), is(REPORT_URI));
        assertThat("Failed to create report metadata with exact report uri", metadata.getTotalPages(), is(TOTAL_PAGES));
        verify(mReportExecutionApi).awaitStatus(EXEC_ID, REPORT_URI, 0, Status.ready());
        verify(mReportExecutionApi).getDetails(EXEC_ID);
    }
}