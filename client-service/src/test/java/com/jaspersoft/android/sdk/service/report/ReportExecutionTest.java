package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.api.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatusResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ExportExecution;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.entity.export.ReportExportExecutionResponse;
import com.jaspersoft.android.sdk.service.exception.ExecutionCancelledException;
import com.jaspersoft.android.sdk.service.exception.ExecutionFailedException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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
        // execution details request
        Set<ExportExecution> exports = Collections.singleton(mExportExecution);
        when(mExportExecution.getStatus()).thenReturn("ready");
        when(mExportExecution.getId()).thenReturn("export_id");
        when(mExecDetails.getExports()).thenReturn(exports);
        when(mExecutionApi.requestReportExecutionDetails(anyString())).thenReturn(mExecDetails);

        // export run request
        when(mExportExecDetails.getExportId()).thenReturn("export_id");
        when(mExportExecDetails.getStatus()).thenReturn("ready");
        when(mExportRestApi.runExportExecution(anyString(), any(ExecutionRequestOptions.class))).thenReturn(mExportExecDetails);

        objectUnderTest.export(exportCriteria);

        verify(mapper).transformExportOptions("http:://localhost", exportCriteria);
        verify(mExportRestApi).runExportExecution(eq("execution_id"), any(ExecutionRequestOptions.class));
        verify(mExecutionApi).requestReportExecutionDetails(eq("execution_id"));
    }

    @Test
    public void testRequestRunExportFailedCase() throws Exception {
        mException.expect(ExecutionFailedException.class);
        mException.expectMessage("Export for report '/my/uri' failed on server side");

        // export run request
        when(mExportExecDetails.getExportId()).thenReturn("export_id");
        when(mExportExecDetails.getStatus()).thenReturn("failed");
        when(mExportRestApi.runExportExecution(anyString(), any(ExecutionRequestOptions.class))).thenReturn(mExportExecDetails);

        objectUnderTest.export(exportCriteria);
        verify(mapper).transformExportOptions("http:://localhost", exportCriteria);
        verify(mExportRestApi).runExportExecution(eq("execution_id"), any(ExecutionRequestOptions.class));
        verify(mExecutionApi).requestReportExecutionDetails(eq("execution_id"));
    }

    @Test
    public void testRequestRunExportCancelledCase() throws Exception {
        mException.expect(ExecutionCancelledException.class);
        mException.expectMessage("Export for report '/my/uri' was cancelled");

        // export run request
        when(mExportExecDetails.getExportId()).thenReturn("export_id");
        when(mExportExecDetails.getStatus()).thenReturn("cancelled");
        when(mExportRestApi.runExportExecution(anyString(), any(ExecutionRequestOptions.class))).thenReturn(mExportExecDetails);

        objectUnderTest.export(exportCriteria);
        verify(mapper).transformExportOptions("http:://localhost", exportCriteria);
        verify(mExportRestApi).runExportExecution(eq("execution_id"), any(ExecutionRequestOptions.class));
        verify(mExecutionApi).requestReportExecutionDetails(eq("execution_id"));
    }

    @Test
    public void testRunReportPendingCase() throws Exception {
        // export run request
        when(mExportExecDetails.getExportId()).thenReturn("export_id");
        when(mExportExecDetails.getStatus()).thenReturn("queued");
        when(mExportRestApi.runExportExecution(anyString(), any(ExecutionRequestOptions.class))).thenReturn(mExportExecDetails);

        Answer<ExecutionStatusResponse> queueReadyAnswer = new Answer<ExecutionStatusResponse>() {
            private int invocationCount = 0;
            @Override
            public ExecutionStatusResponse answer(InvocationOnMock invocation) throws Throwable {
                if (invocationCount == 0) {
                    when(mExecutionStatusResponse.getStatus()).thenReturn("queued");
                    invocationCount++;
                } else {
                    when(mExecutionStatusResponse.getStatus()).thenReturn("ready");
                }
                return mExecutionStatusResponse;
            }
        };
        when(mExportRestApi.checkExportExecutionStatus(anyString(), anyString())).then(queueReadyAnswer);

        Set<ExportExecution> exports = Collections.singleton(mExportExecution);
        when(mExportExecution.getStatus()).thenReturn("ready");
        when(mExportExecution.getId()).thenReturn("export_id");
        when(mExecDetails.getExports()).thenReturn(exports);
        when(mExecutionApi.requestReportExecutionDetails(anyString())).thenReturn(mExecDetails);

        objectUnderTest.export(exportCriteria);

        verify(mapper).transformExportOptions("http:://localhost", exportCriteria);
        verify(mExportRestApi).runExportExecution(eq("execution_id"), any(ExecutionRequestOptions.class));
        verify(mExecutionApi).requestReportExecutionDetails(eq("execution_id"));

        verify(mExportRestApi, times(2)).checkExportExecutionStatus(eq("execution_id"), eq("export_id"));
    }
}