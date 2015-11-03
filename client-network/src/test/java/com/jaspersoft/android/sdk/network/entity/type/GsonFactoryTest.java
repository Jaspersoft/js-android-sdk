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

package com.jaspersoft.android.sdk.network.entity.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.core.Is.is;
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
        assertThat(gson, is(notNullValue()));
    }
}
