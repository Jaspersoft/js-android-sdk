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

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SearchUseCase.class)
public class EmeraldMR3SearchStrategyTest {
    private static final InternalCriteria NO_CRITERIA = InternalCriteria.from(SearchCriteria.none());

    @Mock
    SearchUseCase mSearchUseCase;
    @Mock
    SearchResult mResponse;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);

        when(mSearchUseCase.performSearch(any(InternalCriteria.class))).thenReturn(mResponse);

        List<Resource> stubLookup = Collections.singletonList(null);
        when(mResponse.getResources()).thenReturn(stubLookup);
    }

    @Test
    public void shouldMakeImmediateCallOnApiForUserOffsetZero() {
        InternalCriteria searchCriteria = InternalCriteria.builder().offset(0).create();
        EmeraldMR3SearchStrategy strategy = new EmeraldMR3SearchStrategy(searchCriteria, mSearchUseCase);

        strategy.searchNext();

        InternalCriteria internalCriteria = InternalCriteria.builder()
                .offset(0)
                .forceFullPage(true)
                .create();
        verify(mSearchUseCase).performSearch(internalCriteria);
    }

    @Test
    public void makesAdditionalCallOnApiIfUserOffsetNotZero() {
        InternalCriteria searchCriteria = InternalCriteria.builder().offset(5).create();
        EmeraldMR3SearchStrategy strategy = new EmeraldMR3SearchStrategy(searchCriteria, mSearchUseCase);

        strategy.searchNext();

        InternalCriteria internalCriteria = InternalCriteria.builder()
                .limit(5)
                .forceFullPage(true)
                .create();
        verify(mSearchUseCase).performSearch(internalCriteria);
    }

    @Test
    public void secondSearchLookupShouldUseNextOffset() {
        InternalCriteria searchCriteria = InternalCriteria.builder().offset(0).create();
        EmeraldMR3SearchStrategy strategy = new EmeraldMR3SearchStrategy(searchCriteria, mSearchUseCase);

        when(mResponse.getNextOffset()).thenReturn(133);
        strategy.searchNext();

        when(mResponse.getNextOffset()).thenReturn(233);
        strategy.searchNext();


        InternalCriteria internalCriteria = InternalCriteria.builder()
                .offset(133)
                .limit(100)
                .forceFullPage(true)
                .create();

        verify(mResponse, times(2)).getNextOffset();
        verify(mSearchUseCase).performSearch(internalCriteria);
    }

    @Test
    public void searchWillAlwaysReturnEmptyCollectionIfReachedEndOnApiSide() {
        EmeraldMR3SearchStrategy strategy = new EmeraldMR3SearchStrategy(NO_CRITERIA, mSearchUseCase);

        when(mResponse.getNextOffset()).thenReturn(133);
        strategy.searchNext();

        when(mResponse.getNextOffset()).thenReturn(0);
        strategy.searchNext();

        Collection<Resource> response = strategy.searchNext();
        assertThat(response, is(empty()));
        assertThat(strategy.hasNext(), is(false));

        verify(mResponse, times(2)).getNextOffset();
        verify(mSearchUseCase, times(2)).performSearch(any(InternalCriteria.class));
    }

    @Test
    public void shouldReturnEmptyCollectionForZeroLimit() {
        InternalCriteria userCriteria = InternalCriteria.builder().limit(0).offset(5).create();
        SearchStrategy strategy = new EmeraldMR3SearchStrategy(userCriteria, mSearchUseCase);

        Collection<Resource> result = strategy.searchNext();
        assertThat(result, Matchers.is(Matchers.empty()));

        verifyZeroInteractions(mSearchUseCase);
    }
}