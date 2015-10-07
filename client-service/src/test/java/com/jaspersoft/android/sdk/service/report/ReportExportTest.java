package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.export.ExportResourceResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ExportResourceResponse.class})
public class ReportExportTest {
    @Mock
    ReportExportRestApi.Factory mExportApiFactory;
    @Mock
    ReportExportRestApi mExportRestApi;
    @Mock
    ExportResourceResponse mExportResourceResponse;

    private ReportExport objectUnderTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(mExportApiFactory.get()).thenReturn(mExportRestApi);
        objectUnderTest = new ReportExport("report_execution_id", "export_id", mExportApiFactory);
    }

    @Test
    public void testDownload() throws Exception {
        objectUnderTest.download();
        verify(mExportRestApi).requestExportOutput(eq("report_execution_id"), eq("export_id"));
        verifyNoMoreInteractions(mExportRestApi);
    }
}