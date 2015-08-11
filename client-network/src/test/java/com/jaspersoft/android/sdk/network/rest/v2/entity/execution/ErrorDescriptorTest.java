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

import com.jaspersoft.android.sdk.network.rest.v2.entity.type.GsonFactory;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ErrorDescriptorTest {

    @Test
    public void shouldDeserializeErrorCode() {
        ErrorDescriptor errorDescriptor = deserialize("{\"errorCode\": \"Input controls validation failure\"}");
        String result = errorDescriptor.getErrorCode();
        assertThat(result, is("Input controls validation failure"));
    }

    @Test
    public void shouldDeserializeMessage() {
        ErrorDescriptor errorDescriptor = deserialize("{\"message\": \"input.controls.validation.error\"}");
        String result = errorDescriptor.getMessage();
        assertThat(result, is("input.controls.validation.error"));
    }

    @Test
    public void shouldDeserializeParameters() {
        ErrorDescriptor errorDescriptor = deserialize("{\"parameters\": [\"Specify a valid value for type Integer.\"]}");
        Set<String> result = errorDescriptor.getParameters();
        assertThat(result, is(notNullValue()));
        assertThat(new ArrayList<String>(result).get(0), is("Specify a valid value for type Integer."));
    }

    private ErrorDescriptor deserialize(String json) {
        return GsonFactory.create().fromJson(json, ErrorDescriptor.class);
    }
}
