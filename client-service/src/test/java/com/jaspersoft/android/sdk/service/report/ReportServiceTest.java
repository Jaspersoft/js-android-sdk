package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.api.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.api.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ExecutionConfiguration.class, ReportExecutionDetailsResponse.class, ExecutionOptionsDataMapper.class})
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
    ExecutionConfiguration configuration;
    @Mock
    ReportExecutionDetailsResponse details;

    ReportService objectUnderTest;

    private ExecutionOptionsDataMapper mapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(executionApiFactory.get()).thenReturn(executionApi);
        when(infoApiFactory.get()).thenReturn(infoApi);

        mapper = spy(ExecutionOptionsDataMapper.getInstance());
        objectUnderTest = new ReportService("http:://localhost",
                executionApiFactory,
                mExportApiFactory,
                infoApiFactory,
                mapper);
    }

    @Test
    public void testRunShouldCreateActiveSession() {
        when(details.getExecutionId()).thenReturn("any_id");
        when(executionApi.runReportExecution(any(ReportExecutionRequestOptions.class))).thenReturn(details);

        ExecutionSession session = objectUnderTest.run("/report/uri", configuration);
        assertThat(session, is(notNullValue()));

        verify(mapper).transform("/report/uri", "http:://localhost", configuration);
        verify(executionApi).runReportExecution(any(ReportExecutionRequestOptions.class));
    }

}