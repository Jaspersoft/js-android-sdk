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

import com.jaspersoft.android.sdk.network.rest.v2.entity.type.GsonFactory;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * @author Tom Koptel
 * @since 2.0
 */
abstract class AuthBaseBuilder<Api> extends BaseBuilder<Api> {
    private final String mCookie;

    public AuthBaseBuilder(String baseUrl, String cookie) {
        super(baseUrl);
        if (cookie == null || cookie.length() == 0) {
            throw new IllegalArgumentException("Cookie should not be null or empty");
        }
        mCookie = cookie;
    }

    @Override
    public RestAdapter.Builder getDefaultBuilder() {
        RestAdapter.Builder builder = super.getDefaultBuilder();

        builder.setConverter(new GsonConverter(GsonFactory.create()));
        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Cookie", getCookie());
            }
        });

        return builder;
    }

    public String getCookie() {
        return mCookie;
    }
}
