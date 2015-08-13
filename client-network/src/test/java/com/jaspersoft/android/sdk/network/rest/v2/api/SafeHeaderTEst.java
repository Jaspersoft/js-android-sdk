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

package com.jaspersoft.android.sdk.network.rest.v2.api;

import org.junit.Test;

import retrofit.client.Header;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class SafeHeaderTest {

    @Test
    public void forNullHeaderShouldAlwaysReturnSafeValueIfStringRequested() {
        SafeHeader safeHeader = new SafeHeader(null);
        assertThat(safeHeader.asString(), is(notNullValue()));
        assertThat(safeHeader.asString(), is(""));
    }

    @Test
    public void forNullHeaderShouldAlwaysReturnSafeHeaderIfIntRequested() {
        SafeHeader safeHeader = new SafeHeader(null);
        assertThat(safeHeader.asInt(), is(0));
    }

    @Test
    public void forNullHeaderShouldAlwaysReturnSafeHeaderIfBooleanRequested() {
        SafeHeader safeHeader = new SafeHeader(null);
        assertThat(safeHeader.asBoolean(), is(false));
    }

    @Test
    public void forNumberExceptionReturnsZero() {
        SafeHeader safeHeader = new SafeHeader(new Header("any", "NUN"));
        assertThat(safeHeader.asInt(), is(0));
    }

    @Test
    public void forBooleanParseExceptionReturnsFalse() {
        SafeHeader safeHeader = new SafeHeader(new Header("any", "NOT_A_BOOL"));
        assertThat(safeHeader.asBoolean(), is(false));
    }

    @Test
    public void forMissingValueInHeaderReturnsSafeObjectIfStringRequested() {
        SafeHeader safeHeader = new SafeHeader(new Header("any", null));
        assertThat(safeHeader.asString(), is(""));
    }

    @Test
    public void forMissingValueInHeaderReturnsSafeObjectIfIntRequested() {
        SafeHeader safeHeader = new SafeHeader(new Header("any", null));
        assertThat(safeHeader.asInt(), is(0));
    }

    @Test
    public void forMissingValueInHeaderReturnsSafeObjectIfBooleanRequested() {
        SafeHeader safeHeader = new SafeHeader(new Header("any", null));
        assertThat(safeHeader.asBoolean(), is(false));
    }

    @Test
    public void shouldReturnParseIntFromValidHeader() {
        SafeHeader safeHeader = new SafeHeader(new Header("any", "100"));
        assertThat(safeHeader.asInt(), is(100));
    }

    @Test
    public void shouldReturnParseStringFromValidHeader() {
        SafeHeader safeHeader = new SafeHeader(new Header("any", "value"));
        assertThat(safeHeader.asString(), is("value"));
    }

    @Test
    public void shouldReturnParseBooleanFromValidHeader() {
        SafeHeader safeHeader = new SafeHeader(new Header("any", "true"));
        assertThat(safeHeader.asBoolean(), is(true));
    }
}
