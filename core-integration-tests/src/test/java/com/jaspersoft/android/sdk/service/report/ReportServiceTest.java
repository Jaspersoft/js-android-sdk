package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.ReportTestBundle;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

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

    @Test
    @Parameters(method = "reports")
    public void report_service_should_support_crud_for_report_option(ReportTestBundle bundle) throws Exception {
        if (bundle.hasParams()) {
            String reportUri = bundle.getReportUri();
            ReportService reportService = ReportService.newService(bundle.getClient());

            String reportOptionLabel = "My Label";
            List<ReportParameter> params = bundle.getParams();
            ReportOption reportOption =
                    reportService.createReportOption(reportUri, reportOptionLabel, params, true);

            Set<ReportOption> reportOptions = reportService.listReportOptions(reportUri);
            assertThat(reportOptions, hasItem(reportOption));

            reportService.updateReportOption(reportUri, reportOption.getId(), params);

            reportService.deleteReportOption(reportUri, reportOption.getId());

            reportOptions = reportService.listReportOptions(reportUri);
            assertThat(reportOptions, not(hasItem(reportOption)));
        }
    }

    @Test
    @Parameters(method = "reports")
    public void report_service_should_list_input_controls(ReportTestBundle bundle) throws Exception {
        ReportService reportService = ReportService.newService(bundle.getClient());
        reportService.listControls(bundle.getReportUri());
    }

    @Test
    @Parameters(method = "reports")
    public void report_service_should_list_input_cascades(ReportTestBundle bundle) throws Exception {
        if (bundle.hasParams()) {
            ReportService reportService = ReportService.newService(bundle.getClient());
            List<ReportParameter> params = bundle.getParams();
            reportService.listControlsValues(bundle.getReportUri(),
                    Collections.singletonList(params.get(0)));
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
