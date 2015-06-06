package com.jaspersoft.android.sdk.client;

import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionResponse;
import com.jaspersoft.android.sdk.client.oxm.report.ReportStatus;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class ReportExecutionResponseTest {
    private ReportExecutionResponse reportExecutionResponse;

    @Before
    public void setUp() {
        reportExecutionResponse = new ReportExecutionResponse();
    }

    @Test
    public void test_getReportStatus_for_queued() {
        reportExecutionResponse.setStatus("queued");
        assertThat(reportExecutionResponse.getReportStatus(), is(ReportStatus.queued));
    }

    @Test
    public void test_getReportStatus_for_ready() {
        reportExecutionResponse.setStatus("ready");
        assertThat(reportExecutionResponse.getReportStatus(), is(ReportStatus.ready));
    }

    @Test
    public void test_getReportStatus_for_failed() {
        reportExecutionResponse.setStatus("failed");
        assertThat(reportExecutionResponse.getReportStatus(), is(ReportStatus.failed));
    }

    @Test
    public void test_getReportStatus_for_execution() {
        reportExecutionResponse.setStatus("execution");
        assertThat(reportExecutionResponse.getReportStatus(), is(ReportStatus.execution));
    }

    @Test
    public void test_getReportStatus_for_cancelled() {
        reportExecutionResponse.setStatus("cancelled");
        assertThat(reportExecutionResponse.getReportStatus(), is(ReportStatus.cancelled));
    }

}
