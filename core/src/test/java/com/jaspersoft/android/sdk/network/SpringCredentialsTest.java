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

package com.jaspersoft.android.sdk.network;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class SpringCredentialsTest {

    private SpringCredentials.Builder objectUnderTest;

    @Mock
    AuthenticationRestApi mRestApi;

    @Rule
    public ExpectedException mException = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectUnderTest = SpringCredentials.builder();
    }

    @Test
    public void builderShouldNotAllowNullUsername() {
        mException.expect(NullPointerException.class);
        mException.expectMessage("username == null");

        objectUnderTest.withUsername(null);
    }

    @Test
    public void builderShouldNotAllowNullPassword() {
        mException.expect(NullPointerException.class);
        mException.expectMessage("password == null");

        objectUnderTest.withPassword(null);
    }

    @Test
    public void builderShouldNotAllowNullLocale() {
        mException.expect(NullPointerException.class);
        mException.expectMessage("locale == null");

        objectUnderTest.withLocale(null);
    }

    @Test
    public void builderShouldNotAllowNullTimeZone() {
        mException.expect(NullPointerException.class);
        mException.expectMessage("timeZone == null");

        objectUnderTest.withTimeZone(null);
    }

    @Test
    public void testEqualsHashcodeContract() {
        EqualsVerifier.forClass(SpringCredentials.class).verify();
    }
}
