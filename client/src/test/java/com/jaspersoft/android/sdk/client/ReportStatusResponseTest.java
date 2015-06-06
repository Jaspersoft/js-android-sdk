package com.jaspersoft.android.sdk.client;

import com.jaspersoft.android.sdk.client.oxm.report.ReportStatus;
import com.jaspersoft.android.sdk.client.oxm.report.ReportStatusResponse;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class ReportStatusResponseTest {
    private ReportStatusResponse reportStatusResponse;

    @Before
    public void setUp() {
        reportStatusResponse = new ReportStatusResponse();
    }

    @Test
    public void test_getReportStatus_for_queued() {
        reportStatusResponse.setStatus("queued");
        assertThat(reportStatusResponse.getReportStatus(), is(ReportStatus.queued));
    }

    @Test
    public void test_getReportStatus_for_ready() {
        reportStatusResponse.setStatus("ready");
        assertThat(reportStatusResponse.getReportStatus(), is(ReportStatus.ready));
    }

    @Test
    public void test_getReportStatus_for_failed() {
        reportStatusResponse.setStatus("failed");
        assertThat(reportStatusResponse.getReportStatus(), is(ReportStatus.failed));
    }

    @Test
    public void test_getReportStatus_for_execution() {
        reportStatusResponse.setStatus("execution");
        assertThat(reportStatusResponse.getReportStatus(), is(ReportStatus.execution));
    }

    @Test
    public void test_getReportStatus_for_cancelled() {
        reportStatusResponse.setStatus("cancelled");
        assertThat(reportStatusResponse.getReportStatus(), is(ReportStatus.cancelled));
    }

}
