package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;
import com.jaspersoft.android.sdk.service.auth.TokenProvider;

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
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
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
@PrepareForTest({ResourceSearchResult.class})
public class EmeraldMR2SearchStrategyTest {

    @Mock
    RepositoryRestApi mApi;
    @Mock
    ResourceSearchResult mResponse;
    @Mock
    TokenProvider mTokenProvider;
    /**
     * Objects under test
     */
    private EmeraldMR2SearchStrategy search10itemsStrategy;
    private EmeraldMR2SearchStrategy search10itemsStrategyWithUserOffset5;
    public static final List<ResourceLookup> FIVE_ITEMS = Arrays.asList(null, null, null, null, null);

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);

        when(mTokenProvider.provideToken()).thenReturn("cookie");

        when(mApi.searchResources(anyString(), anyMap())).thenReturn(mResponse);

        List<ResourceLookup> stubLookup = Collections.singletonList(new ResourceLookup());
        when(mResponse.getResources()).thenReturn(stubLookup);

        InternalCriteria criteria = InternalCriteria.builder().limit(10).create();
        search10itemsStrategy = new EmeraldMR2SearchStrategy(criteria, mApi, mTokenProvider);

        InternalCriteria userCriteria = criteria.newBuilder().offset(5).create();
        search10itemsStrategyWithUserOffset5 = new EmeraldMR2SearchStrategy(userCriteria, mApi, mTokenProvider);
    }

    @Test
    public void willAlignResponseToLimitIfAPIRespondsWithPartialNumber() throws Exception {
        when(mResponse.getResources()).thenReturn(FIVE_ITEMS);

        Collection<ResourceLookup> result = search10itemsStrategy.searchNext();
        assertThat(result.size(), is(10));

        Map<String, Object> params = new HashMap<>();
        params.put("limit", "10");
        verify(mApi).searchResources("cookie", params);

        params = new HashMap<>();
        params.put("limit", "10");
        params.put("offset", "10");
        verify(mApi).searchResources("cookie", params);
    }

    @Test
    public void willRetry5timesIfApiReturnsNoElements() throws Exception {
        when(mResponse.getResources()).thenReturn(Collections.<ResourceLookup>emptyList());

        Collection<ResourceLookup> result = search10itemsStrategy.searchNext();
        assertThat(search10itemsStrategy.hasNext(), is(false));

        assertThat(result, is(empty()));
        verify(mApi, times(6)).searchResources( eq("cookie"), anyMap());
    }

    @Test
    public void willReturnAsMuchElementsAsLeftIfEndReached() throws Exception {
        when(mResponse.getResources()).then(OnlyTwoItems.INSTANCE);

        Collection<ResourceLookup> result = search10itemsStrategy.searchNext();
        assertThat(result.size(), is(2));

        verify(mApi, times(6)).searchResources( eq("cookie"), anyMap());
    }

    @Test
    public void forCustomOffsetShouldCalculateServerDisposition() throws Exception {
        when(mResponse.getResources()).thenReturn(FIVE_ITEMS);

        search10itemsStrategyWithUserOffset5.searchNext();

        Map<String, Object> params1 = new HashMap<>();
        params1.put("limit", "5");
        verify(mApi).searchResources("cookie", params1);

        Map<String, Object> params2 = new HashMap<>();
        params2.put("limit", "10");
        params2.put("offset", "5");
        verify(mApi).searchResources("cookie", params2);

        Map<String, Object> params3 = new HashMap<>();
        params3.put("limit", "10");
        params3.put("offset", "15");
        verify(mApi).searchResources("cookie", params3);

        verify(mApi, times(3)).searchResources(eq("cookie"), anyMap());
        verifyNoMoreInteractions(mApi);
    }

    @Test
    public void shouldReturnEmptyCollectionForZeroLimit() {
        InternalCriteria userCriteria = InternalCriteria.builder().limit(0).offset(5).create();
        EmeraldMR2SearchStrategy strategy = new EmeraldMR2SearchStrategy(userCriteria, mApi, mTokenProvider);

        Collection<ResourceLookup> result = strategy.searchNext();
        assertThat(result, is(empty()));

        verifyZeroInteractions(mApi);
    }

    private static class OnlyTwoItems implements Answer<Collection<ResourceLookup>> {
        public static final OnlyTwoItems INSTANCE = new OnlyTwoItems();

        private final List<ResourceLookup> twoItems = Arrays.asList(null, null);
        private final List<ResourceLookup> zeroItems = Collections.emptyList();

        private int count = 0;

        @Override
        public Collection<ResourceLookup> answer(InvocationOnMock invocationOnMock) throws Throwable {
            if (count == 0) {
                count++;
                return twoItems;
            }
            return zeroItems;
        }
    }
}