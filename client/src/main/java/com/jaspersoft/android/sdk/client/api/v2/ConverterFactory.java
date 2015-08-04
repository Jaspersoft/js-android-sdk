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

package com.jaspersoft.android.sdk.client.api.v2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParametersList;
import com.jaspersoft.android.sdk.client.oxm.report.adapter.ReportParametersListDeserializer;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.converter.SimpleXMLConverter;

/**
 * @author Tom Koptel
 * @since 2.2
 */
final class ConverterFactory {

    public static Converter create(DataType dataType) {
        if (dataType == DataType.JSON) {
            return createJsonConverter();
        }
        if (dataType == DataType.XML) {
            return createXmlConverter();
        }
        throw new UnsupportedOperationException("Following DataType[ " + dataType + "] has no converter resolution.");
    }

    private static Converter createXmlConverter() {
        Strategy annotationStrategy = new AnnotationStrategy();
        Serializer serializer = new Persister(annotationStrategy);
        return new SimpleXMLConverter(serializer);
    }

    private static Converter createJsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ReportParametersList.class, new ReportParametersListDeserializer());
        Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
        return new GsonConverter(gson);
    }

}
