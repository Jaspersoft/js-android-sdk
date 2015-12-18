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

import com.jaspersoft.android.sdk.network.entity.server.EncryptionKey;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface AuthenticationRestApi {
    @NotNull
    Cookies authenticate(@NotNull String username,
                        @NotNull String password,
                        @Nullable String organization,
                        @Nullable Map<String, String> params) throws HttpException, IOException;

    @NotNull
    EncryptionKey requestEncryptionMetadata() throws HttpException, IOException;

    final class Builder extends GenericBuilder<Builder, AuthenticationRestApi> {
        @Override
        AuthenticationRestApi createApi() {
            HttpUrl baseUrl = adapterBuilder.baseUrl;
            OkHttpClient okHttpClient = clientBuilder.getClient();
            okHttpClient.setFollowRedirects(false);
            return new AuthenticationRestApiImpl(baseUrl, okHttpClient, getAdapter());
        }
    }
}
