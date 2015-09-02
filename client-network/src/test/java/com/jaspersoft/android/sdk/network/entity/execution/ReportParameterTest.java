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

package com.jaspersoft.android.sdk.network.entity.execution;

import com.google.gson.annotations.Expose;

import org.hamcrest.MatcherAssert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.Collections;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static com.jaspersoft.android.sdk.test.matcher.HasAnnotation.hasAnnotation;
import static com.jaspersoft.android.sdk.test.matcher.HasSerializedName.hasSerializedName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@SuppressWarnings("unchecked")
@RunWith(JUnitParamsRunner.class)
public class ReportParameterTest {

    @Rule
    public ExpectedException mExpectedException = ExpectedException.none();

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
        mExpectedException.expectMessage("Values should not be null. Otherwise use ReportParameter.emptyParameter()");
        ReportParameter.create("key", null);
    }

    @Test
    public void factoryMethodCanCreateParameterWithEmptyValueSet() {
        ReportParameter reportParameter = ReportParameter.emptyParameter("key");
        assertThat(reportParameter.getValues(), is(empty()));
    }

    @Test
    public void factoryMethod1ShouldAssignName() {
        ReportParameter reportParameter = ReportParameter.emptyParameter("key");
        assertThat(reportParameter.getName(), is("key"));
    }

    @Test
    public void factoryMethod2ShouldAssignName() {
        ReportParameter reportParameter = ReportParameter.create("key", Collections.EMPTY_SET);
        assertThat(reportParameter.getName(), is("key"));
    }

    @Test
    @Parameters({
            "name",
            "values",
    })
    public void shouldHaveExposeAnnotationForField(String fieldName) throws NoSuchFieldException {
        Field field = ReportParameter.class.getDeclaredField(fieldName);
        MatcherAssert.assertThat(field, hasAnnotation(Expose.class));
    }

    @Test
    public void valuesFieldShouldHaveSerializedNameAnnotationForField() throws NoSuchFieldException {
        Field field = ReportParameter.class.getDeclaredField("values");
        assertThat(field, hasSerializedName("value"));
    }
}
