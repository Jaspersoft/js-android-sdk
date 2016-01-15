package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.ReportTestBundle;
import com.jaspersoft.android.sdk.env.ServerTestBundle;
import com.jaspersoft.android.sdk.service.data.report.ReportResource;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class RepositoryServiceTest {
    @ClassRule
    public static JrsEnvironmentRule sEnv = new JrsEnvironmentRule();

    @Test
    @Parameters(method = "clients")
    public void repo_service_should_perform_search(ServerTestBundle bundle) throws Exception {
        RepositoryService service = RepositoryService.newService(bundle.getClient());
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .withLimit(5)
                .withResourceMask(SearchCriteria.REPORT)
                .build();

        SearchTask searchTask = service.search(searchCriteria);
        assertThat(searchTask.nextLookup(), is(notNullValue()));
        assertThat(searchTask.nextLookup(), is(notNullValue()));
    }

    @Test
    @Parameters(method = "clients")
    public void repo_service_should_list_root_folders(ServerTestBundle bundle) throws Exception {
        RepositoryService service = RepositoryService.newService(bundle.getClient());
        List<Resource> rootFolders = service.fetchRootFolders();
        assertThat(rootFolders, is(notNullValue()));
    }

    @Test
    @Parameters(method = "reports")
    public void repo_service_should_give_report_details(ReportTestBundle bundle) throws Exception {
        RepositoryService service = RepositoryService.newService(bundle.getClient());
        ReportResource reportResource = service.fetchReportDetails(bundle.getReportUri());
        assertThat(reportResource, is(notNullValue()));
    }

    private Object[] clients() {
        return sEnv.listAuthorizedClients();
    }

    private Object[] reports() {
        return sEnv.listReports();
    }
}
