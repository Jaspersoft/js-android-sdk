package com.jaspersoft.android.sdk.network.rest.v2.entity.server;
/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
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

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.rest.v2.entity.type.GsonFactory;
import com.jaspersoft.android.sdk.test.resource.ResourceFile;
import com.jaspersoft.android.sdk.test.resource.TestResource;
import com.jaspersoft.android.sdk.test.resource.inject.TestResourceInjector;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ServerInfoResponseJsonConvertTest {
    @ResourceFile("json/default_server_info.json")
    TestResource mJsonResource;

    Gson mGson = GsonFactory.create();

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
    }

    @Test
    public void shouldDeserializeDefaultJsonResponse() {
        ServerInfoResponse response = deserialize(mJsonResource.asString());
        assertThat(response, is(notNullValue()));
    }
    
    @Test
    public void shouldDeserializeDateFormatPattern() {
        ServerInfoResponse response = deserialize("{\"dateFormatPattern\": \"yyyy-MM-dd\"}");
        assertThat(response.getDateFormatPattern(), is("yyyy-MM-dd"));
    }
    
    @Test
    public void shouldDeserializeDateTimeFormatPattern() {
        ServerInfoResponse response = deserialize("{\"datetimeFormatPattern\": \"yyyy-MM-dd'T'HH:mm:ss\"}");
        assertThat(response.getDatetimeFormatPattern(), is("yyyy-MM-dd'T'HH:mm:ss"));
    }
    
    @Test
    public void shouldDeserializeVersion() {
        ServerInfoResponse response = deserialize("{\"version\": \"6.1\"}");
        assertThat(response.getVersion(), is("6.1"));
    }
    
    @Test
    public void shouldDeserializeEdition() {
        ServerInfoResponse response = deserialize("{\"edition\": \"PRO\"}");
        assertThat(response.getEdition(), is("PRO"));
    }
    
    @Test
    public void shouldDeserializeEditionName() {
        ServerInfoResponse response = deserialize("{\"editionName\": \"Enterprise for AWS\"}");
        assertThat(response.getEditionName(), is("Enterprise for AWS"));
    }
    
    @Test
    public void shouldDeserializeLicenseType() {
        ServerInfoResponse response = deserialize("{\"licenseType\": \"Commercial\"}");
        assertThat(response.getLicenseType(), is("Commercial"));
    }
    
    @Test
    public void shouldDeserializeBuild() {
        ServerInfoResponse response = deserialize("{\"build\": \"20150527_1447\"}");
        assertThat(response.getBuild(), is("20150527_1447"));
    }
    
    @Test
    public void shouldDeserializeFeatures() {
        ServerInfoResponse response = deserialize("{\"features\": \"Fusion AHD EXP DB AUD ANA MT \"}");
        assertThat(response.getFeatures(), is("Fusion AHD EXP DB AUD ANA MT "));
    }
    
    private ServerInfoResponse deserialize(String json) {
        return mGson.fromJson(json, ServerInfoResponse.class);
    }
}
