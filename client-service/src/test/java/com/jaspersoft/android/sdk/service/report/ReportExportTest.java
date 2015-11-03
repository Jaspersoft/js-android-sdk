package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.ReportExportRestApi;
import com.jaspersoft.android.sdk.service.auth.TokenProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportExportTest {
    @Mock
    ReportExportRestApi mExportRestApi;
    @Mock
    TokenProvider mTokenProvider;

    private ReportExport objectUnderTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ExecutionOptionsDataMapper executionOptionsDataMapper = new ExecutionOptionsDataMapper("/my/uri");
        ReportExportUseCase exportUseCase = new ReportExportUseCase(mExportRestApi, mTokenProvider, executionOptionsDataMapper);
        objectUnderTest = new ReportExport("report_execution_id", "export_id", Collections.<ReportAttachment>emptyList(), exportUseCase);
    }

    @Test
    public void testDownload() throws Exception {
        objectUnderTest.download();
        verify(mExportRestApi).requestExportOutput(anyString(), eq("report_execution_id"), eq("export_id"));
        verifyNoMoreInteractions(mExportRestApi);
    }
}