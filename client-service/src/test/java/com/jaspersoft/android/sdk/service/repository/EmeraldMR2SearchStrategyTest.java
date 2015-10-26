package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.service.data.repository.GenericResource;
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
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SearchUseCase.class})
public class EmeraldMR2SearchStrategyTest {

    @Mock
    SearchUseCase mSearchUseCase;
    @Mock
    SearchResult mResponse;

    /**
     * Objects under test
     */
    private EmeraldMR2SearchStrategy search10itemsStrategy;
    private EmeraldMR2SearchStrategy search10itemsStrategyWithUserOffset5;
    public static final List<GenericResource> FIVE_ITEMS = Arrays.asList(null, null, null, null, null);

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);

        when(mSearchUseCase.performSearch(Matchers.any(InternalCriteria.class))).thenReturn(mResponse);

        InternalCriteria criteria = InternalCriteria.builder().limit(10).create();
        search10itemsStrategy = new EmeraldMR2SearchStrategy(criteria, mSearchUseCase);

        InternalCriteria userCriteria = criteria.newBuilder().offset(5).create();
        search10itemsStrategyWithUserOffset5 = new EmeraldMR2SearchStrategy(userCriteria, mSearchUseCase);
    }

    @Test
    public void willAlignResponseToLimitIfAPIRespondsWithPartialNumber() throws Exception {
        when(mResponse.getResources()).thenReturn(FIVE_ITEMS);

        Collection<GenericResource> result = search10itemsStrategy.searchNext();
        assertThat(result.size(), is(10));

        InternalCriteria criteria1 = InternalCriteria.builder().limit(10).create();
        verify(mSearchUseCase).performSearch(criteria1);

        InternalCriteria criteria2 = InternalCriteria.builder()
                .offset(10).limit(10).create();
        verify(mSearchUseCase).performSearch(criteria2);
    }

    @Test
    public void willRetry5timesIfApiReturnsNoElements() throws Exception {
        when(mResponse.getResources()).thenReturn(Collections.<GenericResource>emptyList());

        Collection<GenericResource> result = search10itemsStrategy.searchNext();
        assertThat(search10itemsStrategy.hasNext(), is(false));

        assertThat(result, is(empty()));
        verify(mSearchUseCase, times(6)).performSearch(Matchers.any(InternalCriteria.class));
    }

    @Test
    public void willReturnAsMuchElementsAsLeftIfEndReached() throws Exception {
        when(mResponse.getResources()).then(OnlyTwoItems.INSTANCE);

        Collection<GenericResource> result = search10itemsStrategy.searchNext();
        assertThat(result.size(), is(2));

        verify(mSearchUseCase, times(6)).performSearch(Matchers.any(InternalCriteria.class));
    }

    @Test
    public void forCustomOffsetShouldCalculateServerDisposition() throws Exception {
        when(mResponse.getResources()).thenReturn(FIVE_ITEMS);

        search10itemsStrategyWithUserOffset5.searchNext();


        InternalCriteria criteria1 = InternalCriteria.builder()
                .limit(5).create();
        verify(mSearchUseCase).performSearch(criteria1);


        InternalCriteria criteria2 = InternalCriteria.builder()
                .limit(10).offset(5).create();
        verify(mSearchUseCase).performSearch(criteria2);

        InternalCriteria criteria3 = InternalCriteria.builder()
                .limit(10).offset(15).create();
        verify(mSearchUseCase).performSearch(criteria3);

        verify(mSearchUseCase, times(3)).performSearch(Matchers.any(InternalCriteria.class));
        verifyNoMoreInteractions(mSearchUseCase);
    }

    @Test
    public void shouldReturnEmptyCollectionForZeroLimit() {
        InternalCriteria userCriteria = InternalCriteria.builder().limit(0).offset(5).create();
        EmeraldMR2SearchStrategy strategy = new EmeraldMR2SearchStrategy(userCriteria, mSearchUseCase);

        Collection<GenericResource> result = strategy.searchNext();
        assertThat(result, is(empty()));

        verifyZeroInteractions(mSearchUseCase);
    }

    private static class OnlyTwoItems implements Answer<Collection<GenericResource>> {
        public static final OnlyTwoItems INSTANCE = new OnlyTwoItems();

        private final List<GenericResource> twoItems = Arrays.asList(null, null);
        private final List<GenericResource> zeroItems = Collections.emptyList();

        private int count = 0;

        @Override
        public Collection<GenericResource> answer(InvocationOnMock invocationOnMock) throws Throwable {
            if (count == 0) {
                count++;
                return twoItems;
            }
            return zeroItems;
        }
    }
}