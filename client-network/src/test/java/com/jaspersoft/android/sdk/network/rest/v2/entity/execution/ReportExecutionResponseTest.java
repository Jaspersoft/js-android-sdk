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

package com.jaspersoft.android.sdk.network.rest.v2.entity.execution;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.rest.v2.entity.type.GsonFactory;
import com.jaspersoft.android.sdk.test.resource.ResourceFile;
import com.jaspersoft.android.sdk.test.resource.TestResource;
import com.jaspersoft.android.sdk.test.resource.inject.TestResourceInjector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class ReportExecutionResponseTest {

    private final Gson mGson = GsonFactory.create();

    @ResourceFile("json/report_execution_details.json")
    TestResource compoundDetailsResponse;

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
    }

    @Test
    public void shouldDeserializeCompoundJsonResponse() {
        ReportExecutionResponse response = deserialize(compoundDetailsResponse.asString());
        assertThat(response, is(notNullValue()));
    }

    @Test
    @Parameters({
            "getReportURI, reportURI, /some/uri",
            "getExecutionId, requestId, 1234-5678-9101",
            "getStatus, status, execute",
    })
    public void shouldDeserializeStringField(String methodName, String key, String value) throws Exception {
        ReportExecutionResponse response = deserialize("{\"" + key + "\": \"" + value + "\"}");
        Method method = response.getClass().getMethod(methodName);
        String result = (String) method.invoke(response);
        assertThat(result, is(value));
    }

    @Test
    @Parameters({
            "getCurrentPage, currentPage, 1",
            "getTotalPages, totalPages, 100",
    })
    public void shouldDeserializeIntField(String methodName, String key, String value) throws Exception {
        ReportExecutionResponse response = deserialize("{\"" + key + "\": \"" + value + "\"}");
        Method method = response.getClass().getMethod(methodName);
        Integer result = (Integer) method.invoke(response);
        assertThat(result, is(Integer.parseInt(value)));
    }

    @Test
    public void shouldDeserializeExports() {
        ReportExecutionResponse response = deserialize("{\"exports\": []}");
        assertThat(response.getExports(), is(notNullValue()));
        assertThat(response.getExports(), is(empty()));
    }

    @Test
    public void shouldDeserializeErrorDescriptor() {
        ReportExecutionResponse response = deserialize("{\"errorDescriptor\": {}}");
        assertThat(response.getErrorDescriptor(), is(notNullValue()));
    }

    private ReportExecutionResponse deserialize(String json) {
        return mGson.fromJson(json, ReportExecutionResponse.class);
    }
}
