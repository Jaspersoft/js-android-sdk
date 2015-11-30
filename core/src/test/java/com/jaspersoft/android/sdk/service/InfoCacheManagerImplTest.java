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

package com.jaspersoft.android.sdk.service;

import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.info.InfoCache;
import com.jaspersoft.android.sdk.service.info.InfoCacheManager;
import com.jaspersoft.android.sdk.service.server.ServerInfoService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class InfoCacheManagerImplTest {
    @Mock
    ServerInfoService mInfoService;
    @Mock
    InfoCache mInfoCache;
    @Mock
    ServerInfo info;

    private InfoCacheManager mManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mInfoService.requestServerInfo()).thenReturn(info);
        mManager = new InfoCacheManagerImpl(mInfoService, mInfoCache);
    }

    @Test
    public void testGetInfoWithCache() throws Exception {
        when(mInfoCache.get()).thenReturn(info);
        mManager.getInfo();
        verify(mInfoCache).get();
    }

    @Test
    public void testGetInfoWithoutCache() throws Exception {
        when(mInfoCache.get()).thenReturn(null);
        mManager.getInfo();
        verify(mInfoCache).get();
        verify(mInfoService).requestServerInfo();
        verify(mInfoCache).put(info);
    }

    @Test
    public void testInvalidateInfo() throws Exception {
        mManager.invalidateInfo();
        verify(mInfoCache).evict();
    }
}