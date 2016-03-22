package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProxyReportServiceTest {

    private static final String REPORT_URI = "/my/uri";
    private static final String OPTION_LABEL = "label";
    private static final String OPTION_ID = OPTION_LABEL;
    private static final List<ReportParameter> REPORT_PARAMETERS = Collections.emptyList();

    @Mock
    ReportServiceFactory mReportServiceFactory;
    @Mock
    ReportService mReportService;

    private ProxyReportService proxyReportService;

    @Rule
    public ExpectedException expected = none();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(mReportServiceFactory.newService()).thenReturn(mReportService);
        proxyReportService = new ProxyReportService(mReportServiceFactory);
    }

    @Test
    public void should_not_run_with_null_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report uri should not be null");
        proxyReportService.run(null, null);
    }

    @Test
    public void should_delegate_run_call() throws Exception {
        proxyReportService.run(REPORT_URI, null);
        verify(mReportServiceFactory).newService();
        verify(mReportService).run(eq(REPORT_URI), any(ReportExecutionOptions.class));
    }


}