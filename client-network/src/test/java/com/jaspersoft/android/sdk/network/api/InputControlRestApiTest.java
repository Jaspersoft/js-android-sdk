package com.jaspersoft.android.sdk.network.api;

import com.jaspersoft.android.sdk.network.entity.control.InputControlResponse;
import com.jaspersoft.android.sdk.network.exception.RestError;
import com.jaspersoft.android.sdk.test.MockResponseFactory;
import com.jaspersoft.android.sdk.test.WebMockRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import rx.Observable;

/**
 * TODO implement tests
 *
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
    public void requestInputControlsShouldNotAllowNullReportUri1() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report URI should not be null");
        restApiUnderTest.requestInputControls(null);
    }

    @Test
    public void requestInputControlsShouldNotAllowNullReportUri2() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report URI should not be null");
        restApiUnderTest.requestInputControls(null, false);
    }

    @Test
    public void requestInputControlsInitialStatesShouldNotAllowNullReportUri1() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report URI should not be null");
        restApiUnderTest.requestInputControlsInitialStates(null);
    }

    @Test
    public void requestInputControlsInitialStatesShouldNotAllowNullReportUri2() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report URI should not be null");
        restApiUnderTest.requestInputControlsInitialStates(null, false);
    }

    @Test
    public void requestInputControlsShouldThrowRestError() {
        mExpectedException.expect(RestError.class);
        mWebMockRule.enqueue(MockResponseFactory.create500());
        Observable<InputControlResponse> call = restApiUnderTest.requestInputControls("any_id");
        call.toBlocking().first();
    }

}