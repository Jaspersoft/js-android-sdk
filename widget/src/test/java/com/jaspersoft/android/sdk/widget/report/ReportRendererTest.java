package com.jaspersoft.android.sdk.widget.report;

import android.content.Context;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.Server;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.widget.RenderersStore;
import com.jaspersoft.android.sdk.widget.report.view.ReportRendererKey;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportRenderer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
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
public class ReportRendererTest {
    private final static int WEB_VIEW_ID = 1251251;
    private final static int WEB_VIEW_HASH = 55212521;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RenderersStore renderersStore;
    private ReportRenderer reportRenderer;
    private ReportRendererKey reportRendererKey;
    private WebView webView;
    private AuthorizedClient authorizedClient;

    @Mock
    public Dispatcher dispatcher;
    @Mock
    public ServerInfo serverInfo;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        webView = new FakeWebView(null, WEB_VIEW_ID);
        authorizedClient = Server.builder().withBaseUrl("http://test.com").build().newClient(SpringCredentials.builder().withUsername("asf").withPassword("asf").build()).create();
        Mockito.doNothing().when(dispatcher).register(null);
        reportRenderer = new ReportRenderer(dispatcher, null, null, null, reportActionsCompat);
        reportRendererKey = ReportRendererKey.newKey();
        renderersStore = RenderersStore.INSTANCE;
    }

    @Test
    public void should_not_build_without_web_view() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("WebView should be provided.");

        ReportRenderer.create(authorizedClient, null, serverInfo);
    }

    @Test
    public void should_not_build_without_client() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Client should be provided.");

        ReportRenderer.create(null, webView, serverInfo);
    }

    @Test
    public void should_not_build_without_server_info() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("ServerInfo should be provided.");

        ReportRenderer.create(authorizedClient, webView, null);
    }

    @Test
    public void should_persist() throws Exception {
        ReportRendererKey key = reportRenderer.persist();
        assertThat(key, is(notNullValue()));
    }

    @Test
    public void should_restore() throws Exception {
        renderersStore.reportExecutorList.put(reportRendererKey, reportRenderer);

        ReportRenderer restoredExecutor = ReportRenderer.restore(reportRendererKey);
        assertThat(restoredExecutor, equalTo(reportRenderer));
    }

    @Test
    public void should_not_restore_with_wrong_key() throws Exception {
        renderersStore.reportExecutorList.put(reportRendererKey, reportRenderer);

        ReportRenderer restoredReportRenderer = ReportRenderer.restore(ReportRendererKey.newKey());
        assertThat(restoredReportRenderer, is(nullValue()));
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