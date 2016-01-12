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
import static org.mockito.Mockito.*;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SearchUseCase.class)
public class SearchTaskV5_6PlusTest {
    private static final InternalCriteria NO_CRITERIA = InternalCriteria.from(SearchCriteria.none());

    @Mock
    SearchUseCase mSearchUseCase;
    @Mock
    SearchResult mResponse;

    @Before
    public void setupMocks() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(mSearchUseCase.performSearch(any(InternalCriteria.class))).thenReturn(mResponse);

        List<Resource> stubLookup = Collections.singletonList(null);
        when(mResponse.getResources()).thenReturn(stubLookup);
    }

    @Test
    public void shouldMakeImmediateCallOnApiForUserOffsetZero() throws Exception {
        InternalCriteria searchCriteria = new InternalCriteria.Builder().offset(0).create();
        SearchTask strategy = new SearchTaskV5_6Plus(searchCriteria, mSearchUseCase);

        strategy.nextLookup();

        InternalCriteria internalCriteria = new InternalCriteria.Builder()
                .offset(0)
                .forceFullPage(true)
                .create();
        verify(mSearchUseCase).performSearch(internalCriteria);
    }

    @Test
    public void makesAdditionalCallOnApiIfUserOffsetNotZero() throws Exception {
        InternalCriteria searchCriteria = new InternalCriteria.Builder().offset(5).create();
        SearchTask strategy = new SearchTaskV5_6Plus(searchCriteria, mSearchUseCase);

        strategy.nextLookup();

        InternalCriteria internalCriteria = new InternalCriteria.Builder()
                .limit(5)
                .forceFullPage(true)
                .create();
        verify(mSearchUseCase).performSearch(internalCriteria);
    }

    @Test
    public void secondSearchLookupShouldUseNextOffset() throws Exception {
        InternalCriteria searchCriteria = new InternalCriteria.Builder().offset(0).create();
        SearchTask strategy = new SearchTaskV5_6Plus(searchCriteria, mSearchUseCase);

        when(mResponse.getNextOffset()).thenReturn(133);
        strategy.nextLookup();

        when(mResponse.getNextOffset()).thenReturn(233);
        strategy.nextLookup();


        InternalCriteria internalCriteria = new InternalCriteria.Builder()
                .offset(133)
                .limit(100)
                .forceFullPage(true)
                .create();

        verify(mResponse, times(2)).getNextOffset();
        verify(mSearchUseCase).performSearch(internalCriteria);
    }

    @Test
    public void searchWillAlwaysReturnEmptyCollectionIfReachedEndOnApiSide() throws Exception {
        SearchTask strategy = new SearchTaskV5_6Plus(NO_CRITERIA, mSearchUseCase);

        when(mResponse.getNextOffset()).thenReturn(133);
        strategy.nextLookup();

        when(mResponse.getNextOffset()).thenReturn(0);
        strategy.nextLookup();

        Collection<Resource> response = strategy.nextLookup();
        assertThat(response, is(empty()));
        assertThat(strategy.hasNext(), is(false));

        verify(mResponse, times(2)).getNextOffset();
        verify(mSearchUseCase, times(2)).performSearch(any(InternalCriteria.class));
    }

    @Test
    public void shouldReturnEmptyCollectionForZeroLimit() throws Exception {
        InternalCriteria userCriteria = new InternalCriteria.Builder().limit(0).offset(5).create();
        SearchTask strategy = new SearchTaskV5_6Plus(userCriteria, mSearchUseCase);

        Collection<Resource> result = strategy.nextLookup();
        assertThat(result, Matchers.is(Matchers.empty()));

        verifyZeroInteractions(mSearchUseCase);
    }
}