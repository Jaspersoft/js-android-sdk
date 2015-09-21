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

package com.jaspersoft.android.sdk.service.auth;

import com.jaspersoft.android.sdk.network.api.AuthenticationRestApi;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class SpringAuthServiceBuilderTest {

    private SpringAuthService.Builder objectUnderTest;

    @Mock
    AuthenticationRestApi mRestApi;

    @Rule
    public ExpectedException mException = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectUnderTest = new SpringAuthService.Builder();
    }

    @Test
    public void builderShouldNotAllowNullUsername() {
        mException.expect(NullPointerException.class);
        mException.expectMessage("username == null");

        objectUnderTest.username(null);
    }

    @Test
    public void builderShouldNotAllowNullPassword() {
        mException.expect(NullPointerException.class);
        mException.expectMessage("password == null");

        objectUnderTest.password(null);
    }

    @Test
    public void builderShouldNotAllowNullRestApi() {
        mException.expect(NullPointerException.class);
        mException.expectMessage("restApi == null");

        objectUnderTest.restApi(null);
    }

    @Test
    public void builderShouldNotAllowNullLocale() {
        mException.expect(NullPointerException.class);
        mException.expectMessage("locale == null");

        objectUnderTest.locale(null);
    }

    @Test
    public void builderShouldNotAllowNullTimeZone() {
        mException.expect(NullPointerException.class);
        mException.expectMessage("timeZone == null");

        objectUnderTest.timeZone(null);
    }

    @Test
    public void serviceShouldThrowIfBuildWithNullUsername() {
        mException.expect(IllegalStateException.class);
        mException.expectMessage("Username should not be null");

        objectUnderTest.restApi(mRestApi);
        objectUnderTest.password("");
        objectUnderTest.build();
    }

    @Test
    public void serviceShouldThrowIfBuildWithNullPassword() {
        mException.expect(IllegalStateException.class);
        mException.expectMessage("Password should not be null");

        objectUnderTest.restApi(mRestApi);
        objectUnderTest.username("");
        objectUnderTest.build();
    }

    @Test
    public void serviceShouldThrowIfBuildWithNullRestApi() {
        mException.expect(IllegalStateException.class);
        mException.expectMessage("Rest api should not be null. Either set it or call withDefaultApiProvider(url)");

        objectUnderTest.username("");
        objectUnderTest.password("");
        objectUnderTest.build();
    }

    @Test
    public void builderShouldCreateServiceWithDefaultApi() {
        objectUnderTest.username("");
        objectUnderTest.password("");

        SpringAuthService service = objectUnderTest.withDefaultApiProvider("http://localhost/").build();
        assertThat(service, is(notNullValue()));
    }
}
