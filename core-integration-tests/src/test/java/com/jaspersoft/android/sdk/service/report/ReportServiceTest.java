package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.ReportTestBundle;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class ReportServiceTest {
    @ClassRule
    public static JrsEnvironmentRule sEnv = new JrsEnvironmentRule();

    @Test
    @Parameters(method = "reports")
    public void report_service_should_export(ReportTestBundle bundle) throws Exception {
        ReportExecution execution = runReport(bundle);

        ReportExportOptions exportOptions = ReportExportOptions.builder()
                .withPageRange(PageRange.parse("1"))
                .withFormat(ReportFormat.HTML)
                .build();

        ReportExport export = execution.export(exportOptions);
        List<ReportAttachment> attachments = export.getAttachments();
        for (ReportAttachment reportAttachment : attachments) {
            reportAttachment.download();
        }
        export.download();
    }

    @Test
    @Parameters(method = "reports")
    public void report_service_should_await_complete_event(ReportTestBundle bundle) throws Exception {
        ReportExecution execution = runReport(bundle);
        execution.waitForReportCompletion();
    }

    @Test
    @Parameters(method = "reports")
    public void report_service_should_update_execution(ReportTestBundle bundle) throws Exception {
        if (bundle.hasParams()) {
            ReportExecution execution = runReport(bundle);
            execution.updateExecution(bundle.getParams());
        }
    }

    private ReportExecution runReport(ReportTestBundle bundle) throws ServiceException {
        ReportService reportService = ReportService.newService(bundle.getClient());

        ReportExecutionOptions executionOptions = ReportExecutionOptions.builder()
                .withFormat(ReportFormat.HTML)
                .withFreshData(true)
                .withInteractive(true)
                .build();
        return reportService.run(bundle.getReportUri(), executionOptions);
    }

    private Object[] reports() {
        return sEnv.listReports();
    }
}
