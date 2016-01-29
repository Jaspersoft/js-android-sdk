package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.env.DashboardTestBundle;
import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.ReportTestBundle;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOptionEntity;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

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
            ReportOptionEntity reportOption =
                    filtersService.createReportOption(reportUri, reportOptionLabel, params, true);

            Set<ReportOptionEntity> reportOptions = filtersService.listReportOptions(reportUri);
            assertThat(reportOptions, hasItem(reportOption));

            filtersService.updateReportOption(reportUri, reportOption.getId(), params);

            filtersService.deleteReportOption(reportUri, reportOption.getId());

            reportOptions = filtersService.listReportOptions(reportUri);
            assertThat(reportOptions, not(hasItem(reportOption)));
        }
    }

    @Test
    @Parameters(method = "reports")
    public void filters_service_should_list_report_input_controls(ReportTestBundle bundle) throws Exception {
        FiltersService filtersService = FiltersService.newService(bundle.getClient());
        filtersService.listReportControls(bundle.getReportUri());
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

    @Test
    @Parameters(method = "dashboards")
    public void filters_service_should_list_dashboard_input_controls(DashboardTestBundle bundle) throws Exception {
        if (bundle.getVersion().greaterThanOrEquals(ServerVersion.v6)) {
            FiltersService filtersService = FiltersService.newService(bundle.getClient());
            filtersService.listDashboardControls(bundle.getUri());
        }
    }

    private Object[] reports() {
        return sEnv.listReports();
    }

    private Object[] dashboards() {
        return sEnv.listDashboards();
    }
}
