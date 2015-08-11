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
import com.jaspersoft.android.sdk.test.resource.ResourceFile;
import com.jaspersoft.android.sdk.test.resource.TestResource;
import com.jaspersoft.android.sdk.test.resource.inject.TestResourceInjector;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * 
 * @author Tom Koptel
 * @since 2.0
 */
public class FolderLookupResponseTest {

    @ResourceFile("json/root_folder.json")
    TestResource rootFolderResponse;

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
    }

    @Test
    public void shouldDeserializeResponse1FromWholeJson() {
        FolderLookupResponse response1 = deserialize(rootFolderResponse.asString());
        assertThat(response1, is(notNullValue()));
    }

    @Test
    public void shouldAlwaysReturnReportUnitUriAsType() {
        FolderLookupResponse response = deserialize("{}");
        assertThat(response.getResourceType(), is("folder"));
    }

    @Test
    public void shouldDeserializeVersion() {
        FolderLookupResponse response = deserialize("{\"version\": 0}");
        assertThat(response.getVersion(), is(0));
    }

    @Test
    public void shouldDeserializePermissionMask() {
        FolderLookupResponse response = deserialize("{\"permissionMask\": 2}");
        assertThat(response.getPermissionMask(), is(2));
    }

    @Test
    public void shouldDeserializeCreationDate() {
        FolderLookupResponse response = deserialize("{\"creationDate\": \"2015-06-05T07:21:11\"}");
        assertThat(response.getCreationDate(), is("2015-06-05T07:21:11"));
    }

    @Test
    public void shouldDeserializeUpdateDate() {
        FolderLookupResponse response = deserialize("{\"updateDate\": \"2014-05-14T17:38:49\"}");
        assertThat(response.getUpdateDate(), is("2014-05-14T17:38:49"));
    }

    @Test
    public void shouldDeserializeLabel() {
        FolderLookupResponse response = deserialize("{\"label\": \"Organization\"}");
        assertThat(response.getLabel(), is("Organization"));
    }

    @Test
    public void shouldDeserializeDescription() {
        FolderLookupResponse response = deserialize("{\"description\": \"Organization\"}");
        assertThat(response.getDescription(), is("Organization"));
    }

    @Test
    public void shouldDeserializeUri() {
        FolderLookupResponse response = deserialize("{\"uri\": \"/\"}");
        assertThat(response.getUri(), is("/"));
    }

    private FolderLookupResponse deserialize(String json) {
        return GsonFactory.create().fromJson(json, FolderLookupResponse.class);
    }

}
