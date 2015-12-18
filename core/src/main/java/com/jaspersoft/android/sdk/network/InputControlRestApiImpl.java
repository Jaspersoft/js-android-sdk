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


import com.jaspersoft.android.sdk.network.entity.control.InputControl;
import com.jaspersoft.android.sdk.network.entity.control.InputControlCollection;
import com.jaspersoft.android.sdk.network.entity.control.InputControlState;
import com.jaspersoft.android.sdk.network.entity.control.InputControlStateCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit.Call;
import retrofit.Retrofit;
import retrofit.http.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class InputControlRestApiImpl implements InputControlRestApi {
    private final RestApi mRestApi;

    InputControlRestApiImpl(Retrofit restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
    }

    @NotNull
    @Override
    public Collection<InputControl> requestInputControls(@Nullable Cookies cookies,
                                                         @Nullable String reportUri,
                                                         boolean excludeState) throws IOException, HttpException {
        Utils.checkNotNull(reportUri, "Report URI should not be null");
        Utils.checkNotNull(cookies, "Request cookies should not be null");

        String state = (excludeState ? "state" : null);
        Call<InputControlCollection> call = mRestApi.requestInputControls(reportUri, state, cookies.toString());
        InputControlCollection response = CallWrapper.wrap(call).body();
        return response.get();
    }

    @NotNull
    @Override
    public Collection<InputControlState> requestInputControlsInitialStates(@Nullable Cookies cookies,
                                                                           @Nullable String reportUri,
                                                                           boolean freshData) throws IOException, HttpException {
        Utils.checkNotNull(reportUri, "Report URI should not be null");
        Utils.checkNotNull(cookies, "Request cookies should not be null");

        Call<InputControlStateCollection> call = mRestApi.requestInputControlsInitialValues(reportUri, freshData, cookies.toString());
        InputControlStateCollection response = CallWrapper.wrap(call).body();
        return response.get();
    }

    @NotNull
    @Override
    public Collection<InputControlState> requestInputControlsStates(@Nullable Cookies cookies,
                                                                    @Nullable String reportUri,
                                                                    @Nullable Map<String, Set<String>> controlsValues,
                                                                    boolean freshData) throws IOException, HttpException {
        Utils.checkNotNull(reportUri, "Report URI should not be null");
        Utils.checkNotNull(controlsValues, "Controls values should not be null");
        Utils.checkNotNull(cookies, "Request cookies should not be null");

        String ids = Utils.joinString(";", controlsValues.keySet());
        Call<InputControlStateCollection> call = mRestApi.requestInputControlsValues(reportUri, ids, controlsValues, freshData, cookies.toString());
        InputControlStateCollection response = CallWrapper.wrap(call).body();
        return response.get();
    }

    private interface RestApi {
        @NotNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reports{reportUnitURI}/inputControls")
        Call<InputControlCollection> requestInputControls(
                @NotNull @Path(value = "reportUnitURI", encoded = true) String reportUri,
                @Query("exclude") String state,
                @Header("Cookie") String cookie);

        @NotNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reports{reportUnitURI}/inputControls/values")
        Call<InputControlStateCollection> requestInputControlsInitialValues(
                @NotNull @Path(value = "reportUnitURI", encoded = true) String reportUri,
                @Query("freshData") boolean freshData,
                @Header("Cookie") String cookie);

        @NotNull
        @Headers("Accept: application/json")
        @POST("rest_v2/reports{reportUnitURI}/inputControls/{controlsId}/values")
        Call<InputControlStateCollection> requestInputControlsValues(
                @NotNull @Path(value = "reportUnitURI", encoded = true) String reportUri,
                @NotNull @Path(value = "controlsId", encoded = true) String ids,
                @NotNull @Body Map<String, Set<String>> controlsValues,
                @Query("freshData") boolean freshData,
                @Header("Cookie") String cookie);
    }
}
