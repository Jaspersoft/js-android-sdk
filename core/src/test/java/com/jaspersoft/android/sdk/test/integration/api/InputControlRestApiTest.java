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

package com.jaspersoft.android.sdk.test.integration.api;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.InputControlRestApi;
import com.jaspersoft.android.sdk.network.entity.control.InputControl;
import com.jaspersoft.android.sdk.network.entity.control.InputControlState;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class InputControlRestApiTest {
    private static final String REPORT_URI = "/public/Samples/Reports/01._Geographic_Results_by_Segment_Report";

    public static final Map<String, Set<String>> CONTROL_PARAMETERS = new HashMap<>();

    static {
        Set<String> values = new HashSet<>();
        values.add("19");
        CONTROL_PARAMETERS.put("sales_fact_ALL__store_sales_2013_1", values);
    }

    private final static LazyClient mLazyClient = new LazyClient(JrsMetadata.createMobileDemo2());
    private static InputControlRestApi apiUnderTest;

    @BeforeClass
    public static void setUp() throws Exception {
        if (apiUnderTest == null) {
            AuthorizedClient client = mLazyClient.getAuthorizedClient();
            apiUnderTest = client.inputControlApi();
        }
    }

    @Test
    public void shouldProvideInputControlsList() throws Exception {
        Collection<InputControl> controls = apiUnderTest.requestInputControls(REPORT_URI, false);
        assertThat(controls, is(not(empty())));

        InputControl control = new ArrayList<>(controls).get(0);
        assertThat(control.getState(), is(notNullValue()));
    }

    /**
     * TODO: Implement annotation to mark specific API tests.
     */
    @Test
    public void shouldProvideInputControlsListIfStateExcluded() throws Exception {
        Collection<InputControl> controls = apiUnderTest.requestInputControls(REPORT_URI, true);
        assertThat(controls, is(not(empty())));

        InputControl control = new ArrayList<>(controls).get(0);
        assertThat(control.getState(), is(nullValue()));
    }

    @Test
    public void shouldProvideFreshInitialInputControlsValues() throws Exception {
        Collection<InputControlState> states = apiUnderTest.requestInputControlsInitialStates(REPORT_URI, true);
        assertThat(states, is(not(empty())));
    }

    @Test
    public void shouldProvideFreshStatesForInputControls() throws Exception {
        Collection<InputControlState> states = apiUnderTest.requestInputControlsStates(REPORT_URI, CONTROL_PARAMETERS, true);
        assertThat(states, is(not(empty())));
    }
}
