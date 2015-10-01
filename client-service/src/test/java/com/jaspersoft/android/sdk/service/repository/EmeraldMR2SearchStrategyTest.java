package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ResourceSearchResponse.class})
public class EmeraldMR2SearchStrategyTest {

    @Mock
    RepositoryRestApi.Factory mApiFactory;
    @Mock
    RepositoryRestApi mApi;
    @Mock
    ResourceSearchResponse mResponse;

    /**
     * Objects under test
     */
    private EmeraldMR2SearchStrategy search10itemsStrategy;
    private EmeraldMR2SearchStrategy search10itemsStrategyWithUserOffset5;
    public static final List<ResourceLookupResponse> FIVE_ITEMS = Arrays.asList(null, null, null, null, null);

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);

        when(mApi.searchResources(anyMap())).thenReturn(mResponse);
        when(mApiFactory.get()).thenReturn(mApi);

        List<ResourceLookupResponse> stubLookup = Collections.singletonList(new ResourceLookupResponse());
        when(mResponse.getResources()).thenReturn(stubLookup);

        InternalCriteria criteria = InternalCriteria.builder().limit(10).create();
        search10itemsStrategy = new EmeraldMR2SearchStrategy(mApiFactory, criteria);

        InternalCriteria userCriteria = criteria.newBuilder().offset(5).create();
        search10itemsStrategyWithUserOffset5 = new EmeraldMR2SearchStrategy(mApiFactory, userCriteria);
    }

    @Test
    public void willAlignResponseToLimitIfAPIRespondsWithPartialNumber() throws Exception {
        when(mResponse.getResources()).thenReturn(FIVE_ITEMS);

        Collection<ResourceLookupResponse> result = search10itemsStrategy.searchNext().toBlocking().first();
        assertThat(result.size(), is(10));

        Map<String, Object> params = new HashMap<>();
        params.put("limit", "10");
        verify(mApi).searchResources(params);

        params = new HashMap<>();
        params.put("limit", "10");
        params.put("offset", "10");
        verify(mApi).searchResources(params);

        verify(mApiFactory, times(2)).get();
    }

    @Test
    public void willRetry5timesIfApiReturnsNoElements()throws Exception {
        when(mResponse.getResources()).thenReturn(Collections.<ResourceLookupResponse>emptyList());

        Collection<ResourceLookupResponse> result = search10itemsStrategy.searchNext().toBlocking().first();
        assertThat(search10itemsStrategy.hasNext(), is(false));

        assertThat(result, is(empty()));
        verify(mApiFactory, times(6)).get();
        verify(mApi, times(6)).searchResources(anyMap());
    }

    @Test
    public void willReturnAsMuchElementsAsLeftIfEndReached()throws Exception {
        when(mResponse.getResources()).then(OnlyTwoItems.INSTANCE);

        Collection<ResourceLookupResponse> result = search10itemsStrategy.searchNext().toBlocking().first();
        assertThat(result.size(), is(2));

        verify(mApiFactory, times(6)).get();
        verify(mApi, times(6)).searchResources(anyMap());
    }

    @Test
    public void forCustomOffsetShouldCalculateServerDisposition()throws Exception {
        when(mResponse.getResources()).thenReturn(FIVE_ITEMS);

        search10itemsStrategyWithUserOffset5.searchNext().toBlocking().first();

        Map<String, Object> params1 = new HashMap<>();
        params1.put("limit", "5");
        verify(mApi).searchResources(params1);

        Map<String, Object> params2 = new HashMap<>();
        params2.put("limit", "10");
        params2.put("offset", "5");
        verify(mApi).searchResources(params2);

        Map<String, Object> params3 = new HashMap<>();
        params3.put("limit", "10");
        params3.put("offset", "15");
        verify(mApi).searchResources(params3);

        verify(mApi, times(3)).searchResources(anyMap());
        verifyNoMoreInteractions(mApi);
    }

    @Test
    public void shouldReturnEmptyCollectionForZeroLimit() {
        InternalCriteria userCriteria = InternalCriteria.builder().limit(0).offset(5).create();
        EmeraldMR2SearchStrategy strategy = new EmeraldMR2SearchStrategy(mApiFactory, userCriteria);

        Collection<ResourceLookupResponse> result = strategy.searchNext().toBlocking().first();
        assertThat(result, is(empty()));

        verifyZeroInteractions(mApi);
    }

    private static class OnlyTwoItems implements Answer<Collection<ResourceLookupResponse>> {
        public static final OnlyTwoItems INSTANCE = new OnlyTwoItems();

        private final List<ResourceLookupResponse> twoItems = Arrays.asList(null, null);
        private final List<ResourceLookupResponse> zeroItems = Collections.emptyList();

        private int count = 0;
        @Override
        public Collection<ResourceLookupResponse> answer(InvocationOnMock invocationOnMock) throws Throwable {
            if (count == 0) {
                count++;
                return twoItems;
            }
            return zeroItems;
        }
    }
}