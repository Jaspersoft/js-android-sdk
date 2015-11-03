package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.jaspersoft.android.sdk.service.auth.TokenProvider;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
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
@PrepareForTest({OutputResource.class})
public class ReportAttachmentTest {
    @Mock
    ReportExportRestApi mExportRestApi;
    @Mock
    TokenProvider mTokenProvider;
    @Mock
    OutputResource input;

    private ReportAttachment objectUnderTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mTokenProvider.provideToken()).thenReturn("cookie");

        ExecutionOptionsDataMapper executionOptionsDataMapper = new ExecutionOptionsDataMapper("/my/uri");
        ReportExportUseCase exportUseCase = new ReportExportUseCase(mExportRestApi, mTokenProvider, executionOptionsDataMapper);
        objectUnderTest = new ReportAttachment("1.jpg", "exec_id", "export_id", exportUseCase);
    }

    @Test
    public void testDownload() throws Exception {
        when(mExportRestApi.requestExportAttachment(anyString(), anyString(), anyString(), anyString())).thenReturn(input);

        ResourceOutput result = objectUnderTest.download();
        assertThat(result, is(notNullValue()));

        verify(mExportRestApi).requestExportAttachment(eq("cookie"), eq("exec_id"), eq("export_id"), eq("1.jpg"));
        verifyNoMoreInteractions(mExportRestApi);
    }
}