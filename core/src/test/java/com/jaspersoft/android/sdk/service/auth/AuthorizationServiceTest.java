/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.auth;

import com.jaspersoft.android.sdk.network.AnonymousClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.MockitoAnnotations.initMocks;

public class AuthorizationServiceTest {

    @Mock
    AnonymousClient mAnonymousClient;
    @Rule
    public ExpectedException expected = none();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void should_not_allow_null_client() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Client should not be null");
        AuthorizationService.newService(null);
    }

    @Test
    public void should_create_new_proxy_service() throws Exception {
        AuthorizationService service = AuthorizationService.newService(mAnonymousClient);
        assertThat(service, is(instanceOf(ProxyAuthorizationService.class)));
        assertThat(service, is(notNullValue()));
    }
}