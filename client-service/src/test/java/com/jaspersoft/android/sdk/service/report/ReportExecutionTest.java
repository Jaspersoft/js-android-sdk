package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.api.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatusResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ExportExecution;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.entity.export.ReportExportExecutionResponse;
import com.jaspersoft.android.sdk.service.data.report.ReportMetadata;
import com.jaspersoft.android.sdk.service.report.exception.ReportExportException;
import com.jaspersoft.android.sdk.service.report.exception.ReportRunException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ExecutionStatusResponse.class,
        ReportExecutionDetailsResponse.class,
        ExportExecution.class,
        ExecutionOptionsDataMapper.class,
        ReportExportExecutionResponse.class})
public class ReportExecutionTest {

    @Mock
    RunExportCriteria exportCriteria;
    @Mock
    ReportExportExecutionResponse mExportExecDetails;
    @Mock
    ReportExecutionDetailsResponse mExecDetails;
    @Mock
    ExportExecution mExportExecution;
    @Mock
    ExecutionStatusResponse mExecutionStatusResponse;

    @Mock
    ReportExportRestApi.Factory mExportApiFactory;
    @Mock
    ReportExportRestApi mExportRestApi;
    @Mock
    ReportExecutionRestApi.Factory executionApiFactory;
    @Mock
    ReportExecutionRestApi mExecutionApi;

    private ReportExecution objectUnderTest;
    private ExecutionOptionsDataMapper mapper;

    @Rule
    public ExpectedException mException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(executionApiFactory.get()).thenReturn(mExecutionApi);
        when(mExportApiFactory.get()).thenReturn(mExportRestApi);
        when(mExecDetails.getExecutionId()).thenReturn("execution_id");
        when(mExecDetails.getReportURI()).thenReturn("/report/uri");

        mapper = spy(ExecutionOptionsDataMapper.getInstance());

        objectUnderTest = new ReportExecution(
                "http:://localhost",
                TimeUnit.SECONDS.toMillis(0),
                executionApiFactory,
                mExportApiFactory,
                mapper,
                mExecDetails);
    }

    @Test
    public void testRequestExportIdealCase() throws Exception {
        mockReportExecutionDetails("ready");
        mockRunExportExecution("ready");

        objectUnderTest.export(exportCriteria);

        verify(mapper).transformExportOptions("http:://localhost", exportCriteria);
        verify(mExportRestApi).runExportExecution(eq("execution_id"), any(ExecutionRequestOptions.class));
        verify(mExecutionApi).requestReportExecutionDetails(eq("execution_id"));
    }

    @Test
    public void testRunThrowsFailedStatusImmediately() throws Exception {
        mException.expect(ReportExportException.class);
        mException.expectMessage("Export for report '/report/uri' failed on server side");

        // export run request
        mockRunExportExecution("failed");

        objectUnderTest.export(exportCriteria);
    }

    @Test
    public void testRunShouldThrowFailedIfStatusFailed() {
        mException.expect(ReportExportException.class);
        mException.expectMessage("Export for report '/report/uri' failed on server side");

        mockRunExportExecution("queued");

        mockCheckExportExecStatus("failed");
        objectUnderTest.export(exportCriteria);
    }

    @Test
    public void testRunThrowsCancelledStatusImmediately() throws Exception {
        mException.expect(ReportExportException.class);
        mException.expectMessage("Export for report '/report/uri' was cancelled");

        // export run request
        mockRunExportExecution("cancelled");

        objectUnderTest.export(exportCriteria);
    }

    @Test
    public void testRunShouldThrowCancelledIfStatusCancelled() {
        mException.expect(ReportExportException.class);
        mException.expectMessage("Export for report '/report/uri' was cancelled");

        mockRunExportExecution("queued");
        mockCheckExportExecStatus("cancelled");

        objectUnderTest.export(exportCriteria);
    }

    @Test
    public void testRunReportPendingCase() throws Exception {
        mockRunExportExecution("queued");
        mockCheckExportExecStatus("queued", "ready");
        mockReportExecutionDetails("ready");

        objectUnderTest.export(exportCriteria);

        verify(mExportRestApi, times(2)).checkExportExecutionStatus(eq("execution_id"), eq("export_id"));
    }

    @Test
    public void ensureThatExportCancelledEventWillBeResolved() {
        mockRunExportExecution("cancelled", "ready");
        mockReportExecutionDetails("ready");

        objectUnderTest.export(exportCriteria);

        verify(mExportRestApi, times(2)).runExportExecution(eq("execution_id"), any(ExecutionRequestOptions.class));
    }

    @Test
    public void testAwaitCompleteReport() throws Exception {
        when(mExecDetails.getReportURI()).thenReturn("/report/uri");
        when(mExecDetails.getTotalPages()).thenReturn(100);
        mockReportExecutionDetails("ready");

        ReportMetadata metadata = objectUnderTest.waitForReportCompletion();
        assertThat(metadata.getTotalPages(), is(100));
        assertThat(metadata.getUri(), is("/report/uri"));

        verify(mExecutionApi).requestReportExecutionDetails(anyString());
        verifyNoMoreInteractions(mExecutionApi);
    }

    @Test
    public void testAwaitCompleteReportShouldLoopCalls() throws Exception {
        mockReportExecutionDetails("execution", "ready");

        objectUnderTest.waitForReportCompletion();

        verify(mExecutionApi, times(2)).requestReportExecutionDetails(anyString());
        verifyNoMoreInteractions(mExecutionApi);
    }

    @Test
    public void testAwaitCompleteReportThrowCancelledIfStatusCancelled() {
        mException.expect(ReportRunException.class);
        mException.expectMessage("Report execution '/report/uri' was cancelled");

        mockReportExecutionDetails("execution", "cancelled");

        objectUnderTest.waitForReportCompletion();
    }

    @Test
    public void testAwaitCompleteReportThrowFailedIfStatusFailed() {
        mException.expect(ReportRunException.class);
        mException.expectMessage("Report execution '/report/uri' failed on server side");

        mockReportExecutionDetails("execution", "failed");

        objectUnderTest.waitForReportCompletion();
    }

    private void mockCheckExportExecStatus(String... statusChain) {
        ensureChain(statusChain);
        when(mExecutionStatusResponse.getStatus()).then(StatusChain.of(statusChain));
        when(mExportRestApi.checkExportExecutionStatus(anyString(), anyString())).thenReturn(mExecutionStatusResponse);
    }

    private void mockRunExportExecution(String... statusChain) {
        ensureChain(statusChain);
        when(mExportExecDetails.getExportId()).thenReturn("export_id");
        when(mExportExecDetails.getStatus()).then(StatusChain.of(statusChain));
        when(mExportRestApi.runExportExecution(anyString(), any(ExecutionRequestOptions.class))).thenReturn(mExportExecDetails);
    }

    private void mockReportExecutionDetails(String... statusChain) {
        ensureChain(statusChain);
        Set<ExportExecution> exports = Collections.singleton(mExportExecution);
        when(mExportExecution.getStatus()).thenReturn("execution");
        when(mExportExecution.getId()).thenReturn("export_id");
        when(mExecDetails.getExports()).thenReturn(exports);
        when(mExecDetails.getStatus()).then(StatusChain.of(statusChain));
        when(mExecutionApi.requestReportExecutionDetails(anyString())).thenReturn(mExecDetails);
    }

    private void ensureChain(String[] statusChain) {
        if (statusChain.length == 0) {
            throw new IllegalArgumentException("You should supply at least one status: ready, queued, execution, cancelled, failed");
        }
    }
}