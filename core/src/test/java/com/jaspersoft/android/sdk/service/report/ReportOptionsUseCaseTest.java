package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.ReportOptionRestApi;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReportOptionsUseCaseTest {
    private static final String REPORT_URI = "/my/uri";
    private static final String OPTION_LABEL = "label";
    private static final String OPTION_ID = OPTION_LABEL;
    private static final List<ReportParameter> REPORT_PARAMETERS = Collections.emptyList();

    @Mock
    ServiceExceptionMapper mExceptionMapper;
    @Mock
    ReportOptionRestApi mReportOptionRestApi;

    @Mock
    IOException mIOException;
    @Mock
    HttpException mHttpException;
    @Mock
    ServiceException mServiceException;

    private ReportOptionsUseCase reportOptionsUseCase;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        setupMocks();
        reportOptionsUseCase = new ReportOptionsUseCase(mExceptionMapper, mReportOptionRestApi);
    }

    @Test
    public void should_list_report_options() throws Exception {
        reportOptionsUseCase.requestReportOptionsList(REPORT_URI);
        verify(mReportOptionRestApi).requestReportOptionsList(REPORT_URI);
    }

    @Test
    public void list_report_options_adapts_io_exception() throws Exception {
        when(mReportOptionRestApi.requestReportOptionsList(anyString())).thenThrow(mIOException);
        try {
            reportOptionsUseCase.requestReportOptionsList(REPORT_URI);
            fail("Should adapt IO exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mIOException);
        }
    }

    @Test
    public void list_report_options_adapts_http_exception() throws Exception {
        when(mReportOptionRestApi.requestReportOptionsList(anyString())).thenThrow(mHttpException);

        try {
            reportOptionsUseCase.requestReportOptionsList(REPORT_URI);
            fail("Should adapt HTTP exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mHttpException);
        }
    }

    @Test
    public void should_create_report_option() throws Exception {
        reportOptionsUseCase.createReportOption(REPORT_URI, OPTION_LABEL, REPORT_PARAMETERS, true);
        verify(mReportOptionRestApi).createReportOption(REPORT_URI, OPTION_LABEL, REPORT_PARAMETERS, true);
    }


    @Test
    public void should_update_report_option() throws Exception {
        reportOptionsUseCase.updateReportOption(REPORT_URI, OPTION_ID, REPORT_PARAMETERS);
        verify(mReportOptionRestApi).updateReportOption(REPORT_URI, OPTION_ID, REPORT_PARAMETERS);
    }

    @Test
    public void should_delete_report_option() throws Exception {
        reportOptionsUseCase.deleteReportOption(REPORT_URI, OPTION_LABEL);
        verify(mReportOptionRestApi).deleteReportOption(REPORT_URI, OPTION_ID);
    }

    @Test
    public void create_report_option_adapts_io_exception() throws Exception {
        when(mReportOptionRestApi.createReportOption(anyString(),
                anyString(), anyListOf(ReportParameter.class), anyBoolean())).thenThrow(mIOException);
        try {
            reportOptionsUseCase.createReportOption(REPORT_URI, OPTION_LABEL, REPORT_PARAMETERS, true);
            fail("Should adapt IO exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mIOException);
        }
    }

    @Test
    public void create_report_option_adapts_http_exception() throws Exception {
        when(mReportOptionRestApi.createReportOption(anyString(),
                anyString(), anyListOf(ReportParameter.class), anyBoolean())).thenThrow(mHttpException);

        try {
            reportOptionsUseCase.createReportOption(REPORT_URI, OPTION_LABEL, REPORT_PARAMETERS, true);
            fail("Should adapt HTTP exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mHttpException);
        }
    }



    @Test
    public void update_report_option_adapts_io_exception() throws Exception {
        doThrow(mIOException).when(mReportOptionRestApi).updateReportOption(anyString(),
                anyString(), anyListOf(ReportParameter.class));
        try {
            reportOptionsUseCase.updateReportOption(REPORT_URI, OPTION_LABEL, REPORT_PARAMETERS);
            fail("Should adapt IO exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mIOException);
        }
    }

    @Test
    public void update_report_option_adapts_http_exception() throws Exception {
        doThrow(mHttpException).when(mReportOptionRestApi).updateReportOption(anyString(),
                anyString(), anyListOf(ReportParameter.class));
        try {
            reportOptionsUseCase.updateReportOption(REPORT_URI, OPTION_LABEL, REPORT_PARAMETERS);
            fail("Should adapt HTTP exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mHttpException);
        }
    }

    @Test
    public void delete_report_option_adapts_io_exception() throws Exception {
        doThrow(mIOException).when(mReportOptionRestApi).deleteReportOption(anyString(), anyString());

        try {
            reportOptionsUseCase.deleteReportOption(REPORT_URI, OPTION_LABEL);
            fail("Should adapt IO exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mIOException);
        }
    }

    @Test
    public void delete_report_option_adapts_http_exception() throws Exception {
        doThrow(mHttpException).when(mReportOptionRestApi).deleteReportOption(anyString(), anyString());

        try {
            reportOptionsUseCase.deleteReportOption(REPORT_URI, OPTION_LABEL);
            fail("Should adapt HTTP exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mHttpException);
        }
    }

    private void setupMocks() {
        when(mExceptionMapper.transform(any(HttpException.class))).thenReturn(mServiceException);
        when(mExceptionMapper.transform(any(IOException.class))).thenReturn(mServiceException);
    }
}