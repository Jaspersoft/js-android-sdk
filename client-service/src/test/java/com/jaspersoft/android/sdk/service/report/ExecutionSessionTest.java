package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.api.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ReportExecutionDetailsResponse.class})
public class ExecutionSessionTest {
    private static final String REQUEST_ID = "any_id";


    @Mock
    ReportExportRestApi.Factory mExportApiFactory;

    @Mock
    ReportExecutionRestApi.Factory executionApiFactory;
    @Mock
    ReportExecutionRestApi executionApi;
    @Mock
    ReportExecutionDetailsResponse mDetails;

    private ExecutionSession objectUnderTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(executionApiFactory.get()).thenReturn(executionApi);
        when(mDetails.getExecutionId()).thenReturn(REQUEST_ID);
        objectUnderTest = new ExecutionSession(executionApiFactory, mExportApiFactory, mDetails);
    }

    @Test
    public void testRequestDetails() throws Exception {
        objectUnderTest.requestDetails();
        verify(executionApi).requestReportExecutionDetails(REQUEST_ID);
    }

    @Test
    public void testRequestStatus() throws Exception {
        objectUnderTest.requestStatus();
        verify(executionApi).requestReportExecutionStatus(REQUEST_ID);
    }
}