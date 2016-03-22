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
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ExportExecutionDescriptor.class,
        ReportExecutionDescriptor.class,
})
public class ReportExecution5_6PlusTest {
    private static final ReportExportOptions EXPORT_OPTIONS = FakeOptions.export();
    private static final List<ReportParameter> REPORT_PARAMS = Collections.<ReportParameter>emptyList();

    private static final String EXEC_ID = "exec_id";
    private static final String EXPORT_ID = "export_id";
    private static final String REPORT_URI = "my/uri";

    @Mock
    ExportExecutionApi mExportExecutionApi;
    @Mock
    ReportExecutionApi mReportExecutionApi;
    @Mock
    ExportFactory mExportFactory;
    @Mock
    ExportIdWrapper mExportIdWrapper;

    @Mock
    ExportExecutionDescriptor exportDescriptor;
    @Mock
    ReportExecutionDescriptor reportDescriptor;

    @Mock
    ReportExecutionOptions.Builder mCriteriaBuilder;

    private ReportExecution reportExecution;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        reportExecution = new ReportExecution5_6Plus(
                mExportExecutionApi,
                mReportExecutionApi,
                mExportIdWrapper,
                mExportFactory,
                EXEC_ID,
                REPORT_URI,
                0);
    }

    @Test
    public void testExport() throws Exception {
        when(mReportExecutionApi.getDetails(anyString())).thenReturn(reportDescriptor);
        when(exportDescriptor.getExportId()).thenReturn(EXPORT_ID);
        when(mExportExecutionApi.start(anyString(), any(ReportExportOptions.class))).thenReturn(exportDescriptor);

        reportExecution.export(EXPORT_OPTIONS);

        verify(mExportExecutionApi).start(EXEC_ID, EXPORT_OPTIONS);
        verify(mExportExecutionApi).awaitReadyStatus(EXEC_ID, EXPORT_ID, REPORT_URI, 0);
        verify(mReportExecutionApi).getDetails(EXEC_ID);
        verify(mExportFactory).create(reportDescriptor, EXEC_ID, mExportIdWrapper);
    }

    @Test
    public void testUpdateExecution() throws Exception {
        ReportExecution newExecution = reportExecution.updateExecution(REPORT_PARAMS);
        assertThat("For servers 5.6+ we return same report execution", newExecution, is(reportExecution));

        verify(mReportExecutionApi).update(EXEC_ID, REPORT_PARAMS);
        verify(mReportExecutionApi).awaitStatus(EXEC_ID, REPORT_URI, 0, Status.execution(), Status.ready());
    }
}