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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.List;

import static com.jaspersoft.android.sdk.service.report.Status.execution;
import static com.jaspersoft.android.sdk.service.report.Status.ready;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ReportExecutionDescriptor.class,
})
public class AbstractReportServiceTest {

    private static final String REPORT_URI = "my/uri";
    private static final String EXEC_ID = "exec_id";
    private static final String OPTION_LABEL = "label";
    private static final String OPTION_ID = OPTION_LABEL;
    private static final List<ReportParameter> REPORT_PARAMETERS = Collections.emptyList();

    @Mock
    ExportExecutionApi mExportExecutionApi;
    @Mock
    ReportExecutionApi mReportExecutionApi;
    @Mock
    ExportFactory mExportFactory;

    @Mock
    ReportExecutionDescriptor mReportExecutionDescriptor;

    private AbstractReportService reportService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        reportService = new AbstractReportService(
                mExportExecutionApi,
                mReportExecutionApi,
                mExportFactory,
                0) {
            @Override
            protected ReportExecution buildExecution(String reportUri,
                                                     String executionId,
                                                     ReportExecutionOptions criteria) {
                return null;
            }
        };
        reportService = spy(reportService);
        when(mReportExecutionApi.start(anyString(), any(ReportExecutionOptions.class))).thenReturn(mReportExecutionDescriptor);
        when(mReportExecutionDescriptor.getExecutionId()).thenReturn(EXEC_ID);
    }

    @Test
    public void testRun() throws Exception {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder().build();
        reportService.run(REPORT_URI, criteria);

        verify(mReportExecutionApi).start(REPORT_URI, criteria);
        verify(mReportExecutionApi).awaitStatus(EXEC_ID, REPORT_URI, 0, execution(), ready());
        verify(reportService).buildExecution(REPORT_URI, EXEC_ID, criteria);
    }
}