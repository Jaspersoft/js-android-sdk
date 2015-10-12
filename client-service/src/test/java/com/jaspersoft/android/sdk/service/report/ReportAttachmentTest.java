package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExportExecution;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.entity.export.ExportInput;
import com.jaspersoft.android.sdk.network.entity.export.ExportResourceResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ExportInput.class})
public class ReportAttachmentTest {
    @Mock
    ReportExportRestApi.Factory mExportApiFactory;
    @Mock
    ReportExportRestApi mExportRestApi;
    @Mock
    ExportInput input;

    private ReportAttachment objectUnderTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(mExportApiFactory.get()).thenReturn(mExportRestApi);

        objectUnderTest = new ReportAttachment("1.jpg", "exec_id", "export_id", mExportApiFactory);
    }

    @Test
    public void testDownload() throws Exception {
        when(mExportRestApi.requestExportAttachment(anyString(), anyString(), anyString())).thenReturn(input);

        ExportInput result = objectUnderTest.download();
        assertThat(result, is(notNullValue()));

        verify(mExportRestApi).requestExportAttachment(eq("exec_id"), eq("export_id"), eq("1.jpg"));
        verifyNoMoreInteractions(mExportRestApi);
    }
}