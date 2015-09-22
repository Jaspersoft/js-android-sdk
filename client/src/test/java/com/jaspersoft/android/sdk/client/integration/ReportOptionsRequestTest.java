/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.client.integration;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.async.request.CreateReportOptionsRequest;
import com.jaspersoft.android.sdk.client.async.request.ReportOptionsRequest;
import com.jaspersoft.android.sdk.client.async.request.UpdateReportOptionsRequest;
import com.jaspersoft.android.sdk.client.oxm.report.option.ReportOption;
import com.jaspersoft.android.sdk.client.oxm.report.option.ReportOptionResponse;
import com.jaspersoft.android.sdk.client.util.RealHttpRule;
import com.jaspersoft.android.sdk.client.util.TargetDataType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Tom Koptel
 * @since 1.11
 */
@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(manifest = Config.NONE)
@TargetDataType(values = {"XML", "JSON"})
public class ReportOptionsRequestTest extends ParametrizedTest {
    @Rule
    public RealHttpRule realHttpRule = new RealHttpRule();

    public static final Map<String, Set<String>> CONTROL_PARAMETERS = new HashMap<>();
    static {
        Set<String> values = new HashSet<>();
        values.add("19");
        CONTROL_PARAMETERS.put("sales_fact_ALL__store_sales_2013_1", values);
    }

    @ParameterizedRobolectricTestRunner.Parameters(name = "Data type = {2} Server version = {0} url = {1}")
    public static Collection<Object[]> data() {
        return ParametrizedTest.data(ReportOptionsRequestTest.class);
    }

    public ReportOptionsRequestTest(String versionCode, String url, String dataType) {
        super(versionCode, url, dataType);
    }

    @Test
    public void requestShouldListOptions() throws Exception {
        JsRestClient jsRestClient = getJsRestClient();
        String uri = getFactoryGirl().getResourceUri(jsRestClient);
        ReportOptionsRequest runReportExecutionRequest = new ReportOptionsRequest(jsRestClient, uri);

        ReportOptionResponse response = runReportExecutionRequest.loadDataFromNetwork();

        assertThat(response.getOptions(), is(not(empty())));
    }

    @Test
    public void shouldSupportCrudForReportOption() throws Exception {
        JsRestClient jsRestClient = getJsRestClient();
        String uri = getFactoryGirl().getResourceUri(jsRestClient);
        CreateReportOptionsRequest createReportOptionsRequest =
                new CreateReportOptionsRequest(jsRestClient, uri, "label", CONTROL_PARAMETERS);

        ReportOption response = createReportOptionsRequest.loadDataFromNetwork();
        assertThat(response, is(notNullValue()));

        UpdateReportOptionsRequest request = new UpdateReportOptionsRequest(jsRestClient, uri, response.getId(), CONTROL_PARAMETERS);
        request.loadDataFromNetwork();
    }
}
