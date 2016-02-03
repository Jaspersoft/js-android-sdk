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

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.FileLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ReportLookup;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.text.SimpleDateFormat;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RepositoryUseCaseTest {
    private static final String REPORT_URI = "/my/uri";

    @Mock
    ServiceExceptionMapper mServiceExceptionMapper;
    @Mock
    RepositoryRestApi mRepositoryRestApi;
    @Mock
    ReportResourceMapper reportResourceMapper;
    @Mock
    InfoCacheManager infoCacheManager;
    @Mock
    ServerInfo mServerInfo;

    private RepositoryUseCase useCase;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        useCase = new RepositoryUseCase(
                mServiceExceptionMapper,
                mRepositoryRestApi,
                reportResourceMapper,
                infoCacheManager);
    }

    @Test
    public void testGetReportDetails() throws Exception {
        when(infoCacheManager.getInfo()).thenReturn(mServerInfo);

        useCase.getReportDetails(REPORT_URI);

        verify(infoCacheManager).getInfo();
        verify(reportResourceMapper).toReportResource(any(ReportLookup.class), any(SimpleDateFormat.class));
        verify(mRepositoryRestApi).requestReportResource(REPORT_URI);
    }

    @Test
    public void testGetFileDetails() throws Exception {
        when(infoCacheManager.getInfo()).thenReturn(mServerInfo);

        useCase.getFileDetails(REPORT_URI);

        verify(infoCacheManager).getInfo();
        verify(reportResourceMapper).toFileResource(any(FileLookup.class), any(SimpleDateFormat.class));
        verify(mRepositoryRestApi).requestFileResource(REPORT_URI);
    }
}