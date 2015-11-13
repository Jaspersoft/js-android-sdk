/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.TestLogger;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
import com.jaspersoft.android.sdk.testkit.resource.ResourceServiceFactory;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.Set;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class IntegrationReportOptionRestApiTest {

    @ClassRule
    public static JrsEnvironmentRule sEnv = new JrsEnvironmentRule();

    @Test
    @Parameters(method = "reports")
    public void shouldRequestReportOptionsList(String token, String baseUrl, String reportUri) throws Exception {
        Set<ReportOption> response = createApi(baseUrl).requestReportOptionsList(token, reportUri);
        assertThat("Failed load report options data", response != null);
    }

    @Test
    @Parameters(method = "reports")
    public void apiSupportsCrudForReportOption(String token, String baseUrl, String reportUri) throws Exception {
        Map<String, Set<String>> params = ResourceServiceFactory.builder()
                .baseUrl(baseUrl).token(token)
                .create()
                .resourceParameter(reportUri)
                .listParams();
        if (params != null && !params.isEmpty()) {
            ReportOption response = createApi(baseUrl).createReportOption(token, reportUri, "label", params, true);
            assertThat(response.getLabel(), is("label"));

            createApi(baseUrl).updateReportOption(token, reportUri, response.getId(), params);
            createApi(baseUrl).deleteReportOption(token, reportUri, response.getId());
        }
    }

    private Object[] reports() {
        return sEnv.listReports();
    }

    private ReportOptionRestApi createApi(String baseUrl) {
        return new ReportOptionRestApi.Builder()
                .logger(TestLogger.get(this))
                .baseUrl(baseUrl)
                .build();
    }
}
