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

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class RepositorySearchTaskFactoryTest {

    @Mock
    InfoCacheManager mInfoCacheManager;
    @Mock
    SearchUseCase mSearchUseCase;
    @Mock
    ServerInfo mServerInfo;

    private static final InternalCriteria CRITERIA =
            new InternalCriteria.Builder().create();
    private SearchTaskFactory searchTaskFactory;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mInfoCacheManager.getInfo()).thenReturn(mServerInfo);

        searchTaskFactory = new SearchTaskFactory(CRITERIA, mSearchUseCase, mInfoCacheManager);
    }

    @Test
    public void testShouldReturnLegacySearchTask() throws Exception {
        when(mServerInfo.getVersion()).thenReturn(ServerVersion.v5_5);
        RepositorySearchTask task = searchTaskFactory.create();
        assertThat(task, is(instanceOf(RepositorySearchTaskV5_5.class)));
    }

    @Test
    public void testShouldReturnNewSearchTask() throws Exception {
        when(mServerInfo.getVersion()).thenReturn(ServerVersion.v5_6);
        RepositorySearchTask task = searchTaskFactory.create();
        assertThat(task, is(instanceOf(RepositorySearchTaskV5_6Plus.class)));
    }
}