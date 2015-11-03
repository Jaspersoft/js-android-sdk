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

package com.jaspersoft.android.sdk.network.entity.control;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.jaspersoft.android.sdk.network.entity.type.GsonFactory;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;

import static com.jaspersoft.android.sdk.test.matcher.HasAnnotation.hasAnnotation;
import static com.jaspersoft.android.sdk.test.matcher.HasSerializedName.hasSerializedName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class InputControlCollectionTest {

    Gson mGson = GsonFactory.create();

    @Test
    public void shouldHaveExposeAnnotationForFieldControls() throws NoSuchFieldException {
        Field field = InputControlCollection.class.getDeclaredField("mValues");
        assertThat(field, hasAnnotation(Expose.class));
    }

    @Test
    public void valuesFieldShouldHaveSerializedNameAnnotationForFieldControls() throws NoSuchFieldException {
        Field field = InputControlCollection.class.getDeclaredField("mValues");
        assertThat(field, hasSerializedName("inputControl"));
    }

    @Test
    public void shouldProperlyAdaptMasterDependencies() {
        String json = "{\"inputControl\" : [ {\"masterDependencies\":[\"Country_multi_select\",\"Cascading_state_multi_select\"]}]}";
        InputControl inputControl = deserializeInputControl(json);

        Set<String> deps = inputControl.getMasterDependencies();

        assertThat(deps, hasItem("Cascading_state_multi_select"));
    }

    @Test
    public void shouldProperlyAdaptSlaveDependencies() {
        String json = "{\"inputControl\" : [ {\"slaveDependencies\":[\"Country_multi_select\",\"Cascading_state_multi_select\"]}]}";
        InputControl inputControl = deserializeInputControl(json);

        Set<String> deps = inputControl.getSlaveDependencies();

        assertThat(deps, hasItem("Cascading_state_multi_select"));
    }

    @Test
    public void shouldProperlyAdaptDateTimeValidationRule() {
        String json = "{\"inputControl\" : [ {\"validationRules\" : [ { \"dateTimeFormatValidationRule\" : { \"errorMessage\" : \"This field is mandatory so you must enter data.\", \"format\": \"YYYY-mm-dd\" } }]} ]}";
        InputControl inputControl = deserializeInputControl(json);

        ValidationRule rule = (ValidationRule) inputControl.getValidationRules().toArray()[0];

        assertThat(rule.getErrorMessage(), is("This field is mandatory so you must enter data."));
        assertThat(rule.getType(), is("dateTimeFormatValidationRule"));
        assertThat(rule.getValue(), is("YYYY-mm-dd"));
    }

    @Test
    public void shouldProperlyAdaptMandatoryValidationRule() {
        String json = "{\"inputControl\" : [ {\"validationRules\" : [ { \"mandatoryValidationRule\" : { \"errorMessage\" : \"This field is mandatory so you must enter data.\" } }]} ]}";
        InputControl inputControl = deserializeInputControl(json);

        ValidationRule rule = (ValidationRule) inputControl.getValidationRules().toArray()[0];

        assertThat(rule.getErrorMessage(), is("This field is mandatory so you must enter data."));
        assertThat(rule.getType(), is("mandatoryValidationRule"));
    }
    private InputControl deserializeInputControl(String json) {
        InputControlCollection response = mGson.fromJson(json, InputControlCollection.class);
        return new ArrayList<>(response.get()).get(0);
    }
}