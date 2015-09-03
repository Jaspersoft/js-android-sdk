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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jaspersoft.android.sdk.network.entity.server.AuthResponse;
import com.squareup.okhttp.OkHttpClient;

import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface AuthenticationRestApi {
    @NonNull
    AuthResponse authenticate(@NonNull String username,
                              @NonNull String password,
                              @Nullable String organization,
                              @Nullable Map<String, String> params);

    final class Builder extends BaseBuilder<AuthenticationRestApi, Builder> {
        public Builder(String baseUrl) {
            super(baseUrl);
        }

        @Override
        AuthenticationRestApi createApi() {

            OkHttpClient httpClient = getClient();
            httpClient.setFollowRedirects(false);

            return new AuthenticationRestApiImpl(getDefaultBuilder().build());
        }
    }
}
