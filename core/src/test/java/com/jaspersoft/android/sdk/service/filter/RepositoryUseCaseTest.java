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
import com.jaspersoft.android.sdk.network.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardComponentCollection;
import com.jaspersoft.android.sdk.network.entity.dashboard.InputControlDashboardComponent;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Collections;

import static junit.framework.TestCase.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RepositoryUseCaseTest {
    private static final String DASHBOARD_URI = "/my/uri";

    @Mock
    ServiceExceptionMapper mExceptionMapper;
    @Mock
    RepositoryRestApi mRepositoryRestApi;

    @Mock
    IOException mIOException;
    @Mock
    HttpException mHttpException;
    @Mock
    ServiceException mServiceException;

    private RepositoryUseCase useCase;

    DashboardComponentCollection mDashboardComponentCollection =
            new DashboardComponentCollection(Collections.<InputControlDashboardComponent>emptyList());


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        setupMocks();
        useCase = new RepositoryUseCase(mExceptionMapper, mRepositoryRestApi);
    }

    @Test
    public void  list_dashboard_components() throws Exception {
        useCase.requestDashboardComponents(DASHBOARD_URI);
        verify(mRepositoryRestApi).requestDashboardComponents(DASHBOARD_URI);
    }

    @Test
    public void list_dashboard_components_adapts_io_exception() throws Exception {
        when(mRepositoryRestApi.requestDashboardComponents(anyString())).thenThrow(mIOException);
        try {
            useCase.requestDashboardComponents(DASHBOARD_URI);
            fail("Should adapt IO exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mIOException);
        }
    }

    @Test
    public void list_dashboard_components_adapts_http_exception() throws Exception {
        when(mRepositoryRestApi.requestDashboardComponents(anyString())).thenThrow(mHttpException);

        try {
            useCase.requestDashboardComponents(DASHBOARD_URI);
            fail("Should adapt HTTP exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mHttpException);
        }
    }

    private void setupMocks() throws Exception {
        when(mRepositoryRestApi.requestDashboardComponents(anyString())).thenReturn(mDashboardComponentCollection);
        when(mExceptionMapper.transform(any(HttpException.class))).thenReturn(mServiceException);
        when(mExceptionMapper.transform(any(IOException.class))).thenReturn(mServiceException);
    }
}