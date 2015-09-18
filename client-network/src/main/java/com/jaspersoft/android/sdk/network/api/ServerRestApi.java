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

package com.jaspersoft.android.sdk.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.jaspersoft.android.sdk.network.entity.server.ServerInfoResponse;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface ServerRestApi {

    @NonNull
    @WorkerThread
    ServerInfoResponse requestServerInfo();
    @NonNull
    @WorkerThread
    String requestBuild();
    @NonNull
    @WorkerThread
    String requestDateFormatPattern();
    @NonNull
    @WorkerThread
    String requestDateTimeFormatPattern();
    @NonNull
    @WorkerThread
    String requestEdition();
    @NonNull
    @WorkerThread
    String requestEditionName();
    @NonNull
    @WorkerThread
    String requestVersion();
    @NonNull
    @WorkerThread
    String requestFeatures();
    @NonNull
    @WorkerThread
    String requestLicenseType();
    @NonNull
    @WorkerThread
    String requestExpiration();

    final class Builder extends GenericBuilder<Builder, ServerRestApi> {
        @Override
        ServerRestApi createApi() {
            return new ServerRestApiImpl(getAdapter().build());
        }
    }
}
