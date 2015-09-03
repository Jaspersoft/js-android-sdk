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
import android.support.annotation.Nullable;

import com.jaspersoft.android.sdk.network.entity.resource.DashboardLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.FolderLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.LegacyDashboardLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.ReportLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResponse;

import java.util.Map;

import retrofit.Call;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface RepositoryRestApi {
    @NonNull
    ResourceSearchResponse searchResources(@Nullable Map<String, String> searchParams);

    @NonNull
    Call<ReportLookupResponse> requestReportResource(@NonNull String resourceUri);

    @NonNull
    Call<DashboardLookupResponse> requestDashboardResource(@NonNull String resourceUri);

    @NonNull
    Call<LegacyDashboardLookupResponse> requestLegacyDashboardResource(@NonNull String resourceUri);

    @NonNull
    Call<FolderLookupResponse> requestFolderResource(@NonNull String resourceUri);

    final class Builder extends AuthBaseBuilder<RepositoryRestApi, Builder> {
        public Builder(String baseUrl, String cookie) {
            super(baseUrl, cookie);
        }

        @Override
        RepositoryRestApi createApi() {
            return new RepositoryRestApiImpl(getDefaultBuilder().build());
        }
    }
}
