package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;
import com.jaspersoft.android.sdk.service.data.repository.SearchResult;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class SearchUseCaseTest {

    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat();
    @Mock
    RepositoryRestApi mRepositoryRestApi;
    @Mock
    SearchResultMapper mDataMapper;

    @Mock
    ResourceLookup mResourceLookup;
    @Mock
    SearchResult mAdaptedSearchResult;

    @Mock
    InternalCriteria mCriteria;
    @Mock
    ServerInfo mServerInfo;

    @Mock
    ResourceSearchResult mResult;

    private SearchUseCase objectUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectUnderTest = new SearchUseCase(mRepositoryRestApi, mDataMapper);
    }

    @Test
    public void shouldProvideAndAdaptSearchResult() {
        when(mRepositoryRestApi.searchResources(any(Map.class))).thenReturn(mResult);
        when(mDataMapper.transform(any(ResourceSearchResult.class), any(SimpleDateFormat.class))).thenReturn(mAdaptedSearchResult);

        SearchResult result = objectUnderTest.performSearch(mCriteria, DATE_TIME_FORMAT);
        assertThat(result, is(mAdaptedSearchResult));

        verify(mRepositoryRestApi).searchResources(any(Map.class));
        verify(mDataMapper).transform(mResult, DATE_TIME_FORMAT);
    }
}