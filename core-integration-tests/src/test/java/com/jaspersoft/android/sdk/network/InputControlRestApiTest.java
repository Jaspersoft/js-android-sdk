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
import com.jaspersoft.android.sdk.env.DummyTokenProvider;
import com.jaspersoft.android.sdk.env.JrsMetadata;
import com.jaspersoft.android.sdk.env.TestLogger;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class InputControlRestApiTest {
    private static final String REPORT_URI = "/public/Samples/Reports/01._Geographic_Results_by_Segment_Report";
    private final JrsMetadata mMetadata = JrsMetadata.createMobileDemo2();
    private final DummyTokenProvider mAuthenticator = DummyTokenProvider.create(mMetadata);
    private InputControlRestApi mRestApi;
    public static final Map<String, Set<String>> CONTROL_PARAMETERS = new HashMap<>();

    static {
        Set<String> values = new HashSet<>();
        values.add("19");
        CONTROL_PARAMETERS.put("sales_fact_ALL__store_sales_2013_1", values);
    }

    @Before
    public void setup() {
        mRestApi = new InputControlRestApi.Builder()
                .baseUrl(mMetadata.getServerUrl())
                .logger(TestLogger.get(this))
                .build();
    }

    @Test
    public void shouldProvideInputControlsList() {
        Collection<InputControl> controls = mRestApi.requestInputControls(mAuthenticator.token(), REPORT_URI, false);
        assertThat(controls, is(not(empty())));

        InputControl control = new ArrayList<>(controls).get(0);
        assertThat(control.getState(), is(notNullValue()));
    }

    /**
     * TODO: Implement annotation to mark specific API tests.
     */
    @Test
    public void shouldProvideInputControlsListIfStateExcluded() {
        Collection<InputControl> controls = mRestApi.requestInputControls(mAuthenticator.token(), REPORT_URI, true);
        assertThat(controls, is(not(empty())));

        InputControl control = new ArrayList<>(controls).get(0);
        assertThat(control.getState(), is(nullValue()));
    }

    @Test
    public void shouldProvideFreshInitialInputControlsValues() {
        Collection<InputControlState> states = mRestApi.requestInputControlsInitialStates(mAuthenticator.token(), REPORT_URI, true);
        assertThat(states, is(not(empty())));
    }

    @Test
    public void shouldProvideFreshStatesForInputControls() {
        Collection<InputControlState> states = mRestApi.requestInputControlsStates(mAuthenticator.token(), REPORT_URI, CONTROL_PARAMETERS, true);
        assertThat(states, is(not(empty())));
    }
}
