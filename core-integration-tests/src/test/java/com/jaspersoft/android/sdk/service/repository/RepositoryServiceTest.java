/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.ReportTestBundle;
import com.jaspersoft.android.sdk.env.ResourceTestBundle;
import com.jaspersoft.android.sdk.env.ServerTestBundle;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
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
 * @since 2.3
 */
@RunWith(JUnitParamsRunner.class)
public class RepositoryServiceTest {
    @ClassRule
    public static JrsEnvironmentRule sEnv = new JrsEnvironmentRule();

    @Test
    @Parameters(method = "clients")
    public void repo_service_should_perform_search(ServerTestBundle bundle) throws Exception {
        RepositoryService service = RepositoryService.newService(bundle.getClient());
        RepositorySearchCriteria repositorySearchCriteria = RepositorySearchCriteria.builder()
                .withLimit(5)
                .withResourceMask(RepositorySearchCriteria.REPORT)
                .build();

        RepositorySearchTask repositorySearchTask = service.search(repositorySearchCriteria);
        assertThat(repositorySearchTask.nextLookup(), is(notNullValue()));
        assertThat(repositorySearchTask.nextLookup(), is(notNullValue()));
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
        Resource resource = service.fetchResourceDetails(bundle.getUri(), ResourceType.reportUnit);
        assertThat(resource, is(notNullValue()));
    }

    @Test
    @Parameters(method = "files")
    public void repo_service_should_give_file_details(ResourceTestBundle bundle) throws Exception {
        RepositoryService service = RepositoryService.newService(bundle.getClient());
        Resource resource = service.fetchResourceDetails(bundle.getUri(), ResourceType.file);
        assertThat(resource, is(notNullValue()));
    }

    @Test
    @Parameters(method = "files")
    public void repo_service_should_give_file_contents(ResourceTestBundle bundle) throws Exception {
        RepositoryService service = RepositoryService.newService(bundle.getClient());
        ResourceOutput output = service.fetchResourceContent(bundle.getUri());
        assertThat(output, is(notNullValue()));
    }

    private Object[] clients() {
        return sEnv.listAuthorizedClients();
    }

    private Object[] reports() {
        return sEnv.listReports();
    }

    private Object[] files() {
        return sEnv.listFiles();
    }
}
