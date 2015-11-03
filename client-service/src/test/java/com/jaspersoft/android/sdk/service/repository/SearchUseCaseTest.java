package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;
import com.jaspersoft.android.sdk.service.InfoProvider;
import com.jaspersoft.android.sdk.service.auth.TokenProvider;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.SearchResult;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.anyString;
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
    ResourceMapper mDataMapper;
    @Mock
    TokenProvider mTokenProvider;
    @Mock
    InfoProvider mInfoProvider;

    @Mock
    ResourceLookup mResourceLookup;

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
        objectUnderTest = new SearchUseCase(mDataMapper, mRepositoryRestApi, mTokenProvider, mInfoProvider);
    }

    @Test
    public void shouldProvideAndAdaptSearchResult() {
        when(mResult.getNextOffset()).thenReturn(100);
        when(mRepositoryRestApi.searchResources(anyString(), any(Map.class))).thenReturn(mResult);
        when(mInfoProvider.provideDateTimeFormat()).thenReturn(DATE_TIME_FORMAT);

        Collection<Resource> resources = new ArrayList<Resource>();
        when(mDataMapper.transform(anyCollection(), any(SimpleDateFormat.class))).thenReturn(resources);

        SearchResult result = objectUnderTest.performSearch(mCriteria);
        assertThat(result, is(not(nullValue())));
        assertThat(result.getNextOffset(), is(100));
        assertThat(result.getResources(), is(resources));

        verify(mRepositoryRestApi).searchResources(anyString(), any(Map.class));
    }
}