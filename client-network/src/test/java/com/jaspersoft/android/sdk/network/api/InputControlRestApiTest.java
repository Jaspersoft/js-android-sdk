package com.jaspersoft.android.sdk.network.api;

import com.jaspersoft.android.sdk.network.entity.control.InputControlResponse;
import com.jaspersoft.android.sdk.network.exception.RestError;
import com.jaspersoft.android.sdk.test.MockResponseFactory;
import com.jaspersoft.android.sdk.test.WebMockRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;

import rx.Observable;

/**
 * @author Tom Koptel
 * @since 2.2
 */
public class InputControlRestApiTest {

    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();

    private InputControlRestApi restApiUnderTest;

    @Before
    public void setup() {
        restApiUnderTest = new InputControlRestApi.Builder(mWebMockRule.getRootUrl(), "cookie").build();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForNullBaseUrl() {
        mExpectedException.expect(IllegalArgumentException.class);
        new InputControlRestApi.Builder(null, "cookie").build();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForNullCookie() {
        mExpectedException.expect(IllegalArgumentException.class);
        InputControlRestApi restApi = new InputControlRestApi.Builder(mWebMockRule.getRootUrl(), null).build();
    }

    @Test
    public void requestInputControlsShouldNotAllowNullReportUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report URI should not be null");
        restApiUnderTest.requestInputControls(null, false);
    }

    @Test
    public void requestInputControlsInitialStatesShouldNotAllowNullReportUri2() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report URI should not be null");
        restApiUnderTest.requestInputControlsInitialStates(null, false);
    }

    @Test
    public void requestInputControlsStatesShouldNotAllowNullReportUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report URI should not be null");
        restApiUnderTest.requestInputControlsStates(null, Collections.EMPTY_MAP, true);
    }

    @Test
    public void requestInputControlsStatesShouldNotAllowNullControlParams() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Controls values should not be null");
        restApiUnderTest.requestInputControlsStates("any_id", null, true);
    }

    @Test
    public void requestInputControlsShouldThrowRestError() {
        mExpectedException.expect(RestError.class);
        mWebMockRule.enqueue(MockResponseFactory.create500());
        Observable<InputControlResponse> call = restApiUnderTest.requestInputControls("any_id", true);
        call.toBlocking().first();
    }

}