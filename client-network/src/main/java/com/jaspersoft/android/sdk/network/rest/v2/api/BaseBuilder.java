/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
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

import com.jaspersoft.android.sdk.network.rest.v2.exception.ErrorHandler;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * @author Tom Koptel
 * @since 2.0
 */
abstract class BaseBuilder<API> {
    private final String mBaseUrl;
    private final RestAdapter.Builder mRestAdapterBuilder;

    public BaseBuilder(String baseUrl){
        if (baseUrl == null || baseUrl.length() == 0) {
            throw new IllegalArgumentException("Base url should not be null or empty");
        }
        mBaseUrl = baseUrl;
        mRestAdapterBuilder = new RestAdapter.Builder();
        mRestAdapterBuilder.setEndpoint(mBaseUrl);
        mRestAdapterBuilder.setErrorHandler(new retrofit.ErrorHandler() {
            @Override
            @SuppressWarnings("unchecked")
            public Throwable handleError(RetrofitError cause) {
                return ErrorHandler.DEFAULT.handleError(cause);
            }
        });
    }

    protected RestAdapter.Builder getDefaultBuilder() {
        return mRestAdapterBuilder;
    }

    public abstract API build();
}
