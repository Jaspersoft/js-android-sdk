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

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.RepositoryRestApi;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.service.internal.InfoCacheManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SearchStrategy.Factory.class)
public class SearchTaskImplTest {
    private static final InternalCriteria CRITERIA = InternalCriteria.from(SearchCriteria.none());

    @Mock
    RepositoryRestApi mRepoApi;
    @Mock
    SearchStrategy mSearchStrategy;
    @Mock
    InfoCacheManager mInfoCacheManager;
    @Mock
    ServerInfo mServerInfo;

    @Mock
    SearchUseCase mSearchUseCase;

    private SearchTaskImpl objectUnderTest;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        objectUnderTest = new SearchTaskImpl(CRITERIA, mSearchUseCase, mInfoCacheManager);

        when(mInfoCacheManager.getInfo()).thenReturn(mServerInfo);
        when(mSearchStrategy.searchNext()).thenReturn(Collections.<Resource>emptyList());

        PowerMockito.mockStatic(SearchStrategy.Factory.class);


        PowerMockito.when(
                SearchStrategy.Factory.get(
                        any(SearchUseCase.class),
                        any(InternalCriteria.class),
                        any(ServerVersion.class)
                )
        ).thenReturn(mSearchStrategy);
    }

    @Test
    public void nextLookupShouldDefineSearchStrategy() throws Exception {
        objectUnderTest.nextLookup();

        verify(mSearchStrategy).searchNext();
        verify(mInfoCacheManager).getInfo();
    }

    @Test
    public void secondLookupShouldUseCachedStrategy() throws Exception {
        objectUnderTest.nextLookup();
        objectUnderTest.nextLookup();

        verify(mInfoCacheManager).getInfo();
        verify(mSearchStrategy, times(2)).searchNext();
    }
}