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

package com.jaspersoft.android.sdk.client.retrofit.converter;

import com.jaspersoft.android.sdk.data.DataType;
import com.jaspersoft.android.sdk.data.GsonFactory;
import com.jaspersoft.android.sdk.data.XmlSerializerFactory;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.converter.SimpleXMLConverter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.times;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConverterFactory.class, GsonFactory.class, XmlSerializerFactory.class})
public class ConverterFactoryTest {
    @Rule
    public ExpectedException mException = ExpectedException.none();

    @Test
    public void shouldCreateGsonConverterForJsonDataType() {
        PowerMockito.mockStatic(GsonFactory.class);
        Converter  converter = ConverterFactory.create(DataType.JSON);

        PowerMockito.verifyStatic(times(1));

        assertThat(converter, is(instanceOf(GsonConverter.class)));
        assertThat(converter, is(notNullValue()));
    }

    @Test
    public void shouldCreateGsonConverterForXMLDataType() {
        PowerMockito.mockStatic(XmlSerializerFactory.class);
        Converter converter = ConverterFactory.create(DataType.XML);

        PowerMockito.verifyStatic(times(1));

        assertThat(converter, is(instanceOf(SimpleXMLConverter.class)));
        assertThat(converter, is(notNullValue()));
    }

    @Test
    public void shouldThrowExceptionIfDataTypeNull() {
        mException.expectMessage("DataType should not be null");
        mException.expect(IllegalArgumentException.class);
        ConverterFactory.create(null);
    }
}
