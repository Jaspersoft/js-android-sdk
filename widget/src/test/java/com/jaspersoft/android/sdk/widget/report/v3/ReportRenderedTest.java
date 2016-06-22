package com.jaspersoft.android.sdk.widget.report.v3;

import android.content.Context;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.Server;
import com.jaspersoft.android.sdk.network.SpringCredentials;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ReportRenderedTest {
    private final static int WEB_VIEW_ID = 1251251;
    private final static int WEB_VIEW_HASH = 55212521;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RenderersStore renderersStore;
    private ReportRendered reportRendered;
    private ReportRendererKey reportRendererKey;
    private WebView webView;
    private AuthorizedClient authorizedClient;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        webView = new FakeWebView(null, WEB_VIEW_ID);
        authorizedClient = Server.builder().withBaseUrl("http://test.com").build().newClient(SpringCredentials.builder().withUsername("asf").withPassword("asf").build()).create();
        reportRendered = new ReportRendered.Builder()
                .withWebView(webView)
                .withClient(authorizedClient)
                .build();
        reportRendererKey = ReportRendererKey.newKey();
        renderersStore = RenderersStore.INSTANCE;
    }

    @Test
    public void should_build() throws Exception {
        assertThat(reportRendered, is(notNullValue()));
    }

    @Test
    public void should_not_build_without_web_view() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("WebView should be provided.");

        new ReportRendered.Builder()
                .withWebView(null)
                .withClient(authorizedClient)
                .build();
    }

    @Test
    public void should_not_build_without_client() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Client should be provided.");

        new ReportRendered.Builder()
                .withWebView(webView)
                .withClient(null)
                .build();
    }

    @Test
    public void should_persist() throws Exception {
        ReportRendererKey key = reportRendered.persist();
        assertThat(key, is(notNullValue()));
    }

    @Test
    public void should_restore() throws Exception {
        renderersStore.reportExecutorList.put(reportRendererKey, reportRendered);

        ReportRendered restoredExecutor = new ReportRendered.Builder()
                .withKey(reportRendererKey)
                .build();
        assertThat(restoredExecutor, equalTo(reportRendered));
    }

    @Test
    public void should_not_restore_with_wrong_key() throws Exception {
        renderersStore.reportExecutorList.put(reportRendererKey, reportRendered);

        ReportRendered restoredReportRendered = new ReportRendered.Builder()
                .withKey(ReportRendererKey.newKey())
                .build();
        assertThat(restoredReportRendered, is(nullValue()));
    }

    private class FakeWebView extends WebView {
        private int id;

        public FakeWebView(Context context, int id) {
            super(context);
            this.id = id;
        }

        @Override
        public int hashCode() {
            return WEB_VIEW_HASH;
        }

        @Override
        public int getId() {
            return id;
        }
    }
}