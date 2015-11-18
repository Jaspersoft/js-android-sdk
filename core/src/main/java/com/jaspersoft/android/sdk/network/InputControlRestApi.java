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
import com.jaspersoft.android.sdk.network.entity.control.InputControlState;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface InputControlRestApi {

    /**
     * Returns input controls for associated response. Options can be excluded by additional argument.
     *
     * <b>ATTENTION:</b> Exclude flag works only on JRS instances 6.0+
     *
     * @param token        is a key API sends to authorize client
     * @param reportUri    uri of report
     * @param excludeState exclude field state which incorporates options values for control
     * @return unmodifiable list of {@link InputControl}
     */
    @NotNull
    Collection<InputControl> requestInputControls(@NotNull String token,
                                                  @NotNull String reportUri,
                                                  boolean excludeState) throws HttpException, IOException;

    @NotNull
    Collection<InputControlState> requestInputControlsInitialStates(@NotNull String token,
                                                                    @NotNull String reportUri,
                                                                    boolean freshData) throws HttpException, IOException;

    /**
     * Provides values for specified controls. This API helpful to
     * delegate cascading resolving for the server, also should handle non-cascading cases
     *
     * @param token        is a key API sends to authorize client
     * @param reportUri      uri of report
     * @param controlsValues map of {control_id: [value, value]} associated input controls metadata
     * @param freshData      whether data should be retrieved from cache or not
     * @return unmodifiable list of {@link InputControlState}
     */
    @NotNull
    Collection<InputControlState> requestInputControlsStates(@NotNull String token,
                                                             @NotNull String reportUri,
                                                             @NotNull Map<String, Set<String>> controlsValues,
                                                             boolean freshData) throws HttpException, IOException;

    final class Builder extends GenericBuilder<Builder, InputControlRestApi> {
        @Override
        InputControlRestApi createApi() {
            return new InputControlRestApiImpl(getAdapter().build());
        }
    }
}
