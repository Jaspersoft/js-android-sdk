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

import com.jaspersoft.android.sdk.network.api.HeaderUtil;
import com.jaspersoft.android.sdk.network.api.SafeHeader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;

import retrofit.client.Header;
import retrofit.client.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Response.class)
public class HeaderUtilTest {
    @Mock
    Response mResponse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnSafeHeaderIfSearchResultWasEmpty() {
        when(mResponse.getHeaders()).thenReturn(Collections.EMPTY_LIST);
        HeaderUtil util = HeaderUtil.wrap(mResponse);
        SafeHeader safeHeader = util.getFirstHeader("name");
        assertThat(safeHeader, is(notNullValue()));
    }

    @Test
    public void shouldReturnSafeHeaderForSuccessfulSearch() {
        when(mResponse.getHeaders()).thenReturn(new ArrayList<Header>(){{
            add(new Header("name", "value"));
        }});
        HeaderUtil util = HeaderUtil.wrap(mResponse);
        SafeHeader safeHeader = util.getFirstHeader("name");
        assertThat(safeHeader, is(notNullValue()));
    }
}
