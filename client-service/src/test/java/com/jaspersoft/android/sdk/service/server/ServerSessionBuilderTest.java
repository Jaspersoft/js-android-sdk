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

package com.jaspersoft.android.sdk.service.server;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ServerSessionBuilderTest {

    private ServerSession.Builder objectUnderTest;

    @Rule
    public ExpectedException mException = ExpectedException.none();

    @Before
    public void setup() {
        objectUnderTest = new ServerSession.Builder();
    }

    @Test
    public void builderShouldNotAcceptNullServerUrl() {
        mException.expect(NullPointerException.class);
        mException.expectMessage("serverUrl == null");
        objectUnderTest.serverUrl(null);
    }

    @Test
    public void builderShouldNotAllowNullServerUrl() {
        mException.expect(IllegalStateException.class);
        mException.expectMessage("Session requires server url");
        objectUnderTest.build();
    }

    @Test
    public void builderShouldNotAcceptNullTokenProvider() {
        mException.expect(NullPointerException.class);
        mException.expectMessage("tokenProvider == null");
        objectUnderTest.tokenProvider(null);
    }

    @Test
    public void builderShouldNotAllowNullTokenProvider() {
        mException.expect(IllegalStateException.class);
        mException.expectMessage("Session requires token provider");
        objectUnderTest.serverUrl("any url").build();
    }
}
