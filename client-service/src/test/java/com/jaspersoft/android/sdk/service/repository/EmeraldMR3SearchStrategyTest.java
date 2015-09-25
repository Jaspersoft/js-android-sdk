package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResponse;

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

import rx.observers.TestSubscriber;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ResourceSearchResponse.class)
public class EmeraldMR3SearchStrategyTest {

    @Mock
    RepositoryRestApi.Factory mApiFactory;
    @Mock
    RepositoryRestApi mApi;
    @Mock
    ResourceSearchResponse mResponse;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);

        when(mApi.searchResources(anyMap())).thenReturn(mResponse);
        when(mApiFactory.get()).thenReturn(mApi);

        List<ResourceLookupResponse> stubLookup = Collections.singletonList(new ResourceLookupResponse());
        when(mResponse.getResources()).thenReturn(stubLookup);
    }

    @Test
    public void shouldMakeImmediateCallOnApiForUserOffsetZero() {
        SearchCriteria searchCriteria = SearchCriteria.builder().offset(0).create();
        EmeraldMR3SearchStrategy strategy = new EmeraldMR3SearchStrategy(mApiFactory, searchCriteria);

        when(mApi.searchResources(anyMap())).thenReturn(mResponse);

        performSearch(strategy);

        Map<String, Object> params = new HashMap<>();
        params.put("forceFullPage", "true");

        verify(mApi, times(1)).searchResources(params);
    }

    @Test
    public void makesAdditionalCallOnApiIfUserOffsetNotZero() {
        SearchCriteria searchCriteria = SearchCriteria.builder().offset(5).create();
        EmeraldMR3SearchStrategy strategy = new EmeraldMR3SearchStrategy(mApiFactory, searchCriteria);

        performSearch(strategy);

        Map<String, Object> params = new HashMap<>();
        params.put("forceFullPage", "true");
        params.put("limit", "5");

        verify(mApi, times(1)).searchResources(params);
    }

    @Test
    public void secondSearchLookupShouldUseNextOffset() {
        SearchCriteria searchCriteria = SearchCriteria.builder().offset(0).create();
        EmeraldMR3SearchStrategy strategy = new EmeraldMR3SearchStrategy(mApiFactory, searchCriteria);

        when(mResponse.getNextOffset()).thenReturn(133);
        performSearch(strategy);

        when(mResponse.getNextOffset()).thenReturn(233);
        performSearch(strategy);


        Map<String, Object> params = new HashMap<>();
        params.put("forceFullPage", "true");
        params.put("offset", "133");

        verify(mApiFactory, times(2)).get();
        verify(mResponse, times(2)).getNextOffset();
        verify(mApi, times(1)).searchResources(params);
    }

    @Test
    public void searchWillAlwaysReturnEmptyCollectionIfReachedEndOnApiSide() {
        EmeraldMR3SearchStrategy strategy = new EmeraldMR3SearchStrategy(mApiFactory, SearchCriteria.none());

        when(mResponse.getNextOffset()).thenReturn(133);
        performSearch(strategy);

        when(mResponse.getNextOffset()).thenReturn(0);
        performSearch(strategy);

        List<Collection<ResourceLookupResponse>> events = performSearch(strategy).getOnNextEvents();
        Collection<ResourceLookupResponse> response = events.get(0);
        assertThat(response, is(empty()));

        verify(mApiFactory, times(2)).get();
        verify(mResponse, times(2)).getNextOffset();
        verify(mApi, times(2)).searchResources(anyMap());
    }

    private TestSubscriber performSearch(EmeraldMR3SearchStrategy strategy) {
        TestSubscriber<Collection<ResourceLookupResponse>> testSubscriber = new TestSubscriber<>();
        strategy.search().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();

        return testSubscriber;
    }
}