package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.env.ResourceTestBundle;
import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.ReportTestBundle;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.report.option.ReportOption;
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
            String reportUri = bundle.getUri();
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
    public void filters_service_should_list_report_input_controls(ReportTestBundle bundle) throws Exception {
        FiltersService filtersService = FiltersService.newService(bundle.getClient());
        filtersService.listReportControls(bundle.getUri());
    }

    @Test
    @Parameters(method = "reports")
    public void filters_service_should_list_input_controls_states(ReportTestBundle bundle) throws Exception {
        if (bundle.hasParams()) {
            List<ReportParameter> params = bundle.getParams();
            FiltersService filtersService = FiltersService.newService(bundle.getClient());
            filtersService.listControlsStates(bundle.getUri(), Collections.singletonList(params.get(0)), true);
        }
    }

    @Test
    @Parameters(method = "reports")
    public void filters_service_should_validate_input_controls(ReportTestBundle bundle) throws Exception {
        if (bundle.hasParams()) {
            List<ReportParameter> params = bundle.getParams();
            FiltersService filtersService = FiltersService.newService(bundle.getClient());
            filtersService.validateControls(bundle.getUri(), Collections.singletonList(params.get(0)), true);
        }
    }

    @Test
    @Parameters(method = "reports")
    public void filters_service_should_list_resource_values(ReportTestBundle bundle) throws Exception {
        if (bundle.hasParams()) {
            FiltersService filtersService = FiltersService.newService(bundle.getClient());
            filtersService.listResourceStates(bundle.getUri(), false);
        }
    }

    @Test
    @Parameters(method = "dashboards")
    public void filters_service_should_list_dashboard_input_controls(ResourceTestBundle bundle) throws Exception {
        if (bundle.getVersion().greaterThanOrEquals(ServerVersion.v6)) {
            FiltersService filtersService = FiltersService.newService(bundle.getClient());
            filtersService.listDashboardControls(bundle.getUri());
        }
    }
    @Test
    @Parameters(method = "dashboards")
    public void filters_service_should_list_dashboard_input_control_components(ResourceTestBundle bundle) throws Exception {
        if (bundle.getVersion().greaterThanOrEquals(ServerVersion.v6)) {
            FiltersService filtersService = FiltersService.newService(bundle.getClient());
            filtersService.listDashboardControlComponents(bundle.getUri());
        }
    }

    private Object[] reports() {
        return sEnv.listReports();
    }

    private Object[] dashboards() {
        return sEnv.listDashboards();
    }
}
