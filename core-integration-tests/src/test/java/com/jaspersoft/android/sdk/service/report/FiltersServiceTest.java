package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.ReportTestBundle;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
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
public class FiltersServiceTest {
    @ClassRule
    public static JrsEnvironmentRule sEnv = new JrsEnvironmentRule();

    @Test
    @Parameters(method = "reports")
    public void filters_service_should_support_crud_for_report_option(ReportTestBundle bundle) throws Exception {
        if (bundle.hasParams()) {
            String reportUri = bundle.getReportUri();
            FiltersService filtersService = FiltersService.newService(bundle.getClient());

            String reportOptionLabel = "My Label";
            List<ReportParameter> params = bundle.getParams();
            ReportOption reportOption =
                    filtersService.createReportOption(reportUri, reportOptionLabel, params, true);

            Set<ReportOption> reportOptions = filtersService.listReportOptions(reportUri);
            assertThat(reportOptions, hasItem(reportOption));

            filtersService.updateReportOption(reportUri, reportOption.getId(), params);

            filtersService.deleteReportOption(reportUri, reportOption.getId());

            reportOptions = filtersService.listReportOptions(reportUri);
            assertThat(reportOptions, not(hasItem(reportOption)));
        }
    }

    @Test
    @Parameters(method = "reports")
    public void filters_service_should_list_input_controls(ReportTestBundle bundle) throws Exception {
        FiltersService filtersService = FiltersService.newService(bundle.getClient());
        filtersService.listControls(bundle.getReportUri());
    }

    @Test
    @Parameters(method = "reports")
    public void filters_service_should_list_input_cascades(ReportTestBundle bundle) throws Exception {
        if (bundle.hasParams()) {
            FiltersService filtersService = FiltersService.newService(bundle.getClient());
            List<ReportParameter> params = bundle.getParams();
            filtersService.listControlsValues(bundle.getReportUri(),
                    Collections.singletonList(params.get(0)), false);
        }
    }
    
    private Object[] reports() {
        return sEnv.listReports();
    }
}
