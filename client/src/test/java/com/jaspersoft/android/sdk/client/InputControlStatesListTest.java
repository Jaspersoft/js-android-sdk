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

package com.jaspersoft.android.sdk.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaspersoft.android.sdk.client.oxm.control.InputControl;
import com.jaspersoft.android.sdk.client.oxm.control.InputControlStatesList;
import com.jaspersoft.android.sdk.client.oxm.control.InputControlsList;
import com.jaspersoft.android.sdk.client.oxm.control.validation.DateTimeFormatValidationRule;
import com.jaspersoft.android.sdk.client.oxm.control.validation.MandatoryValidationRule;
import com.jaspersoft.android.sdk.client.util.TestResource;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Tom Koptel
 * @since 1.10
 */
public class InputControlStatesListTest {
    @Test
    public void shouldDeserializeControlStateList() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

        String json = TestResource.getJson().rawData("input_control_states_list");
        InputControlStatesList controlsList = gson.fromJson(json, InputControlStatesList.class);
        assertThat(controlsList.getInputControlStates(), is(not(empty())));
        assertThat(controlsList.getInputControlStates().get(0).getOptions(), is(not(empty())));
    }

    @Test
    public void shouldAssignValidClassForSpecificValidationRule() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

        String json = TestResource.getJson().rawData("input_controls_date");
        InputControlsList controlsList = gson.fromJson(json, InputControlsList.class);
        InputControl inputControl = controlsList.getInputControls().get(0);
        Assert.assertThat(inputControl.getValidationRules(DateTimeFormatValidationRule.class), is(not(empty())));
        Assert.assertThat(inputControl.getValidationRules(MandatoryValidationRule.class), is(not(empty())));
    }
}
