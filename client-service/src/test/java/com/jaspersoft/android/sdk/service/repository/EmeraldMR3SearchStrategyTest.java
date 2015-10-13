package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ResourceSearchResult.class)
public class EmeraldMR3SearchStrategyTest {
    private static final InternalCriteria NO_CRITERIA = InternalCriteria.from(SearchCriteria.none());

    @Mock
    RepositoryRestApi.Factory mApiFactory;
    @Mock
    RepositoryRestApi mApi;
    @Mock
    ResourceSearchResult mResponse;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);

        when(mApi.searchResources(anyMap())).thenReturn(mResponse);
        when(mApiFactory.get()).thenReturn(mApi);

        List<ResourceLookup> stubLookup = Collections.singletonList(new ResourceLookup());
        when(mResponse.getResources()).thenReturn(stubLookup);
    }

    @Test
    public void shouldMakeImmediateCallOnApiForUserOffsetZero() {
        InternalCriteria searchCriteria = InternalCriteria.builder().offset(0).create();
        EmeraldMR3SearchStrategy strategy = new EmeraldMR3SearchStrategy(mApiFactory, searchCriteria);

        when(mApi.searchResources(anyMap())).thenReturn(mResponse);

        strategy.searchNext();

        Map<String, Object> params = new HashMap<>();
        params.put("forceFullPage", "true");

        verify(mApi, times(1)).searchResources(params);
    }

    @Test
    public void makesAdditionalCallOnApiIfUserOffsetNotZero() {
        InternalCriteria searchCriteria = InternalCriteria.builder().offset(5).create();
        EmeraldMR3SearchStrategy strategy = new EmeraldMR3SearchStrategy(mApiFactory, searchCriteria);

        strategy.searchNext();

        Map<String, Object> params = new HashMap<>();
        params.put("forceFullPage", "true");
        params.put("limit", "5");

        verify(mApi, times(1)).searchResources(params);
    }

    @Test
    public void secondSearchLookupShouldUseNextOffset() {
        InternalCriteria searchCriteria = InternalCriteria.builder().offset(0).create();
        EmeraldMR3SearchStrategy strategy = new EmeraldMR3SearchStrategy(mApiFactory, searchCriteria);

        when(mResponse.getNextOffset()).thenReturn(133);
        strategy.searchNext();

        when(mResponse.getNextOffset()).thenReturn(233);
        strategy.searchNext();


        Map<String, Object> params = new HashMap<>();
        params.put("forceFullPage", "true");
        params.put("offset", "133");

        verify(mApiFactory, times(2)).get();
        verify(mResponse, times(2)).getNextOffset();
        verify(mApi, times(1)).searchResources(params);
    }

    @Test
    public void searchWillAlwaysReturnEmptyCollectionIfReachedEndOnApiSide() {
        EmeraldMR3SearchStrategy strategy = new EmeraldMR3SearchStrategy(mApiFactory, NO_CRITERIA);

        when(mResponse.getNextOffset()).thenReturn(133);
        strategy.searchNext();

        when(mResponse.getNextOffset()).thenReturn(0);
        strategy.searchNext();

        Collection<ResourceLookup> response = strategy.searchNext();
        assertThat(response, is(empty()));
        assertThat(strategy.hasNext(), is(false));

        verify(mApiFactory, times(2)).get();
        verify(mResponse, times(2)).getNextOffset();
        verify(mApi, times(2)).searchResources(anyMap());
    }

    @Test
    public void shouldReturnEmptyCollectionForZeroLimit() {
        InternalCriteria userCriteria = InternalCriteria.builder().limit(0).offset(5).create();
        SearchStrategy strategy = new EmeraldMR3SearchStrategy(mApiFactory, userCriteria);

        Collection<ResourceLookup> result = strategy.searchNext();
        assertThat(result, Matchers.is(Matchers.empty()));

        verifyZeroInteractions(mApi);
    }
}