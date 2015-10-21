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

package com.jaspersoft.android.sdk.client;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParametersList;
import com.jaspersoft.android.sdk.client.oxm.report.adapter.ReportParametersListDeserializer;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 1.10
 */
class GSONDataTypeConverterCreator implements DataTypeConverterCreator<GsonHttpMessageConverter> {
    @NonNull
    @Override
    public GsonHttpMessageConverter create() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ReportParametersList.class, new ReportParametersListDeserializer());
        Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

        GsonHttpMessageConverter converter = new GsonHttpMessageConverter(gson);
        List<MediaType> supportedMediaTypes = new ArrayList<>(3);
        supportedMediaTypes.add(new MediaType("application", "json", GsonHttpMessageConverter.DEFAULT_CHARSET));
        supportedMediaTypes.add(new MediaType("application", "*+json", GsonHttpMessageConverter.DEFAULT_CHARSET));
        converter.setSupportedMediaTypes(supportedMediaTypes);

        return converter;
    }
}
