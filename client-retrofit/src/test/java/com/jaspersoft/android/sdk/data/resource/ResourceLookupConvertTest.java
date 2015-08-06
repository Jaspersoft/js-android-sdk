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

package com.jaspersoft.android.sdk.data.resource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaspersoft.android.sdk.data.type.GsonFactory;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Collection;

import sdk.test.resource.ResourceFile;
import sdk.test.resource.TestResource;
import sdk.test.resource.inject.TestResourceInjector;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ResourceLookupConvertTest {
    @ResourceFile("json/all_resources.json")
    TestResource mJsonResources;

    Gson mGson = GsonFactory.create();

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
    }

    @Test
    public void shouldDeserializeCollectionFromJson() {
        Type type = new TypeToken<Collection<ResourceLookup>>(){}.getType();
        Collection<ResourceLookup> resourceLookups = mGson.fromJson(mJsonResources.asString(), type);
        assertThat(resourceLookups.size(), is(not(0)));
    }

}
