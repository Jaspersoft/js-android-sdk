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

import com.jaspersoft.android.sdk.network.entity.control.InputControlResponse;
import com.jaspersoft.android.sdk.network.entity.control.InputControlValueResponse;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * @author Tom Koptel
 * @since 2.2
 */
final class InputControlRestApiImpl implements InputControlRestApi {
    private final RestApi mRestApi;

    InputControlRestApiImpl(RestAdapter restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
    }

    @NonNull
    @Override
    public InputControlResponse requestInputControls(@NonNull String reportUri) {
        return mRestApi.requestInputControls(reportUri, null);
    }

    @NonNull
    @Override
    public InputControlResponse requestInputControls(@NonNull String reportUri, boolean excludeState) {
        return mRestApi.requestInputControls(reportUri, excludeState ? "state" : null);
    }

    @NonNull
    @Override
    public InputControlValueResponse requestInputControlsInitialValues(@NonNull String reportUri) {
        return mRestApi.requestInputControlsInitialValues(reportUri, false);
    }

    @NonNull
    @Override
    public InputControlValueResponse requestInputControlsInitialValues(@NonNull String reportUri, boolean freshData) {
        return mRestApi.requestInputControlsInitialValues(reportUri, freshData);
    }

    private interface RestApi {
        @NonNull
        @Headers("Accept: application/json")
        @GET("/rest_v2/reports{reportUnitURI}/inputControls")
        InputControlResponse requestInputControls(
                @NonNull @Path(value = "reportUnitURI", encode = false) String reportUri,
                @Query("exclude") String state);

        @NonNull
        @Headers("Accept: application/json")
        @GET("/rest_v2/reports{reportUnitURI}/inputControls/values")
        InputControlValueResponse requestInputControlsInitialValues(
                @NonNull @Path(value = "reportUnitURI", encode = false) String reportUri,
                @Query("freshData") boolean freshData);
    }
}
