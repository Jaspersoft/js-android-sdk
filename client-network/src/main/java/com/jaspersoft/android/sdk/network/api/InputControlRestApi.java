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

import com.jaspersoft.android.sdk.network.entity.control.InputControl;
import com.jaspersoft.android.sdk.network.entity.control.InputControlResponse;
import com.jaspersoft.android.sdk.network.entity.control.InputControlState;
import com.jaspersoft.android.sdk.network.entity.control.InputControlValueResponse;

import java.util.Map;
import java.util.Set;

import rx.Observable;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface InputControlRestApi {

    @NonNull
    Observable<InputControlResponse> requestInputControls(@NonNull String reportUri);

    /**
     * Returns input controls for associated response. Options can be excluded by additional argument.
     *
     * <b>ATTENTION:<b/> Exclude flag works only on JRS instances 6.0+
     *
     * @param reportUri uri of report
     * @param excludeState exclude field state which incorporates options values for control
     * @return unmodifiable list of {@link InputControl}
     */
    @NonNull
    Observable<InputControlResponse> requestInputControls(@NonNull String reportUri, boolean excludeState);

    @NonNull
    Observable<InputControlValueResponse> requestInputControlsInitialStates(@NonNull String reportUri);

    @NonNull
    Observable<InputControlValueResponse> requestInputControlsInitialStates(@NonNull String reportUri,
                                                              boolean freshData);

    /**
     * TODO: 1. consider to flatten controls id parameter.
     */
    @NonNull
    Observable<InputControlValueResponse> requestInputControlsStates(@NonNull String reportUri,
                                                       @NonNull Map<String, Set<String>> controlsValues);

    /**
     * Provides values for specified controls. This API helpful to
     * delegate cascading resolving for the server, also should handle non-cascading cases
     *
     * @param reportUri uri of report
     * @param controlsValues map of {control_id: [value, value]} associated input controls metadata
     * @param freshData whether data should be retrieved from cache or not
     * @return unmodifiable list of {@link InputControlState}
     */
    @NonNull
    Observable<InputControlValueResponse> requestInputControlsStates(@NonNull String reportUri,
                                                       @NonNull Map<String, Set<String>> controlsValues,
                                                       boolean freshData);

    final class Builder extends AuthBaseBuilder<InputControlRestApi, Builder> {
        public Builder(String baseUrl, String cookie) {
            super(baseUrl, cookie);
        }

        @Override
        InputControlRestApi createApi() {
            return new InputControlRestApiImpl(getDefaultBuilder().build());
        }
    }
}
