package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.ReportTestBundle;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    public void shouldRunReport(ReportTestBundle bundle) throws Exception {
        ReportService reportService = ReportService.newService(bundle.getClient());

        ReportExecutionOptions executionOptions = ReportExecutionOptions.builder()
                .withFormat(ReportFormat.HTML)
                .withFreshData(true)
                .withInteractive(true)
                .build();
        reportService.run(bundle.getReportUri(), executionOptions);
    }

    private Object[] reports() {
        return sEnv.listReports();
    }
}
