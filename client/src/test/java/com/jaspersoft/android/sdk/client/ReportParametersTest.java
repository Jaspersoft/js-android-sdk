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

package com.jaspersoft.android.sdk.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParameter;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParametersList;
import com.jaspersoft.android.sdk.client.oxm.report.adapter.ReportParametersListDeserializer;
import com.jaspersoft.android.sdk.client.oxm.report.adapter.ReportParametersListSerializer;
import com.jaspersoft.android.sdk.client.util.TestResource;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Tom Koptel
 * @since 1.10
 */
public class ReportParametersTest {
    @Test
    public void shouldDeserializeJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ReportParametersList.class, new ReportParametersListDeserializer());
        Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

        String json = TestResource.getJson().rawData("report_parameters");
        ReportParametersList reportParameter = gson.fromJson(json, ReportParametersList.class);
        assertThat(reportParameter.getReportParameters(), is(not(empty())));
    }

    @Test
    public void shouldSerializeJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ReportParametersList.class, new ReportParametersListSerializer());
        Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
        ReportParameter reportParameter = new ReportParameter();
        reportParameter.setName("key");
        reportParameter.setValue("value");

        ReportParametersList reportParametersList = new ReportParametersList();
        reportParametersList.getReportParameters().add(reportParameter);

        String json = gson.toJson(reportParametersList, ReportParametersList.class);
        assertThat(json, is("{\"key\":[\"value\"]}"));
    }
}
