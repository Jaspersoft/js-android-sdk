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

package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.InputControlRestApi;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReportControlsUseCaseTest {
    private static final String REPORT_URI = "my/uri";
    private static final List<ReportParameter> PARAMS = Collections.emptyList();

    @Mock
    ServiceExceptionMapper mExceptionMapper;
    @Mock
    InputControlRestApi mRestApi;

    @Mock
    IOException mIOException;
    @Mock
    HttpException mHttpException;
    @Mock
    ServiceException mServiceException;

    private ReportControlsUseCase mReportControlsUseCase;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mReportControlsUseCase = new ReportControlsUseCase(mExceptionMapper, mRestApi);

        when(mExceptionMapper.transform(any(HttpException.class))).thenReturn(mServiceException);
        when(mExceptionMapper.transform(any(IOException.class))).thenReturn(mServiceException);
    }

    @Test
    public void should_request_controls() throws Exception {
        mReportControlsUseCase.requestControls(REPORT_URI, null, false);
        verify(mRestApi).requestInputControls(REPORT_URI, null, false);
    }

     @Test
    public void should_request_resource_values() throws Exception {
        mReportControlsUseCase.requestResourceValues(REPORT_URI, false);
        verify(mRestApi).requestInputControlsInitialStates(REPORT_URI, false);
    }

    @Test
    public void request_controls_adapt_io_exception() throws Exception {
        when(mRestApi.requestInputControls(anyString(), anySetOf(String.class), anyBoolean())).thenThrow(mIOException);
        try {
            mReportControlsUseCase.requestControls(REPORT_URI, null, false);
            fail("Should adapt IO exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mIOException);
        }
    }

    @Test
    public void request_controls_adapt_http_exception() throws Exception {
        when(mRestApi.requestInputControls(anyString(), anySetOf(String.class), anyBoolean())).thenThrow(mHttpException);
        try {
            mReportControlsUseCase.requestControls(REPORT_URI, null, false);
            fail("Should adapt HTTP exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mHttpException);
        }
    }


    @Test
    public void request_controls_values_adapt_io_exception() throws Exception {
        when(mRestApi.requestInputControlsStates(anyString(), anyListOf(ReportParameter.class), anyBoolean())).thenThrow(mIOException);
        try {
            mReportControlsUseCase.requestControlsValues(REPORT_URI, PARAMS, false);
            fail("Should adapt IO exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mIOException);
        }
    }

    @Test
    public void request_controls_values_adapt_http_exception() throws Exception {
        when(mRestApi.requestInputControlsStates(anyString(), anyListOf(ReportParameter.class), anyBoolean())).thenThrow(mHttpException);
        try {
            mReportControlsUseCase.requestControlsValues(REPORT_URI, PARAMS, false);
            fail("Should adapt HTTP exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mHttpException);
        }
    }

    @Test
    public void should_request_controls_values() throws Exception {
        mReportControlsUseCase.requestControlsValues(REPORT_URI, PARAMS, false);
        verify(mRestApi).requestInputControlsStates(REPORT_URI, PARAMS, false);
    }
}