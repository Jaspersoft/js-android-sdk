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
import com.jaspersoft.android.sdk.test.MockResponseFactory;
import com.jaspersoft.android.sdk.test.WebMockRule;
import com.jaspersoft.android.sdk.test.resource.ResourceFile;
import com.jaspersoft.android.sdk.test.resource.TestResource;
import com.jaspersoft.android.sdk.test.resource.inject.TestResourceInjector;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import retrofit.Retrofit;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@SuppressWarnings("unchecked")
public class InputControlRestApiTest {

    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();

    private InputControlRestApi restApiUnderTest;

    @ResourceFile("json/input_controls_states_list.json")
    TestResource icsStates;
    @ResourceFile("json/input_controls_without_states.json")
    TestResource icsWithoutStates;
    @ResourceFile("json/input_controls_with_states.json")
    TestResource icsWithStates;

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
        Server server = Server.builder()
                .withBaseUrl(mWebMockRule.getRootUrl())
                .build();
        Retrofit retrofit = server.newRetrofit().build();
        restApiUnderTest = new InputControlRestApiImpl(retrofit);
    }

    @Test
    public void requestInputControlsShouldNotAllowNullReportUri() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report URI should not be null");
        restApiUnderTest.requestInputControls(null, false);
    }

    @Test
    public void requestInputControlsStatesShouldNotAllowNullReportUri() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report URI should not be null");
        restApiUnderTest.requestInputControlsStates(null, Collections.EMPTY_MAP, true);
    }

    @Test
    public void requestInputControlsStatesShouldNotAllowNullControlParams() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Controls values should not be null");
        restApiUnderTest.requestInputControlsStates("any_id", null, true);
    }

    @Test
    public void requestInputControlsShouldThrowRestErrorFor500() throws Exception {
        mExpectedException.expect(HttpException.class);
        mWebMockRule.enqueue(MockResponseFactory.create500());
        restApiUnderTest.requestInputControls("any_id", true);
    }

    @Test
    public void requestInputControlsInitialStatesShouldThrowRestErrorFor500() throws Exception {
        mExpectedException.expect(HttpException.class);
        mWebMockRule.enqueue(MockResponseFactory.create500());
        restApiUnderTest.requestInputControlsInitialStates("any_id", true);
    }

    @Test
    public void requestInputControlsStatesShouldThrowRestErrorFor500() throws Exception {
        mExpectedException.expect(HttpException.class);
        mWebMockRule.enqueue(MockResponseFactory.create500());
        restApiUnderTest.requestInputControlsStates("any_id", Collections.EMPTY_MAP, true);
    }

    @Test
    public void apiShouldProvideListOfInputControlsInitialStatesWithFreshData() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(icsStates.asString());
        mWebMockRule.enqueue(mockResponse);

        Collection<InputControlState> states = restApiUnderTest.requestInputControlsInitialStates("/my/uri", true);
        assertThat(states, is(not(empty())));

        RecordedRequest response = mWebMockRule.get().takeRequest();
        assertThat(response.getPath(), is("/rest_v2/reports/my/uri/inputControls/values?freshData=true"));
    }

    @Test
    public void apiShouldProvideFreshStatesForInputControls() throws Exception {
        Map<String, Set<String>> parameters = new HashMap<>();
        Set<String> values = new HashSet<>();
        values.add("19");
        parameters.put("sales_fact_ALL__store_sales_2013_1", values);

        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(icsStates.asString());
        mWebMockRule.enqueue(mockResponse);

        Collection<InputControlState> states = restApiUnderTest.requestInputControlsStates("/my/uri", parameters, true);
        assertThat(states, Matchers.is(not(Matchers.empty())));

        RecordedRequest response = mWebMockRule.get().takeRequest();
        assertThat(response.getPath(), is("/rest_v2/reports/my/uri/inputControls/sales_fact_ALL__store_sales_2013_1/values?freshData=true"));
    }

    @Test
    public void apiShouldProvideInputControlsListIfStateExcluded() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(icsWithoutStates.asString());
        mWebMockRule.enqueue(mockResponse);

        Collection<InputControl> controls = restApiUnderTest.requestInputControls("/my/uri", true);
        assertThat(controls, Matchers.is(not(Matchers.empty())));
        assertThat(new ArrayList<>(controls).get(0).getState(), is(nullValue()));

        RecordedRequest response = mWebMockRule.get().takeRequest();
        assertThat(response.getPath(), is("/rest_v2/reports/my/uri/inputControls?exclude=state"));
    }

    @Test
    public void apiShouldProvideInputControlsWithStates() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(icsWithStates.asString());
        mWebMockRule.enqueue(mockResponse);

        Collection<InputControl> controls = restApiUnderTest.requestInputControls("/my/uri", false);
        assertThat(controls, Matchers.is(not(Matchers.empty())));
        assertThat(new ArrayList<>(controls).get(0).getState(), is(not(nullValue())));

        RecordedRequest response = mWebMockRule.get().takeRequest();
        assertThat(response.getPath(), is("/rest_v2/reports/my/uri/inputControls"));
    }
}