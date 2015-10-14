package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;
import com.jaspersoft.android.sdk.service.data.repository.GenericResource;
import com.jaspersoft.android.sdk.service.data.repository.SearchResult;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class SearchResultMapperTest {

    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat();
    @Mock
    GenericResourceMapper mGenericResourceMapper;
    @Mock
    GenericResource mGenericResource;

    @Mock
    ResourceSearchResult mSearchResult;
    @Mock
    ServerInfo mServerInfo;
    @Mock
    ResourceLookup mLookup;

    private SearchResultMapper objectUnderTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        objectUnderTest = new SearchResultMapper(mGenericResourceMapper);
    }

    @Test
    public void testTransform() throws Exception {
        List<ResourceLookup> resources = Collections.singletonList(mLookup);
        when(mSearchResult.getNextOffset()).thenReturn(100);
        when(mSearchResult.getResources()).thenReturn(resources);

        Collection<GenericResource> genericResources = Collections.singletonList(mGenericResource);
        when(mGenericResourceMapper.transform(anyList(), any(SimpleDateFormat.class))).thenReturn(genericResources);
        SearchResult searchResult = objectUnderTest.transform(mSearchResult, DATE_TIME_FORMAT);

        assertThat(searchResult.getNextOffset(), is(100));
        assertThat(searchResult.getResources(), contains(mGenericResource));

        verify(mGenericResourceMapper).transform(resources, DATE_TIME_FORMAT);
        verify(mSearchResult).getNextOffset();
        verify(mSearchResult).getResources();
    }
}