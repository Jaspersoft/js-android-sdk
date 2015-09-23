package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.api.ServerRestApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SearchStrategy.Factory.class)
public class SearchTaskTest {

    SearchCriteria mSearchCriteria = SearchCriteria.none();

    @Mock
    RepositoryRestApi.Factory mRepoApiFactory;
    @Mock
    ServerRestApi.Factory mInfoApiFactory;
    @Mock
    RepositoryRestApi mRepoApi;
    @Mock
    ServerRestApi mInfoApi;
    @Mock
    SearchStrategy mSearchStrategy;

    private SearchTask objectUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectUnderTest = new SearchTask(mSearchCriteria, mRepoApiFactory, mInfoApiFactory);
        when(mRepoApiFactory.get()).thenReturn(mRepoApi);
        when(mInfoApiFactory.get()).thenReturn(mInfoApi);

        Observable<SearchResult> resultObservable = Observable.just(null);
        when(mSearchStrategy.search()).thenReturn(resultObservable);

        PowerMockito.mockStatic(SearchStrategy.Factory.class);
        PowerMockito.when(SearchStrategy.Factory.get(anyString(), any(RepositoryRestApi.Factory.class), any(SearchCriteria.class))).thenReturn(mSearchStrategy);
    }

    @Test
    public void nextLookupShouldDefineSearchStrategy() {
        when(mInfoApi.requestVersion()).thenReturn("5.5");
        objectUnderTest.nextLookup().toBlocking().first();

        PowerMockito.verifyStatic(times(1));
        SearchStrategy.Factory.get(eq("5.5"), eq(mRepoApiFactory), eq(mSearchCriteria));

        verify(mInfoApi, times(1)).requestVersion();
        verify(mInfoApiFactory, times(1)).get();
        verify(mSearchStrategy, times(1)).search();
    }

    @Test
    public void secondLookupShouldUseCachedStrategy() {
        when(mInfoApi.requestVersion()).thenReturn("5.5");

        objectUnderTest.nextLookup().toBlocking().first();
        objectUnderTest.nextLookup().toBlocking().first();

        PowerMockito.verifyStatic(times(1));
        SearchStrategy.Factory.get(eq("5.5"), eq(mRepoApiFactory), eq(mSearchCriteria));

        verify(mInfoApi, times(1)).requestVersion();
        verify(mInfoApiFactory, times(1)).get();
        verify(mSearchStrategy, times(2)).search();
    }
}