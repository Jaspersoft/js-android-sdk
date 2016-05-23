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

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RetryReportExecutionTest {

    private static final ReportExportOptions EXPORT_CRITERIA = FakeOptions.export();
    private static final ServiceException CANCELLED_EXCEPTION = new ServiceException(null, null, StatusCodes.EXPORT_EXECUTION_CANCELLED);
    private static final List<ReportParameter> REPORT_PARAMS = Collections.<ReportParameter>emptyList();

    @Mock
    ReportExecution mDelegate;
    @Mock
    ReportExport mReportExport;

    private RetryReportExecution reportExecution;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        reportExecution = new RetryReportExecution(mDelegate);
    }

    @Test
    public void should_retry_export_if_cancelled_on_first_run() throws Exception {
        when(mDelegate.export(any(ReportExportOptions.class))).then(crashOnFirstRun());
        reportExecution.export(EXPORT_CRITERIA);
        verify(mDelegate, times(2)).export(EXPORT_CRITERIA);
    }

    @Test(expected = ServiceException.class)
    public void should_throw_if_cancelled_on_second_run() throws Exception {
        when(mDelegate.export(any(ReportExportOptions.class))).thenThrow(CANCELLED_EXCEPTION);
        reportExecution.export(EXPORT_CRITERIA);
    }

    @Test
    public void should_delegate_waitForReportCompletion() throws Exception {
        reportExecution.waitForReportCompletion();
        verify(mDelegate).waitForReportCompletion();
    }

    @Test
    public void should_delegate_updateExecution() throws Exception {
        reportExecution.updateExecution(REPORT_PARAMS);
        verify(mDelegate).updateExecution(REPORT_PARAMS);
    }

    private Answer<Object> crashOnFirstRun() {
        return new Answer<Object>() {
            private boolean thrown;
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if (!thrown) {
                    thrown = true;
                    throw CANCELLED_EXCEPTION;
                }
                return mReportExport;
            }
        };
    }
}