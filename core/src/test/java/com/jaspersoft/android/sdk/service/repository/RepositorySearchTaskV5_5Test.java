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

import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.SearchResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.3
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SearchUseCase.class})
public class RepositorySearchTaskV5_5Test {

    @Mock
    SearchUseCase mSearchUseCase;
    @Mock
    SearchResult mResponse;

    /**
     * Objects under test
     */
    private RepositorySearchTask search10itemsStrategy;
    private RepositorySearchTask search10itemsStrategyWithUserOffset5;
    public static final List<Resource> FIVE_ITEMS = Arrays.asList(null, null, null, null, null);

    @Before
    public void setupMocks() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(mSearchUseCase.performSearch(Matchers.any(InternalCriteria.class))).thenReturn(mResponse);

        InternalCriteria criteria = new InternalCriteria.Builder().limit(10).create();
        search10itemsStrategy = new RepositorySearchTaskV5_5(criteria, mSearchUseCase);

        InternalCriteria userCriteria = criteria.newBuilder().offset(5).create();
        search10itemsStrategyWithUserOffset5 = new RepositorySearchTaskV5_5(userCriteria, mSearchUseCase);
    }

    @Test
    public void willAlignResponseToLimitIfAPIRespondsWithPartialNumber() throws Exception {
        when(mResponse.getResources()).thenReturn(FIVE_ITEMS);

        Collection<Resource> result = search10itemsStrategy.nextLookup();
        assertThat(result.size(), is(10));

        InternalCriteria criteria1 = new InternalCriteria.Builder().limit(10).create();
        verify(mSearchUseCase).performSearch(criteria1);

        InternalCriteria criteria2 = new InternalCriteria.Builder()
                .offset(10).limit(10).create();
        verify(mSearchUseCase).performSearch(criteria2);
    }

    @Test
    public void willRetry5timesIfApiReturnsNoElements() throws Exception {
        when(mResponse.getResources()).thenReturn(Collections.<Resource>emptyList());

        Collection<Resource> result = search10itemsStrategy.nextLookup();
        assertThat(search10itemsStrategy.hasNext(), is(false));

        assertThat(result, is(empty()));
        verify(mSearchUseCase, times(6)).performSearch(Matchers.any(InternalCriteria.class));
    }

    @Test
    public void willReturnAsMuchElementsAsLeftIfEndReached() throws Exception {
        when(mResponse.getResources()).then(OnlyTwoItems.INSTANCE);

        Collection<Resource> result = search10itemsStrategy.nextLookup();
        assertThat(result.size(), is(2));

        verify(mSearchUseCase, times(6)).performSearch(Matchers.any(InternalCriteria.class));
    }

    @Test
    public void forCustomOffsetShouldCalculateServerDisposition() throws Exception {
        when(mResponse.getResources()).thenReturn(FIVE_ITEMS);

        search10itemsStrategyWithUserOffset5.nextLookup();


        InternalCriteria criteria1 = new InternalCriteria.Builder()
                .limit(5).create();
        verify(mSearchUseCase).performSearch(criteria1);


        InternalCriteria criteria2 = new InternalCriteria.Builder()
                .limit(10).offset(5).create();
        verify(mSearchUseCase).performSearch(criteria2);

        InternalCriteria criteria3 = new InternalCriteria.Builder()
                .limit(10).offset(15).create();
        verify(mSearchUseCase).performSearch(criteria3);

        verify(mSearchUseCase, times(3)).performSearch(Matchers.any(InternalCriteria.class));
        verifyNoMoreInteractions(mSearchUseCase);
    }

    @Test
    public void shouldReturnEmptyCollectionForZeroLimit() throws Exception {
        InternalCriteria userCriteria = new InternalCriteria.Builder().limit(0).offset(5).create();
        RepositorySearchTask strategy = new RepositorySearchTaskV5_5(userCriteria, mSearchUseCase);

        List<Resource> result = strategy.nextLookup();
        assertThat(result, is(empty()));

        verifyZeroInteractions(mSearchUseCase);
    }

    private static class OnlyTwoItems implements Answer<Collection<Resource>> {
        public static final OnlyTwoItems INSTANCE = new OnlyTwoItems();

        private final List<Resource> twoItems = Arrays.asList(null, null);
        private final List<Resource> zeroItems = Collections.emptyList();

        private int count = 0;

        @Override
        public Collection<Resource> answer(InvocationOnMock invocationOnMock) throws Throwable {
            if (count == 0) {
                count++;
                return twoItems;
            }
            return zeroItems;
        }
    }
}