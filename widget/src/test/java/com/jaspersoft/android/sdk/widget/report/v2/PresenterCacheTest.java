package com.jaspersoft.android.sdk.widget.report.v2;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class PresenterCacheTest {

    private PresenterCache cacheUnderTest;

    @Mock
    ReportPresenter presenterUnderTest;

    private PresenterKey presenterKey;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        cacheUnderTest = PresenterCache.getInstance();
        presenterKey = PresenterKey.newKey();
    }

    @Test
    public void should_cache_instances() throws Exception {
        when(presenterUnderTest.getKey()).thenReturn(presenterKey);
        cacheUnderTest.put(presenterUnderTest);

        ReportPresenter cachedPresenter = cacheUnderTest.get(presenterKey);
        assertThat(cachedPresenter, is(presenterUnderTest));

        cacheUnderTest.remove(presenterKey);

        ReportPresenter noPresenter = cacheUnderTest.get(presenterKey);
        assertThat(noPresenter, is(nullValue()));
    }
}