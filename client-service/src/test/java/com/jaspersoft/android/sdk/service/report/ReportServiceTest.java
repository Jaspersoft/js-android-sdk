package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.api.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.api.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatusResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
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

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Matchers.any;
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
        ExecutionOptionsDataMapper.class})
public class ReportServiceTest {

    @Mock
    ReportExecutionRestApi.Factory executionApiFactory;
    @Mock
    ReportExecutionRestApi executionApi;
    @Mock
    ServerRestApi.Factory infoApiFactory;
    @Mock
    ServerRestApi infoApi;

    @Mock
    ReportExportRestApi.Factory mExportApiFactory;

    @Mock
    RunReportCriteria configuration;
    @Mock
    ReportExecutionDetailsResponse initDetails;
    @Mock
    ExecutionStatusResponse statusDetails;

    ReportService objectUnderTest;

    @Rule
    public ExpectedException mException = ExpectedException.none();

    private ExecutionOptionsDataMapper mapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(executionApiFactory.get()).thenReturn(executionApi);
        when(infoApiFactory.get()).thenReturn(infoApi);

        mapper = spy(ExecutionOptionsDataMapper.getInstance());
        objectUnderTest = new ReportService("http:://localhost",
                TimeUnit.MILLISECONDS.toMillis(0),
                executionApiFactory,
                mExportApiFactory,
                infoApiFactory,
                mapper);
    }

    @Test
    public void testRunShouldCreateActiveSession() {
        when(initDetails.getStatus()).thenReturn("execution");
        when(initDetails.getExecutionId()).thenReturn("exec_id");
        when(executionApi.runReportExecution(any(ReportExecutionRequestOptions.class))).thenReturn(initDetails);

        when(executionApi.requestReportExecutionStatus(any(String.class))).thenReturn(statusDetails);
        when(statusDetails.getStatus()).thenReturn("ready");

        ReportExecution session = objectUnderTest.run("/report/uri", configuration);
        assertThat(session, is(notNullValue()));

        verify(mapper).transformRunReportOptions("/report/uri", "http:://localhost", configuration);
        verify(executionApi).runReportExecution(any(ReportExecutionRequestOptions.class));
    }

    @Test
    public void testRunThrowsFailedStatusImmediately() {
        mException.expect(ExecutionFailedException.class);
        mException.expectMessage("Report execution '/report/uri' failed on server side");

        when(initDetails.getExecutionId()).thenReturn("exec_id");
        when(initDetails.getStatus()).thenReturn("failed");
        when(executionApi.runReportExecution(any(ReportExecutionRequestOptions.class))).thenReturn(initDetails);

        objectUnderTest.run("/report/uri", configuration);
    }

    @Test
    public void testRunShouldThrowFailedIfStatusFailed() {
        mException.expect(ExecutionFailedException.class);
        mException.expectMessage("Report execution '/report/uri' failed on server side");

        when(initDetails.getStatus()).thenReturn("queued");
        when(initDetails.getExecutionId()).thenReturn("exec_id");
        when(executionApi.runReportExecution(any(ReportExecutionRequestOptions.class))).thenReturn(initDetails);

        when(executionApi.requestReportExecutionStatus(any(String.class))).thenReturn(statusDetails);
        when(statusDetails.getStatus()).thenReturn("failed");

        objectUnderTest.run("/report/uri", configuration);
    }

    @Test
    public void testRunThrowsCancelledStatusImmediately() {
        mException.expect(ExecutionCancelledException.class);
        mException.expectMessage("Report execution '/report/uri' was cancelled");

        when(initDetails.getExecutionId()).thenReturn("exec_id");
        when(initDetails.getStatus()).thenReturn("cancelled");
        when(executionApi.runReportExecution(any(ReportExecutionRequestOptions.class))).thenReturn(initDetails);

        objectUnderTest.run("/report/uri", configuration);
    }

    @Test
    public void testRunShouldThrowCancelledIfStatusCancelled() {
        mException.expect(ExecutionCancelledException.class);
        mException.expectMessage("Report execution '/report/uri' was cancelled");

        when(initDetails.getStatus()).thenReturn("queued");
        when(initDetails.getExecutionId()).thenReturn("exec_id");
        when(executionApi.runReportExecution(any(ReportExecutionRequestOptions.class))).thenReturn(initDetails);

        when(statusDetails.getStatus()).thenReturn("cancelled");
        when(executionApi.requestReportExecutionStatus(any(String.class))).thenReturn(statusDetails);

        objectUnderTest.run("/report/uri", configuration);
    }

    @Test
    public void testRunShouldLoopUntilStatusExecution() {
        when(initDetails.getStatus()).thenReturn("queued");
        when(initDetails.getExecutionId()).thenReturn("exec_id");
        when(executionApi.runReportExecution(any(ReportExecutionRequestOptions.class))).thenReturn(initDetails);

        when(statusDetails.getStatus()).then(new Answer<String>() {
            private int count = 0;

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                if (count > 0) {
                    return "execution";
                } else {
                    count++;
                    return "queued";
                }
            }
        });
        when(executionApi.requestReportExecutionStatus(any(String.class))).thenReturn(statusDetails);

        objectUnderTest.run("/report/uri", configuration);
        verify(executionApi, times(2)).requestReportExecutionStatus(eq("exec_id"));
    }
}