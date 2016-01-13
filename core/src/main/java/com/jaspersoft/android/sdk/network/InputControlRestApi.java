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
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit.Call;
import retrofit.Retrofit;
import retrofit.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class InputControlRestApi {
    private final RestApi mRestApi;

    InputControlRestApi(Retrofit restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
    }

    /**
     * Returns input controls for associated response. Options can be excluded by additional argument.
     * <p/>
     * <b>ATTENTION:</b> Exclude flag works only on JRS instances 6.0+
     *
     * @param reportUri    uri of report
     * @param excludeState exclude field state which incorporates options values for control
     * @return unmodifiable list of {@link InputControl}
     */
    @NotNull
    public List<InputControl> requestInputControls(@Nullable String reportUri,
                                                         boolean excludeState) throws IOException, HttpException {
        Utils.checkNotNull(reportUri, "Report URI should not be null");

        String state = (excludeState ? "state" : null);
        Call<InputControlCollection> call = mRestApi.requestInputControls(reportUri, state);
        InputControlCollection response = CallWrapper.wrap(call).body();
        return response.get();
    }

    @NotNull
    public List<InputControlState> requestInputControlsInitialStates(@Nullable String reportUri,
                                                                           boolean freshData) throws IOException, HttpException {
        Utils.checkNotNull(reportUri, "Report URI should not be null");

        Call<InputControlStateCollection> call = mRestApi.requestInputControlsInitialValues(reportUri, freshData);
        InputControlStateCollection response = CallWrapper.wrap(call).body();
        return response.get();
    }

    /**
     * Provides values for specified controls. This API helpful to
     * delegate cascading resolving for the server, also should handle non-cascading cases
     *
     * @param reportUri      uri of report
     * @param parameters     {control_id: [value, value]} associated with input controls
     * @param freshData      whether data should be retrieved from cache or not
     * @return unmodifiable list of {@link InputControlState}
     */
    @NotNull
    public List<InputControlState> requestInputControlsStates(@NotNull String reportUri,
                                                              @NotNull List<ReportParameter> parameters,
                                                              boolean freshData) throws HttpException, IOException {
        Utils.checkNotNull(reportUri, "Report URI should not be null");
        Utils.checkNotNull(parameters, "Parameters should not be null");

        Map<String, Set<String>> params = ReportParamsMapper.INSTANCE.toMap(parameters);
        String ids = Utils.joinString(";", params.keySet());
        Call<InputControlStateCollection> call = mRestApi.requestInputControlsValues(reportUri,
                ids, params, freshData);
        InputControlStateCollection response = CallWrapper.wrap(call).body();
        return response.get();
    }

    private interface RestApi {
        @NotNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reports{reportUnitURI}/inputControls")
        Call<InputControlCollection> requestInputControls(
                @NotNull @Path(value = "reportUnitURI", encoded = true) String reportUri,
                @Query("exclude") String state);

        @NotNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reports{reportUnitURI}/inputControls/values")
        Call<InputControlStateCollection> requestInputControlsInitialValues(
                @NotNull @Path(value = "reportUnitURI", encoded = true) String reportUri,
                @Query("freshData") boolean freshData);

        @NotNull
        @Headers("Accept: application/json")
        @POST("rest_v2/reports{reportUnitURI}/inputControls/{controlsId}/values")
        Call<InputControlStateCollection> requestInputControlsValues(
                @NotNull @Path(value = "reportUnitURI", encoded = true) String reportUri,
                @NotNull @Path(value = "controlsId", encoded = true) String ids,
                @NotNull @Body Map<String, Set<String>> controlsValues,
                @Query("freshData") boolean freshData);
    }
}
