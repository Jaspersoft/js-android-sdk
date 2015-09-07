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

package com.jaspersoft.android.sdk.test.integration.api;

import com.jaspersoft.android.sdk.network.api.InputControlRestApi;
import com.jaspersoft.android.sdk.network.entity.control.InputControl;
import com.jaspersoft.android.sdk.network.entity.control.InputControlResponse;
import com.jaspersoft.android.sdk.network.entity.control.InputControlValueResponse;
import com.jaspersoft.android.sdk.test.TestLogger;
import com.jaspersoft.android.sdk.test.integration.api.utils.JrsMetadata;
import com.jaspersoft.android.sdk.test.integration.api.utils.TestAuthenticator;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import rx.Observable;

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
    private final TestAuthenticator mAuthenticator = TestAuthenticator.newInstance(mMetadata);
    private InputControlRestApi mRestApi;

    @Before
    public void setup() {
        mAuthenticator.authorize();
        String cookie = mAuthenticator.getCookie();
        mRestApi = new InputControlRestApi.Builder(mMetadata.getServerUrl(), cookie)
                .setLog(TestLogger.get(this))
                .build();
    }

    @Test
    public void shouldProvideInputControlsList() throws IOException {
        Observable<InputControlResponse> call = mRestApi.requestInputControls(REPORT_URI);

        List<InputControl> controls = call.toBlocking().first().getValues();
        assertThat(controls, is(not(empty())));

        InputControl control = controls.get(0);
        assertThat(control.getState(), is(notNullValue()));
    }

    /**
     * TODO: Implement annotation to mark specific API tests.
     */
    @Test
    public void shouldProvideInputControlsListIfStateExcluded() throws IOException {
        Observable<InputControlResponse> call = mRestApi.requestInputControls(REPORT_URI, true);

        List<InputControl> controls = call.toBlocking().first().getValues();
        assertThat(controls, is(not(empty())));

        InputControl control = controls.get(0);
        assertThat(control.getState(), is(nullValue()));
    }

    @Test
    public void shouldProvideInitialInputControlsValues() throws IOException {
        Observable<InputControlValueResponse> call = mRestApi.requestInputControlsInitialStates(REPORT_URI);
        InputControlValueResponse response = call.toBlocking().first();
        assertThat(response.getValues(), is(not(empty())));
    }

    @Test
    public void shouldProvideFreshInitialInputControlsValues() throws IOException {
        Observable<InputControlValueResponse> call = mRestApi.requestInputControlsInitialStates(REPORT_URI, true);
        InputControlValueResponse response = call.toBlocking().first();
        assertThat(response.getValues(), is(not(empty())));
    }
}