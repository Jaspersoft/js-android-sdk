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

package com.jaspersoft.android.sdk.test.integration.api;

import com.jaspersoft.android.sdk.network.ReportOptionRestApi;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
import com.jaspersoft.android.sdk.test.TestLogger;
import com.jaspersoft.android.sdk.test.integration.api.utils.DummyTokenProvider;
import com.jaspersoft.android.sdk.test.integration.api.utils.JrsMetadata;
import org.junit.Before;
import org.junit.Ignore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportOptionRestApiTest {

    private final JrsMetadata mMetadata = JrsMetadata.createMobileDemo();
    private final DummyTokenProvider mAuthenticator = DummyTokenProvider.create(mMetadata);
    private ReportOptionRestApi apiUnderTest;

    private static final String REPORT_URI = "/public/Samples/Reports/1._Geographic_Results_by_Segment_Report";
    public static final Map<String, Set<String>> CONTROL_PARAMETERS = new HashMap<>();

    static {
        Set<String> values = new HashSet<>();
        values.add("19");
        CONTROL_PARAMETERS.put("sales_fact_ALL__store_sales_2013_1", values);
    }

    @Before
    public void setup() {
        if (apiUnderTest == null) {
            apiUnderTest = new ReportOptionRestApi.Builder()
                    .logger(TestLogger.get(this))
                    .baseUrl(mMetadata.getServerUrl())
                    .build();
        }
    }

    // TODO: fix this by providing proper integration tests
    @Ignore
    public void shouldRequestReportOptionsList() throws Exception {
        Set<ReportOption> response = apiUnderTest.requestReportOptionsList(mAuthenticator.token(), REPORT_URI);
        assertThat(response, is(not(nullValue())));
    }

    // TODO: fix this by providing proper integration tests
    @Ignore
    public void apiSupportsCrudForReportOption() throws Exception {
        ReportOption response = apiUnderTest.createReportOption(mAuthenticator.token(), REPORT_URI, "label", CONTROL_PARAMETERS, true);
        assertThat(response.getLabel(), is("label"));

        apiUnderTest.updateReportOption(mAuthenticator.token(), REPORT_URI, response.getId(), CONTROL_PARAMETERS);

        apiUnderTest.deleteReportOption(mAuthenticator.token(), REPORT_URI, response.getId());
    }
}
