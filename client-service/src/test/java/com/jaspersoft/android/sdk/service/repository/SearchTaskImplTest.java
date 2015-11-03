/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.RepositoryRestApi;
import com.jaspersoft.android.sdk.service.InfoProvider;
import com.jaspersoft.android.sdk.service.auth.TokenProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    TokenProvider mTokenProvider;
    @Mock
    InfoProvider mInfoProvider;

    private SearchTaskImpl objectUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectUnderTest = new SearchTaskImpl(CRITERIA, mRepoApi, mTokenProvider, mInfoProvider);

        when(mSearchStrategy.searchNext()).thenReturn(null);

        PowerMockito.mockStatic(SearchStrategy.Factory.class);


        PowerMockito.when(
                SearchStrategy.Factory.get(
                        any(InternalCriteria.class),
                        any(RepositoryRestApi.class),
                        any(InfoProvider.class),
                        any(TokenProvider.class)
                )
        ).thenReturn(mSearchStrategy);
    }

    @Test
    public void nextLookupShouldDefineSearchStrategy() {
        objectUnderTest.nextLookup();

        PowerMockito.verifyStatic(times(1));
        SearchStrategy.Factory.get(eq(CRITERIA), eq(mRepoApi), eq(mInfoProvider), eq(mTokenProvider));

        verify(mSearchStrategy).searchNext();
    }

    @Test
    public void secondLookupShouldUseCachedStrategy() {
        objectUnderTest.nextLookup();
        objectUnderTest.nextLookup();

        PowerMockito.verifyStatic(times(1));
        SearchStrategy.Factory.get(eq(CRITERIA), eq(mRepoApi), eq(mInfoProvider), eq(mTokenProvider));

        verify(mSearchStrategy, times(2)).searchNext();
    }
}