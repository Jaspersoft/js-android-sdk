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

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
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

        resolveDataTypeConverter(messageConverters);

        return messageConverters;
    }

    private void resolveDataTypeConverter(List<HttpMessageConverter<?>> messageConverters) {
        switch (dataType) {
            case XML:
                addXMLConverter(messageConverters);
                break;
            case JSON:
                addJSONConverter(messageConverters);
                break;
        }
    }

    private void addXMLConverter(List<HttpMessageConverter<?>> messageConverters) {
        SimpleXmlHttpMessageConverter simpleXmlHttpMessageConverter
                = new SimpleXmlHttpMessageConverter();
        Strategy annotationStrategy = new AnnotationStrategy();
        Serializer serializer = new Persister(annotationStrategy);
        simpleXmlHttpMessageConverter.setSerializer(serializer);

        messageConverters.add(simpleXmlHttpMessageConverter);
    }

    private void addJSONConverter(List<HttpMessageConverter<?>> messageConverters) {
        messageConverters.add(new GsonHttpMessageConverter());
    }
}
