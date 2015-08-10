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

import com.jaspersoft.android.sdk.network.rest.v2.entity.type.GsonFactory;
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
public class DashboardLookupResponseTest {
    @com.jaspersoft.android.sdk.test.resource.ResourceFile("json/dashboard_unit_resource.json")
    TestResource dashboardResponse1;

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
    }

    @Test
    public void shouldDeserializeResponse1FromWholeJson() {
        DashboardLookupResponse response1 = deserialize(dashboardResponse1.asString());
        assertThat(response1, is(notNullValue()));
    }

    @Test
    public void shouldDeserializeVersion() {
        DashboardLookupResponse response = deserialize("{\"version\": 2}");
        assertThat(response.getVersion(), is(2));
    }

    @Test
    public void shouldDeserializePermissionMask() {
        DashboardLookupResponse response = deserialize("{\"permissionMask\": 1}");
        assertThat(response.getPermissionMask(), is(1));
    }

    @Test
    public void shouldDeserializeCreationDate() {
        DashboardLookupResponse response = deserialize("{\"creationDate\": \"2015-06-05T07:21:11\"}");
        assertThat(response.getCreationDate(), is("2015-06-05T07:21:11"));
    }

    @Test
    public void shouldDeserializeUpdateDate() {
        DashboardLookupResponse response = deserialize("{\"updateDate\": \"2014-05-14T17:38:49\"}");
        assertThat(response.getUpdateDate(), is("2014-05-14T17:38:49"));
    }

    @Test
    public void shouldDeserializeLabel() {
        DashboardLookupResponse response = deserialize("{\"label\": \"01. Geographic Results by Segment\"}");
        assertThat(response.getLabel(), is("01. Geographic Results by Segment"));
    }

    @Test
    public void shouldDeserializeDescription() {
        DashboardLookupResponse response = deserialize("{\"description\": \"Sample HTML5 multi-axis column chart\"}");
        assertThat(response.getDescription(), is("Sample HTML5 multi-axis column chart"));
    }

    @Test
    public void shouldDeserializeUri() {
        DashboardLookupResponse response = deserialize("{\"uri\": \"/public/Samples/Ad_Hoc_Views/01__Geographic_Results_by_Segment\"}");
        assertThat(response.getUri(), is("/public/Samples/Ad_Hoc_Views/01__Geographic_Results_by_Segment"));
    }

    @Test
    public void shouldDeserializeFoundations() {
        DashboardLookupResponse response = deserialize("{\"foundations\": [{\"id\": \"default\",\"layout\": \"layout\",\"wiring\": \"wiring\",\"components\": \"components\"}]}");
        DashboardFoundation foundation = response.getFoundations().get(0);
        assertThat(foundation.getId(), is("default"));
        assertThat(foundation.getLayout(), is("layout"));
        assertThat(foundation.getWiring(), is("wiring"));
        assertThat(foundation.getComponents(), is("components"));
    }

    @Test
    public void shouldDeserializeDefaultFoundation() {
        DashboardLookupResponse response = deserialize("{\"defaultFoundation\": \"default\"}");
        assertThat(response.getDefaultFoundation(), is("default"));
    }

    @Test
    public void shouldDeserializeResources() {
        DashboardLookupResponse response = deserialize("{\"resources\": [{\"name\": \"wiring\",\"type\": \"wiring\",\"resource\": {\"resourceReference\": {\"uri\": \"/public/Samples/Dashboards/1._Supermart_Dashboard_files/wiring\"}}}]}");
        DashboardResource resource = response.getResources().get(0);
        DashboardResourceInfo info = resource.getResource();
        assertThat(resource.getName(), is("wiring"));
        assertThat(resource.getType(), is("wiring"));
        assertThat(info.getResourceReference().getUri(), is("/public/Samples/Dashboards/1._Supermart_Dashboard_files/wiring"));
    }

    private DashboardLookupResponse deserialize(String json) {
        return GsonFactory.create().fromJson(json, DashboardLookupResponse.class);
    }
}
