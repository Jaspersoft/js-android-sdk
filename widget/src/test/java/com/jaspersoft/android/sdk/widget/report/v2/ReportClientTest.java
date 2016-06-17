package com.jaspersoft.android.sdk.widget.report.v2;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ReportClientTest {

    private static final String REPORT_URI = "/my/uri";

    @Mock
    AuthorizedClient authorizedClient;
    @Mock
    WebView webView;
    @Mock
    PresenterCache presenterCache;
    @Mock
    ReportPresenter mockPresenter;

    private ReportClient clientUnderTest;
    private PresenterKey presenterKey;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        clientUnderTest = new ReportClient(authorizedClient, presenterCache);
    }

    @Test
    public void should_register_new_presenter_in_cache() throws Exception {
        ReportPresenter reportPresenter = clientUnderTest.newPresenter(REPORT_URI, webView);

        assertThat(reportPresenter, is(not(nullValue())));
        verify(presenterCache).put(reportPresenter);
    }

    @Test
    public void should_retrieve_running_instance_from_cache() throws Exception {
        givenPresenterInCache();

        ReportPresenter instance = clientUnderTest.getRunningInstance(presenterKey);

        assertThat(instance, is(mockPresenter));
        verify(presenterCache).get(presenterKey);
    }

    @Test
    public void should_remove_presenter_from_cache_on_demand() throws Exception {
        givenMockPresenter();

        clientUnderTest.removePresenter(mockPresenter);

        verify(mockPresenter).getKey();
        verify(presenterCache).remove(presenterKey);
    }

    private void givenPresenterInCache() {
        givenMockPresenter();
        when(presenterCache.get(presenterKey)).thenReturn(mockPresenter);
    }

    private void givenMockPresenter() {
        givenPresenterKey();
        when(mockPresenter.getKey()).thenReturn(presenterKey);
    }

    private void givenPresenterKey() {
        presenterKey = PresenterKey.newKey();
    }
}