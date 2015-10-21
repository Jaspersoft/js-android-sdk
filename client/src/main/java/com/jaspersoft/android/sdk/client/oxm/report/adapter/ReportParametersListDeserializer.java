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

package com.jaspersoft.android.sdk.client.oxm.report.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParameter;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParametersList;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 1.10
 */
public class ReportParametersListDeserializer implements JsonDeserializer<ReportParametersList> {
    private final Gson gson;
    private final Type mType;

    public ReportParametersListDeserializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
        mType = new TypeToken<Map<String, Set<String>>>() {
        }.getType();
    }

    @Override
    public ReportParametersList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Type type = new TypeToken<Map<String, Set<String>>>() {
        }.getType();

        Map<String, Set<String>> response = gson.fromJson(json, type);

        ReportParametersList reportParametersList = new ReportParametersList();
        ReportParameter reportParameter;
        for (Map.Entry<String, Set<String>> entry : response.entrySet()) {
            reportParameter = new ReportParameter();
            reportParameter.setName(entry.getKey());
            reportParameter.setValues(entry.getValue());
            reportParametersList.getReportParameters().add(reportParameter);
        }

        return reportParametersList;
    }
}
