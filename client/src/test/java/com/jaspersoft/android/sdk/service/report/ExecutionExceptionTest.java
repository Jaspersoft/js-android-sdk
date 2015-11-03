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

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.service.report.exception.ReportExportException;
import com.jaspersoft.android.sdk.service.report.exception.ReportRunException;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ExecutionExceptionTest {

    @Test
    public void testReportFailed() throws Exception {
        ExecutionException ex = ExecutionException.reportFailed("/my/uri");
        assertThat(ex.getMessage(), is("Report execution '/my/uri' failed on server side"));
    }

    @Test
    public void testReportFailedWithThrowable() throws Exception {
        ExecutionException ex = ExecutionException.reportFailed("/my/uri", new InterruptedException("interrupted"));
        assertThat(ex.getMessage(), is("Report execution '/my/uri' failed. Reason: interrupted"));
        assertThat(ex.getCause(), is(instanceOf(InterruptedException.class)));
    }

    @Test
    public void testReportCancelled() throws Exception {
        ExecutionException ex = ExecutionException.reportCancelled("/my/uri");
        assertThat(ex.getMessage(), is("Report execution '/my/uri' was cancelled"));
    }

    @Test
    public void testExportFailed() throws Exception {
        ExecutionException ex = ExecutionException.exportFailed("/my/uri");
        assertThat(ex.getMessage(), is("Export for report '/my/uri' failed on server side"));
    }

    @Test
    public void testExportFailedWithThrowable() throws Exception {
        ExecutionException ex = ExecutionException.exportFailed("/my/uri", new InterruptedException("interrupted"));
        assertThat(ex.getMessage(), is("Export for report '/my/uri' failed. Reason: interrupted"));
    }

    @Test
    public void testExportCancelled() throws Exception {
        ExecutionException ex = ExecutionException.exportCancelled("/my/uri");
        assertThat(ex.getMessage(), is("Export for report '/my/uri' was cancelled"));

    }

    @Test
    public void testIsCancelled() throws Exception {
        ExecutionException exportEx = ExecutionException.exportCancelled("/my/uri");
        assertThat(exportEx.isCancelled(), is(true));

        ExecutionException reportEx = ExecutionException.reportCancelled("/my/uri");
        assertThat(reportEx.isCancelled(), is(true));
    }

    @Test
    public void testReportCancelledAdaptsToClientEx() throws Exception {
        ExecutionException ex = ExecutionException.reportCancelled("/");
        RuntimeException runtimeException = ex.adaptToClientException();
        assertThat(runtimeException.getMessage(), is(notNullValue()));
        assertThat(runtimeException, is(instanceOf(ReportRunException.class)));
    }

    @Test
    public void testReportFailedAdaptsToClientEx() throws Exception {
        ExecutionException ex = ExecutionException.reportFailed("/");
        RuntimeException runtimeException = ex.adaptToClientException();
        assertThat(runtimeException.getMessage(), is(notNullValue()));
        assertThat(runtimeException, is(instanceOf(ReportRunException.class)));
    }

    @Test
    public void testExportCancelledAdaptsToClientEx() throws Exception {
        ExecutionException ex = ExecutionException.exportCancelled("/");
        RuntimeException runtimeException = ex.adaptToClientException();
        assertThat(runtimeException.getMessage(), is(notNullValue()));
        assertThat(runtimeException, is(instanceOf(ReportExportException.class)));
    }

    @Test
    public void testExportFailedAdaptsToClientEx() throws Exception {
        ExecutionException ex = ExecutionException.exportFailed("/");
        RuntimeException runtimeException = ex.adaptToClientException();
        assertThat(runtimeException.getMessage(), is(notNullValue()));
        assertThat(runtimeException, is(instanceOf(ReportExportException.class)));
    }
}