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

import org.junit.Test;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Tom Koptel
 * @since 1.10
 */
public class JsRestClientBuilderTest {
    @Test
    public void shouldCreateClientInstanceWithRestTemplate() {
        JsRestClient jsRestClient = JsRestClient.builder().build();
        assertThat(jsRestClient.getRestTemplate(), is(notNullValue()));
        assertThat(jsRestClient.getRestTemplate(), instanceOf(RestTemplate.class));
    }

    @Test
    public void shouldCreateClientInstanceWithDataType() {
        JsRestClient jsRestClient = JsRestClient.builder().build();
        assertThat(jsRestClient.getDataType(), is(notNullValue()));
        assertThat(jsRestClient.getDataType(), instanceOf(JsRestClient.DataType.class));
    }

    @Test
    public void shouldCreateClientInstanceWithRequestFactory() {
        JsRestClient jsRestClient = JsRestClient.builder().build();
        assertThat(jsRestClient.getRequestFactory(), is(notNullValue()));
        assertThat(jsRestClient.getRequestFactory(), instanceOf(SimpleClientHttpRequestFactory.class));
    }
}
