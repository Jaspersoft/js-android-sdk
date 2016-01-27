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

package com.jaspersoft.android.sdk.network.entity.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardComponentCollection;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.test.resource.ResourceFile;
import com.jaspersoft.android.sdk.test.resource.TestResource;
import com.jaspersoft.android.sdk.test.resource.inject.TestResourceInjector;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GsonBuilder.class, GsonFactory.class})
public class GsonFactoryTest {

    @ResourceFile("json/dashboard_components.json")
    TestResource mComponents;

    @Before
    public void setUp() throws Exception {
        TestResourceInjector.inject(this);
    }

    @Test
    public void shouldEnableGsonExposeAnnotationField() throws Exception {
        GsonBuilder gsonBuilder = PowerMockito.mock(GsonBuilder.class);
        whenNew(GsonBuilder.class).withNoArguments().thenReturn(gsonBuilder);
        GsonFactory.create();
        verify(gsonBuilder, times(1)).excludeFieldsWithoutExposeAnnotation();
    }

    @Test
    public void shouldDisableHtmlEscaping() throws Exception {
        GsonBuilder gsonBuilder = PowerMockito.mock(GsonBuilder.class);
        whenNew(GsonBuilder.class).withNoArguments().thenReturn(gsonBuilder);
        GsonFactory.create();
        verify(gsonBuilder, times(1)).disableHtmlEscaping();
    }

    @Test
    public void shouldCreateInstanceOfGson() {
        Gson gson = GsonFactory.create();
        assertThat(gson, Is.is(notNullValue()));
    }

    @Test
    public void shouldModifyReportExecutionParametersField() throws Exception {
        ReportExecutionRequestOptions options = ReportExecutionRequestOptions.newRequest("/my/uri");
        options.withParameters(Collections.singletonList(new ReportParameter("key", Collections.singleton("value"))));
        Gson gson = GsonFactory.create();
        assertThat(gson.toJson(options), is("{\"reportUnitUri\":\"/my/uri\",\"parameters\":{\"reportParameter\":[{\"name\":\"key\",\"value\":[\"value\"]}]}}"));
    }

    @Test
    public void should_parse_only_input_controls_from_dashboard_components() throws Exception {
        Gson gson = GsonFactory.create();
        DashboardComponentCollection components = gson.fromJson(mComponents.asString(), DashboardComponentCollection.class);
        assertThat(components.getInputControlComponents() , is(not(empty())));
    }
}
