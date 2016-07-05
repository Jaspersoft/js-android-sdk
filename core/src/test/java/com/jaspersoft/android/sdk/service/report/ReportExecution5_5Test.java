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

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ExportExecutionDescriptor.class,
        ReportExecutionDescriptor.class,
        ReportExecutionOptions.class,
})
public class ReportExecution5_5Test {
    private static final ReportExecutionOptions REPORT_OPTIONS = FakeOptions.report();
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

    private ReportExecution5_5 reportExecution;
    private ReportExecutionOptions initialCriteria;
    private ReportExecutionOptions newCriteria;
    private ReportExecutionOptions.Builder criteriaBuilder;
    private ReportExecution newExecution;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        reportExecution = new ReportExecution5_5(
                mExportExecutionApi,
                mReportExecutionApi,
                mExportIdWrapper,
                mExportFactory,
                REPORT_OPTIONS,
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
        verify(mReportExecutionApi).getDetails(EXEC_ID);
        verify(mExportFactory).create(reportDescriptor, EXEC_ID, mExportIdWrapper);
    }

    @Test
    public void testApplyParams() throws Exception {
        givenInitialCriteria();
        giveReportDescriptorForExecId(EXEC_ID);

        whenAppliesParams();

        thenShouldYieldNewExecution();
        thenShouldCreateNewCriteria();
        thenShouldStartExecutionWithNewParams();
        thenShouldWaitForStatuses(Status.execution(), Status.ready());
    }

    @Test
    public void testRefresh() throws Exception {
        givenInitialCriteria();
        giveReportDescriptorForExecId(EXEC_ID);

        whenRefreshesExecution();

        thenShouldYieldNewExecution();
        thenShouldCreateNewCriteria();
        thenShouldStartExecutionWithSameParams();
        thenShouldWaitForStatuses(Status.execution(), Status.ready());
    }

    private void givenInitialCriteria() {
        initialCriteria = spy(REPORT_OPTIONS);
        criteriaBuilder = spy(initialCriteria.newBuilder());
        newCriteria = criteriaBuilder.withParams(REPORT_PARAMS).build();

        when(criteriaBuilder.build()).thenReturn(newCriteria);
        when(initialCriteria.newBuilder()).thenReturn(criteriaBuilder);
    }

    private void giveReportDescriptorForExecId(String execId) throws ServiceException {
        when(mReportExecutionApi.start(anyString(), any(ReportExecutionOptions.class))).thenReturn(reportDescriptor);
        when(reportDescriptor.getExecutionId()).thenReturn(execId);
    }

    private void whenAppliesParams() throws ServiceException {
        newExecution = reportExecution.updateExecution(REPORT_PARAMS);
    }

    private void whenRefreshesExecution() throws ServiceException {
        newExecution = reportExecution.refresh();
    }

    private void thenShouldYieldNewExecution() {
        assertThat(newExecution, is(instanceOf(ReportExecution5_5.class)));
        assertThat(newExecution, is(notNullValue()));
    }

    private void thenShouldWaitForStatuses(Status... statuses) throws ServiceException {
        verify(mReportExecutionApi).awaitStatus(EXEC_ID, REPORT_URI, 0, statuses);
    }

    private void thenShouldStartExecutionWithNewParams() throws ServiceException {
        verify(mReportExecutionApi).start(eq(REPORT_URI), not(eq(REPORT_OPTIONS)));
    }

    private void thenShouldStartExecutionWithSameParams() throws ServiceException {
        verify(mReportExecutionApi).start(eq(REPORT_URI), eq(REPORT_OPTIONS));
    }

    private void thenShouldCreateNewCriteria() {
        verify(initialCriteria).newBuilder();
        verify(criteriaBuilder).withParams(REPORT_PARAMS);
        verify(criteriaBuilder).build();
    }
}