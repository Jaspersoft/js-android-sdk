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

package com.jaspersoft.android.sdk.network.rest.v2.entity.resource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaspersoft.android.sdk.network.rest.v2.entity.type.GsonFactory;
import com.jaspersoft.android.sdk.test.resource.ResourceFile;
import com.jaspersoft.android.sdk.test.resource.TestResource;
import com.jaspersoft.android.sdk.test.resource.inject.TestResourceInjector;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * {
 * "version": 2,
 * "permissionMask": 1,
 * "creationDate": "2015-06-05T07:21:11",
 * "updateDate": "2014-05-14T17:38:49",
 * "label": "01. Geographic Results by Segment",
 * "description": "Sample HTML5 multi-axis column chart from Domain showing Sales, Units, and $ Per Square Foot by Country and Store Type with various filters",
 * "uri": "/public/Samples/Ad_Hoc_Views/01__Geographic_Results_by_Segment",
 * "resourceType": "adhocDataView"
 * }
 *
 * @author Tom Koptel
 * @since 2.0
 */
public class ResourceLookupResponseJsonConvertTest {
    @ResourceFile("json/all_resources.json")
    TestResource mJsonResources;
    @ResourceFile("json/resource_lookup_item.json")
    TestResource mJsonResource;

    Gson mGson = GsonFactory.create();

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
    }

    @Test
    public void shouldDeserializeCollectionFromJson() {
        Type type = new TypeToken<Collection<ResourceLookupResponse>>() {
        }.getType();
        Collection<ResourceLookupResponse> resourceLookupResponses = mGson.fromJson(mJsonResources.asString(), type);
        assertThat(resourceLookupResponses.size(), is(not(0)));
    }

    @Test
    public void shouldDeserializeVersion() {
        ResourceLookupResponse response = deserialize("{\"version\": 2}");
        assertThat(response.getVersion(), is(2));
    }

    @Test
    public void shouldDeserializePermissionMask() {
        ResourceLookupResponse response = deserialize("{\"permissionMask\": 1}");
        assertThat(response.getPermissionMask(), is(1));
    }

    @Test
    public void shouldDeserializeCreationDate() {
        ResourceLookupResponse response = deserialize("{\"creationDate\": \"2015-06-05T07:21:11\"}");
        assertThat(response.getCreationDate(), is("2015-06-05T07:21:11"));
    }

    @Test
    public void shouldDeserializeUpdateDate() {
        ResourceLookupResponse response = deserialize("{\"updateDate\": \"2014-05-14T17:38:49\"}");
        assertThat(response.getUpdateDate(), is("2014-05-14T17:38:49"));
    }

    @Test
    public void shouldDeserializeLabel() {
        ResourceLookupResponse response = deserialize("{\"label\": \"01. Geographic Results by Segment\"}");
        assertThat(response.getLabel(), is("01. Geographic Results by Segment"));
    }

    @Test
    public void shouldDeserializeDescription() {
        ResourceLookupResponse response = deserialize("{\"description\": \"Sample HTML5 multi-axis column chart\"}");
        assertThat(response.getDescription(), is("Sample HTML5 multi-axis column chart"));
    }

    @Test
    public void shouldDeserializeUri() {
        ResourceLookupResponse response = deserialize("{\"uri\": \"/public/Samples/Ad_Hoc_Views/01__Geographic_Results_by_Segment\"}");
        assertThat(response.getUri(), is("/public/Samples/Ad_Hoc_Views/01__Geographic_Results_by_Segment"));
    }

    @Test
    public void shouldDeserializeResourceType() {
        ResourceLookupResponse response = deserialize("{\"resourceType\": \"adhocDataView\"}");
        assertThat(response.getResourceType(), is("adhocDataView"));
    }

    private ResourceLookupResponse deserialize(String json) {
        return mGson.fromJson(json, ResourceLookupResponse.class);
    }
}
