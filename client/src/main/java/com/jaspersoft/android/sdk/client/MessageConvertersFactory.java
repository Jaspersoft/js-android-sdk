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

import android.support.annotation.Nullable;

import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 1.10
 */
class MessageConvertersFactory {
    private final RestTemplate restTemplate;
    private final JsRestClient.DataType dataType;

    private MessageConvertersFactory(RestTemplate restTemplate, JsRestClient.DataType dataType) {
        this.restTemplate = restTemplate;
        this.dataType = dataType;
    }

    public static MessageConvertersFactory newInstance(RestTemplate restTemplate, JsRestClient.DataType dataType) {
        return new MessageConvertersFactory(restTemplate, dataType);
    }

    public List<HttpMessageConverter<?>> createMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new FormHttpMessageConverter());

        createDataTypeConverter(messageConverters);

        return messageConverters;
    }

    private void createDataTypeConverter(List<HttpMessageConverter<?>> messageConverters) {
        DataTypeConverterCreator dataTypeConverterCreator = resolveDataTypeConverter();
        if (dataTypeConverterCreator != null) {
            HttpMessageConverter<?> converter = dataTypeConverterCreator.create();
            messageConverters.add(converter);
        }
    }

    @Nullable
    private DataTypeConverterCreator resolveDataTypeConverter() {
        switch (dataType) {
            case XML:
                return new XMLDataTypeConverterCreator();
            case JSON:
                return new GSONDataTypeConverterCreator();
        }
        return null;
    }
}
