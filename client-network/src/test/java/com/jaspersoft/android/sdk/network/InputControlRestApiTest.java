package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.InputControlRestApi;
import com.jaspersoft.android.sdk.network.RestError;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        restApiUnderTest = new InputControlRestApi.Builder()
                .baseUrl(mWebMockRule.getRootUrl())
                .build();
    }

    @Test
    public void requestInputControlsShouldNotAllowNullReportUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report URI should not be null");
        restApiUnderTest.requestInputControls("cookie", null, false);
    }

    @Test
    public void requestInputControlsInitialStatesShouldNotAllowNullToken() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");
        restApiUnderTest.requestInputControlsInitialStates(null, "/uri", false);
    }

    @Test
    public void requestInputControlsStatesShouldNotAllowNullReportUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report URI should not be null");
        restApiUnderTest.requestInputControlsStates("cookie", null, Collections.EMPTY_MAP, true);
    }

    @Test
    public void requestInputControlsStatesShouldNotAllowNullControlParams() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Controls values should not be null");
        restApiUnderTest.requestInputControlsStates("cookie", "any_id", null, true);
    }

    @Test
    public void requestInputControlsStatesShouldNotAllowNullToken() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");
        restApiUnderTest.requestInputControlsStates(null, "any_id", Collections.EMPTY_MAP, true);
    }

    @Test
    public void requestInputControlsShouldThrowRestErrorFor500() {
        mExpectedException.expect(RestError.class);
        mWebMockRule.enqueue(MockResponseFactory.create500());
        restApiUnderTest.requestInputControls("cookie", "any_id", true);
    }

    @Test
    public void requestInputControlsInitialStatesShouldThrowRestErrorFor500() {
        mExpectedException.expect(RestError.class);
        mWebMockRule.enqueue(MockResponseFactory.create500());
        restApiUnderTest.requestInputControlsInitialStates("cookie", "any_id", true);
    }

    @Test
    public void requestInputControlsStatesShouldThrowRestErrorFor500() {
        mExpectedException.expect(RestError.class);
        mWebMockRule.enqueue(MockResponseFactory.create500());
        restApiUnderTest.requestInputControlsStates("cookie", "any_id", Collections.EMPTY_MAP, true);
    }

    @Test
    public void apiShouldProvideListOfInputControlsInitialStatesWithFreshData() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(icsStates.asString());
        mWebMockRule.enqueue(mockResponse);

        Collection<InputControlState> states = restApiUnderTest.requestInputControlsInitialStates("cookie", "/my/uri", true);
        assertThat(states, is(not(empty())));

        RecordedRequest response = mWebMockRule.get().takeRequest();
        assertThat(response.getPath(), is("/rest_v2/reports/my/uri/inputControls/values?freshData=true"));
        assertThat(response.getHeader("Cookie"), is("cookie"));
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

        Collection<InputControlState> states = restApiUnderTest.requestInputControlsStates("cookie", "/my/uri", parameters, true);
        assertThat(states, Matchers.is(not(Matchers.empty())));

        RecordedRequest response = mWebMockRule.get().takeRequest();
        assertThat(response.getPath(), is("/rest_v2/reports/my/uri/inputControls/sales_fact_ALL__store_sales_2013_1/values?freshData=true"));
        assertThat(response.getHeader("Cookie"), is("cookie"));
    }

    @Test
    public void apiShouldProvideInputControlsListIfStateExcluded() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(icsWithoutStates.asString());
        mWebMockRule.enqueue(mockResponse);

        Collection<InputControl> controls = restApiUnderTest.requestInputControls("cookie", "/my/uri", true);
        assertThat(controls, Matchers.is(not(Matchers.empty())));
        assertThat(new ArrayList<>(controls).get(0).getState(), is(nullValue()));

        RecordedRequest response = mWebMockRule.get().takeRequest();
        assertThat(response.getPath(), is("/rest_v2/reports/my/uri/inputControls?exclude=state"));
        assertThat(response.getHeader("Cookie"), is("cookie"));
    }

    @Test
    public void apiShouldProvideInputControlsWithStates() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(icsWithStates.asString());
        mWebMockRule.enqueue(mockResponse);

        Collection<InputControl> controls = restApiUnderTest.requestInputControls("cookie", "/my/uri", false);
        assertThat(controls, Matchers.is(not(Matchers.empty())));
        assertThat(new ArrayList<>(controls).get(0).getState(), is(not(nullValue())));

        RecordedRequest response = mWebMockRule.get().takeRequest();
        assertThat(response.getPath(), is("/rest_v2/reports/my/uri/inputControls"));
        assertThat(response.getHeader("Cookie"), is("cookie"));
    }
}