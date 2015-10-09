package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.api.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatusResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ExportExecution;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.entity.export.ReportExportExecutionResponse;
import com.jaspersoft.android.sdk.service.exception.ExecutionException;

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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
        when(mExecDetails.getReportURI()).thenReturn("/my/uri");

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
    public void testRequestDetails() throws Exception {
        objectUnderTest.requestDetails();
        verify(mExecutionApi).requestReportExecutionDetails("execution_id");
    }

    @Test
    public void testRequestExportIdealCase() throws Exception {
        mockReportExecutionDetails();
        mockRunReportExecution("ready");

        objectUnderTest.export(exportCriteria);

        verify(mapper).transformExportOptions("http:://localhost", exportCriteria);
        verify(mExportRestApi).runExportExecution(eq("execution_id"), any(ExecutionRequestOptions.class));
        verify(mExecutionApi).requestReportExecutionDetails(eq("execution_id"));
    }

    @Test
    public void testRunThrowsFailedStatusImmediately() throws Exception {
        mException.expect(ExecutionException.class);
        mException.expectMessage("Export for report '/my/uri' failed on server side");

        // export run request
        mockRunReportExecution("failed");

        objectUnderTest.export(exportCriteria);
    }

    @Test
    public void testRunShouldThrowFailedIfStatusFailed() {
        mException.expect(ExecutionException.class);
        mException.expectMessage("Export for report '/my/uri' failed on server side");

        mockRunReportExecution("queued");

        mockCheckExportExecStatus("failed");
        objectUnderTest.export(exportCriteria);
    }

    @Test
    public void testRunThrowsCancelledStatusImmediately() throws Exception {
        mException.expect(ExecutionException.class);
        mException.expectMessage("Export for report '/my/uri' was cancelled");

        // export run request
        mockRunReportExecution("cancelled");

        objectUnderTest.export(exportCriteria);
    }

    @Test
    public void testRunShouldThrowCancelledIfStatusCancelled() {
        mException.expect(ExecutionException.class);
        mException.expectMessage("Export for report '/my/uri' was cancelled");

        mockRunReportExecution("queued");
        mockCheckExportExecStatus("cancelled");

        objectUnderTest.export(exportCriteria);
    }

    @Test
    public void testRunReportPendingCase() throws Exception {
        mockRunReportExecution("queued");
        mockCheckExportExecStatus("queued", "ready");
        mockReportExecutionDetails();

        objectUnderTest.export(exportCriteria);

        verify(mExportRestApi, times(2)).checkExportExecutionStatus(eq("execution_id"), eq("export_id"));
    }

    @Test
    public void ensureThatExportCancelledEventWillBeResolved() {
        mockRunReportExecution("cancelled", "ready");
        mockReportExecutionDetails();

        objectUnderTest.export(exportCriteria);

        verify(mExportRestApi, times(2)).runExportExecution(eq("execution_id"), any(ExecutionRequestOptions.class));
    }

    private void mockCheckExportExecStatus(String... statusChain) {
        when(mExecutionStatusResponse.getStatus()).then(StatusChain.of(statusChain));
        when(mExportRestApi.checkExportExecutionStatus(anyString(), anyString())).thenReturn(mExecutionStatusResponse);
    }

    private void mockRunReportExecution(String... statusChain) {
        when(mExportExecDetails.getExportId()).thenReturn("export_id");
        when(mExportExecDetails.getStatus()).then(StatusChain.of(statusChain));
        when(mExportRestApi.runExportExecution(anyString(), any(ExecutionRequestOptions.class))).thenReturn(mExportExecDetails);
    }

    private void mockReportExecutionDetails() {
        Set<ExportExecution> exports = Collections.singleton(mExportExecution);
        when(mExportExecution.getStatus()).thenReturn("ready");
        when(mExportExecution.getId()).thenReturn("export_id");
        when(mExecDetails.getExports()).thenReturn(exports);
        when(mExecutionApi.requestReportExecutionDetails(anyString())).thenReturn(mExecDetails);
    }
}