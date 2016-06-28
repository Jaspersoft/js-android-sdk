package com.jaspersoft.android.sdk.widget.report;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsMapContaining.hasEntry;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ReportRenderedStoreTest {

    private RenderersStore renderersStore;
    private ReportRendered reportRendered;
    private ReportRendererKey reportRendererKey;

    @Mock
    public Dispatcher dispatcher;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        renderersStore = RenderersStore.INSTANCE;
        reportRendererKey = ReportRendererKey.newKey();

        Mockito.doNothing().when(dispatcher).register(null);
        reportRendered = new ReportRendered(dispatcher, null, null);
    }

    @Test
    public void should_store_executor() throws Exception {
        ReportRendererKey savedReportRendererKey = renderersStore.saveExecutor(reportRendered);
        assertThat(renderersStore.reportExecutorList, hasEntry(savedReportRendererKey, reportRendered));
    }

    @Test
    public void should_get_executor() throws Exception {
        renderersStore.reportExecutorList.put(reportRendererKey, reportRendered);
        ReportRendered restoredExecutor = renderersStore.restoreExecutor(reportRendererKey);
        assertThat(restoredExecutor, equalTo(reportRendered));
    }

    @Test
    public void should_remove_restored_executor() throws Exception {
        renderersStore.reportExecutorList.put(reportRendererKey, reportRendered);
        renderersStore.restoreExecutor(reportRendererKey);
        assertThat(renderersStore.reportExecutorList, not(hasEntry(reportRendererKey, reportRendered)));
    }

    @Test
    public void should_not_restore_with_unsaved_key() throws Exception {
        ReportRendered restoredExecutor = renderersStore.restoreExecutor(ReportRendererKey.newKey());
        assertThat(restoredExecutor, is(nullValue()));
    }
}