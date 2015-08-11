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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.DashboardLookupResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.FolderLookupResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.LegacyDashboardLookupResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.ReportLookupResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.ResourceSearchResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.type.GsonFactory;

import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface RepositoryRestApi {
    @NonNull
    ResourceSearchResponse searchResources(@Nullable Map<String, String> searchParams);

    @NonNull
    ReportLookupResponse requestReportResource(@NonNull String resourceUri);

    @NonNull
    DashboardLookupResponse requestDashboardResource(@NonNull String resourceUri);

    @NonNull
    LegacyDashboardLookupResponse requestLegacyDashboardResource(@NonNull String resourceUri);

    @NonNull
    FolderLookupResponse requestFolderResource(@NonNull String resourceUri);

    class Builder extends AuthBaseBuilder<RepositoryRestApi> {
        public Builder(String baseUrl, String cookie) {
            super(baseUrl, cookie);
        }

        public RepositoryRestApi build() {
            RestAdapter restAdapter = getDefaultBuilder().build();
            return new RepositoryRestApiImpl(restAdapter);
        }
    }
}
