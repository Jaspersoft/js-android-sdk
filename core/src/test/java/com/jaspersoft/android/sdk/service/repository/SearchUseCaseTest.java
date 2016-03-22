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
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;
import com.jaspersoft.android.sdk.service.internal.FakeCallExecutor;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.SearchResult;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class SearchUseCaseTest {

    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat();

    @Mock
    ResourcesMapper mDataMapper;
    @Mock
    RepositoryRestApi mRepositoryRestApi;
    @Mock
    InfoCacheManager mInfoCacheManager;

    @Mock
    ResourceLookup mResourceLookup;
    @Mock
    InternalCriteria mCriteria;
    @Mock
    ServerInfo mServerInfo;

    @Mock
    ResourceSearchResult mResult;

    private SearchUseCase objectUnderTest;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(mServerInfo.getDatetimeFormatPattern()).thenReturn(DATE_TIME_FORMAT);
        when(mInfoCacheManager.getInfo()).thenReturn(mServerInfo);

        objectUnderTest = new SearchUseCase(
                mDataMapper,
                mRepositoryRestApi,
                mInfoCacheManager,
                new FakeCallExecutor()
        );
    }

    @Test
    public void shouldProvideAndAdaptSearchResult() throws Exception {
        when(mResult.getNextOffset()).thenReturn(100);
        when(mRepositoryRestApi.searchResources(anyMapOf(String.class, Object.class))).thenReturn(mResult);

        List<Resource> resources = new ArrayList<Resource>();
        when(mDataMapper.toResources(anyCollectionOf(ResourceLookup.class), Matchers.any(SimpleDateFormat.class))).thenReturn(resources);

        SearchResult result = objectUnderTest.performSearch(mCriteria);
        assertThat(result, is(not(nullValue())));
        assertThat(result.getNextOffset(), is(100));
        assertThat(result.getResources(), is(resources));

        verify(mRepositoryRestApi).searchResources(anyMapOf(String.class, Object.class));
    }
}