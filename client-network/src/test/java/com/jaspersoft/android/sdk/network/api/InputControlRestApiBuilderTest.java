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

package com.jaspersoft.android.sdk.network.api;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class InputControlRestApiBuilderTest {
    private InputControlRestApi.Builder builderUnderTest;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        builderUnderTest = new InputControlRestApi.Builder();
    }

    @Test
    public void builderShouldNotAllowNullBaseUrl() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("baseUrl == null");

        builderUnderTest.baseUrl(null);
    }

    @Test
    public void builderShouldNotAllowEmptyUrl() {
        expectedException.expect(IllegalArgumentException.class);
        builderUnderTest.baseUrl("");
    }

    @Test
    public void builderShouldAllowNullLogLevel() {
        builderUnderTest.logger(null);
    }

    @Test
    public void builderShouldEnsureBaseUrlNotNull() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Base url should be supplied to work with JRS API");

        builderUnderTest.tokenProvider(FakeTokenProvider.get());
        builderUnderTest.build();
    }

    @Test
    public void builderShouldEnsureTokenNotNull() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("This API requires authentication tokenProvider");

        builderUnderTest.baseUrl("http://localhost");
        builderUnderTest.build();
    }

    @Test
    public void builderShouldAllowNullToken() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("tokenProvider == null");

        builderUnderTest.tokenProvider(null);
    }
}
