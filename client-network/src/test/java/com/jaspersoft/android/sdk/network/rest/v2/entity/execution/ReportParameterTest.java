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

package com.jaspersoft.android.sdk.network.rest.v2.entity.execution;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.rest.v2.entity.type.GsonFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@SuppressWarnings("unchecked")
public class ReportParameterTest {

    @Rule
    public ExpectedException mExpectedException = ExpectedException.none();
    private Gson mGson;

    @Before
    public void setup() {
        mGson = GsonFactory.create();
    }

    @Test
    public void factoryMethodShouldNotAllowEmptyName() {
        mExpectedException.expect(IllegalArgumentException.class);
        ReportParameter.create(null, Collections.EMPTY_SET);
    }

    @Test
    public void factoryMethodShouldNotAllowNullName() {
        mExpectedException.expect(IllegalArgumentException.class);
        ReportParameter.create("", Collections.EMPTY_SET);
    }

    @Test
    public void factoryMethodShouldNotAllowNullValueSet() {
        mExpectedException.expect(IllegalArgumentException.class);
        mExpectedException.expectMessage("Values should not be null. Otherwise use ReportParameter.createWithEmptyValue()");
        ReportParameter.create("key", null);
    }

    @Test
    public void factoryMethodCanCreateParameterWithEmptyValueSet() {
        ReportParameter reportParameter = ReportParameter.createWithEmptyValue("key");
        assertThat(reportParameter.getValues(), is(empty()));
    }

    @Test
    public void factoryMethod1ShouldAssignName() {
        ReportParameter reportParameter = ReportParameter.createWithEmptyValue("key");
        assertThat(reportParameter.getName(), is("key"));
    }

    @Test
    public void factoryMethod2ShouldAssignName() {
        ReportParameter reportParameter = ReportParameter.create("key", Collections.EMPTY_SET);
        assertThat(reportParameter.getName(), is("key"));
    }

    @Test
    public void shouldBeSerializableToJson() {
        ReportParameter reportParameter = ReportParameter.create("key", new HashSet<String>(){{
            add("value");
        }});
        String json = mGson.toJson(reportParameter);
        assertThat(json, is("{\"name\":\"key\",\"value\":[\"value\"]}"));
    }
}
