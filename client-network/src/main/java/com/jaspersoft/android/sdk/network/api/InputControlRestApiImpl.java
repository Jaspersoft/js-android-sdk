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

import com.jaspersoft.android.sdk.network.entity.control.InputControlResponse;
import com.jaspersoft.android.sdk.network.entity.control.InputControlValueResponse;

import java.util.Map;
import java.util.Set;

import retrofit.Call;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

import static com.jaspersoft.android.sdk.network.api.Utils.checkNotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class InputControlRestApiImpl implements InputControlRestApi {
    private final RestApi mRestApi;

    InputControlRestApiImpl(Retrofit restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
    }

    @NonNull
    @Override
    public InputControlResponse requestInputControls(@Nullable String reportUri, boolean excludeState) {
        checkNotNull(reportUri, "Report URI should not be null");

        Call<InputControlResponse> call = mRestApi.requestInputControls(reportUri, excludeState ? "state" : null);
        return CallWrapper.wrap(call).body();
    }

    @NonNull
    @Override
    public InputControlValueResponse requestInputControlsInitialStates(@Nullable String reportUri, boolean freshData) {
        checkNotNull(reportUri, "Report URI should not be null");

        Call<InputControlValueResponse> call = mRestApi.requestInputControlsInitialValues(reportUri, freshData);
        return CallWrapper.wrap(call).body();
    }

    @NonNull
    @Override
    public InputControlValueResponse requestInputControlsStates(@Nullable String reportUri,
                                                                @Nullable Map<String, Set<String>> controlsValues,
                                                                boolean freshData) {
        checkNotNull(reportUri, "Report URI should not be null");
        checkNotNull(controlsValues, "Controls values should not be null");

        String ids = Utils.joinString(";", controlsValues.keySet());
        Call<InputControlValueResponse> call = mRestApi.requestInputControlsValues(reportUri, ids, controlsValues, freshData);
        return CallWrapper.wrap(call).body();
    }

    private interface RestApi {
        @NonNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reports{reportUnitURI}/inputControls")
        Call<InputControlResponse> requestInputControls(
                @NonNull @Path(value = "reportUnitURI", encoded = true) String reportUri,
                @Query("exclude") String state);

        @NonNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reports{reportUnitURI}/inputControls/values")
        Call<InputControlValueResponse> requestInputControlsInitialValues(
                @NonNull @Path(value = "reportUnitURI", encoded = true) String reportUri,
                @Query("freshData") boolean freshData);

        @NonNull
        @Headers("Accept: application/json")
        @POST("rest_v2/reports{reportUnitURI}/inputControls/{controlsId}/values")
        Call<InputControlValueResponse> requestInputControlsValues(
                @NonNull @Path(value = "reportUnitURI", encoded = true) String reportUri,
                @NonNull @Path(value = "controlsId", encoded = true) String ids,
                @NonNull @Body Map<String, Set<String>> controlsValues,
                @Query("freshData") boolean freshData);
    }
}
