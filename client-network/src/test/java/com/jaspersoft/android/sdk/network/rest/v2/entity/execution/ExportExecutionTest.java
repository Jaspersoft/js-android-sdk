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

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ExportExecutionTest {

    @Test
    public void shouldDeserializeId() {
        ExportExecution exportExecution = deserialize("{\"id\": \"1234-1234\"}");
        String result = exportExecution.getId();
        assertThat(result, is("1234-1234"));
    }

    @Test
    public void shouldDeserializeStatus() {
        ExportExecution exportExecution = deserialize("{\"status\": \"executed\"}");
        String result = exportExecution.getStatus();
        assertThat(result, is("executed"));
    }

    @Test
    public void shouldDeserializeOutputResource() {
        ExportExecution exportExecution = deserialize("{\"outputResource\": {}}");
        ReportOutputResource result = exportExecution.getOutputResource();
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void shouldDeserializeAttachments() {
        ExportExecution exportExecution = deserialize("{\"attachments\": []}");
        Set<ReportOutputResource> result = exportExecution.getAttachments();
        assertThat(result, is(notNullValue()));
        assertThat(result, is(empty()));
    }

    @Test
    public void shouldDeserializeErrorDescriptor() {
        ExportExecution exportExecution = deserialize("{\"errorDescriptor\": {}}");
        ErrorDescriptor result = exportExecution.getErrorDescriptor();
        assertThat(result, is(notNullValue()));
    }

    private ExportExecution deserialize(String json) {
        return GsonFactory.create().fromJson(json, ExportExecution.class);
    }
}
