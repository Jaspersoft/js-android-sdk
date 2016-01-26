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
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class InputControlRestApi {

    private final NetworkClient mNetworkClient;

    InputControlRestApi(NetworkClient networkClient) {
        mNetworkClient = networkClient;
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
                                                   @Nullable Set<String> controlIds,
                                                   boolean excludeState) throws IOException, HttpException {
        Utils.checkNotNull(reportUri, "Report URI should not be null");


        String ids = "";
        if (controlIds != null && !controlIds.isEmpty()) {
            ids = Utils.joinString(";", controlIds);
        }

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("reports")
                .addPaths(reportUri)
                .addPath("inputControls")
                .addPath(ids)
                .build()
                .resolve(mNetworkClient.getBaseUrl());
        if (excludeState) {
            url = url.newBuilder()
                    .addQueryParameter("exclude", "state")
                    .build();
        }

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response response = mNetworkClient.makeCall(request);
        InputControlCollection inputControlCollection = mNetworkClient.deserializeJson(response, InputControlCollection.class);
        return Collections.unmodifiableList(inputControlCollection.get());
    }

    @NotNull
    public List<InputControlState> requestInputControlsInitialStates(@Nullable String reportUri,
                                                                     boolean freshData) throws IOException, HttpException {
        Utils.checkNotNull(reportUri, "Report URI should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("reports")
                .addPaths(reportUri)
                .addPath("inputControls")
                .addPath("values")
                .build()
                .resolve(mNetworkClient.getBaseUrl())
                .newBuilder()
                .addQueryParameter("freshData", String.valueOf(freshData))
                .build();

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response response = mNetworkClient.makeCall(request);
        InputControlStateCollection inputControlStateCollection = mNetworkClient.deserializeJson(response, InputControlStateCollection.class);
        return Collections.unmodifiableList(inputControlStateCollection.get());
    }

    /**
     * Provides values for specified controls. This API helpful to
     * delegate cascading resolving for the server, also should handle non-cascading cases
     *
     * @param reportUri  uri of report
     * @param parameters {control_id: [value, value]} associated with input controls
     * @param freshData  whether data should be retrieved from cache or not
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

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("reports")
                .addPaths(reportUri)
                .addPath("inputControls")
                .addPath(ids)
                .addPath("values")
                .build()
                .resolve(mNetworkClient.getBaseUrl())
                .newBuilder()
                .addQueryParameter("freshData", String.valueOf(freshData))
                .build();


        RequestBody jsonRequestBody = mNetworkClient.createJsonRequestBody(params);
        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .post(jsonRequestBody)
                .url(url)
                .build();

        com.squareup.okhttp.Response response = mNetworkClient.makeCall(request);
        InputControlStateCollection inputControlStateCollection = mNetworkClient.deserializeJson(response, InputControlStateCollection.class);
        return Collections.unmodifiableList(inputControlStateCollection.get());
    }
}
