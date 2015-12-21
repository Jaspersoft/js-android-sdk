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

package com.jaspersoft.android.sdk.network;

import com.squareup.okhttp.OkHttpClient;
import org.jetbrains.annotations.TestOnly;

import java.util.concurrent.TimeUnit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public abstract class ClientFactory<Client> {
    final String mBaseUrl;
    final OkHttpClient mOkHttpClient;
    AuthPolicy mAuthPolicy;

    @TestOnly
    ClientFactory(String baseUrl, OkHttpClient okHttpClient) {
        mBaseUrl = baseUrl;
        mOkHttpClient = okHttpClient;
        mAuthPolicy = AuthPolicy.RETRY;
    }

    public ClientFactory<Client> withConnectionTimeOut(long timeout, TimeUnit unit) {
        mOkHttpClient.setConnectTimeout(timeout, unit);
        return this;
    }

    public ClientFactory<Client> withReadTimeout(long timeout, TimeUnit unit) {
        mOkHttpClient.setReadTimeout(timeout, unit);
        return this;
    }

    public ClientFactory<Client> withRetryPolicy(final AuthPolicy authPolicy) {
        mAuthPolicy = authPolicy;
        return this;
    }

    public abstract Client create();
}
