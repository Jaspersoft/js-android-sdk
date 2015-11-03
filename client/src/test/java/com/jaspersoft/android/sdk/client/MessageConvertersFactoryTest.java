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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 1.10
 */
@RunWith(JUnitParamsRunner.class)
public class MessageConvertersFactoryTest {

    private RestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate = new RestTemplate(false);
    }

    @Parameters({"XML", "JSON"})
    @Test
    public void shouldCreateCommonConvertersForAnyDataType(String type) {
        MessageConvertersFactory factory = MessageConvertersFactory.newInstance(restTemplate, JsRestClient.DataType.valueOf(type));
        List<HttpMessageConverter<?>> converters = factory.createMessageConverters();
        assertThat(converters, hasItem(isA(ByteArrayHttpMessageConverter.class)));
        assertThat(converters, hasItem(isA(StringHttpMessageConverter.class)));
        assertThat(converters, hasItem(isA(ResourceHttpMessageConverter.class)));
        assertThat(converters, hasItem(isA(FormHttpMessageConverter.class)));
    }

    @Test
    public void shouldCreateConvertersForXMLDataType() {
        MessageConvertersFactory factory = MessageConvertersFactory.newInstance(restTemplate, JsRestClient.DataType.XML);
        List<HttpMessageConverter<?>> converters = factory.createMessageConverters();
        assertThat(converters, hasItem(isA(SimpleXmlHttpMessageConverter.class)));
    }

    @Test
    public void shouldCreateConvertersForJSONDataType() {
        MessageConvertersFactory factory = MessageConvertersFactory.newInstance(restTemplate, JsRestClient.DataType.JSON);
        List<HttpMessageConverter<?>> converters = factory.createMessageConverters();
        assertThat(converters, hasItem(isA(GsonHttpMessageConverter.class)));
    }

}
