package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.api.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.export.ReportExportExecutionResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ExecutionConfiguration.class, ExecutionOptionsDataMapper.class, ReportExportExecutionResponse.class})
public class ReportExecutionTest {
    @Mock
    ExecutionConfiguration configuration;
    @Mock
    ReportExportExecutionResponse mExecDetails;

    @Mock
    ReportExportRestApi.Factory mExportApiFactory;
    @Mock
    ReportExportRestApi mExportRestApi;
    @Mock
    ReportExecutionRestApi.Factory executionApiFactory;
    @Mock
    ReportExecutionRestApi executionApi;

    private ReportExecution objectUnderTest;
    private ExecutionOptionsDataMapper mapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(executionApiFactory.get()).thenReturn(executionApi);
        when(mExportApiFactory.get()).thenReturn(mExportRestApi);

        mapper = spy(ExecutionOptionsDataMapper.getInstance());
        objectUnderTest = new ReportExecution(
                "http:://localhost",
                executionApiFactory,
                mExportApiFactory,
                mapper,
                "execution_id");
    }

    @Test
    public void testRequestDetails() throws Exception {
        objectUnderTest.requestDetails();
        verify(executionApi).requestReportExecutionDetails("execution_id");
    }

    @Test
    public void testRequestExport() throws Exception {
        when(mExecDetails.getExportId()).thenReturn("export_id");
        when(mExportRestApi.runExportExecution(anyString(), any(ExecutionRequestOptions.class))).thenReturn(mExecDetails);
        objectUnderTest.requestExport(configuration);

        verify(mapper).transformExportOptions("http:://localhost", configuration);
        verify(mExportRestApi).runExportExecution(eq("execution_id"), any(ExecutionRequestOptions.class));
    }
}