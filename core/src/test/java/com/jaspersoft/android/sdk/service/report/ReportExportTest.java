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

import com.jaspersoft.android.sdk.network.ReportExportRestApi;
import com.jaspersoft.android.sdk.service.auth.TokenProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportExportTest {
    @Mock
    ReportExportRestApi mExportRestApi;
    @Mock
    TokenProvider mTokenProvider;

    private ReportExport objectUnderTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ExecutionOptionsDataMapper executionOptionsDataMapper = new ExecutionOptionsDataMapper("/my/uri");
        ReportExportUseCase exportUseCase = new ReportExportUseCase(mExportRestApi, mTokenProvider, executionOptionsDataMapper);
        objectUnderTest = new ReportExport("report_execution_id", "export_id", Collections.<ReportAttachment>emptyList(), exportUseCase);
    }

    @Test
    public void testDownload() throws Exception {
        objectUnderTest.download();
        verify(mExportRestApi).requestExportOutput(anyString(), eq("report_execution_id"), eq("export_id"));
        verifyNoMoreInteractions(mExportRestApi);
    }
}