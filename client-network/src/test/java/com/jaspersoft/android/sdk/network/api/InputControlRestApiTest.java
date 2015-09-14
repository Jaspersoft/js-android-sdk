package com.jaspersoft.android.sdk.network.api;

import com.jaspersoft.android.sdk.network.api.auth.Token;
import com.jaspersoft.android.sdk.test.MockResponseFactory;
import com.jaspersoft.android.sdk.test.WebMockRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

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

    @Mock
    Token<?> mToken;

    private InputControlRestApi restApiUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        restApiUnderTest = new InputControlRestApi.Builder()
                .setToken(mToken)
                .baseUrl(mWebMockRule.getRootUrl())
                .build();
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
    public void requestInputControlsShouldThrowRestErrorFor500() {
        mExpectedException.expect(RestError.class);
        mWebMockRule.enqueue(MockResponseFactory.create500());
        restApiUnderTest.requestInputControls("any_id", true);
    }
    @Test
    public void requestInputControlsInitialStatesShouldThrowRestErrorFor500() {
        mExpectedException.expect(RestError.class);
        mWebMockRule.enqueue(MockResponseFactory.create500());
        restApiUnderTest.requestInputControlsInitialStates("any_id", true);
    }

    @Test
    public void requestInputControlsStatesShouldThrowRestErrorFor500() {
        mExpectedException.expect(RestError.class);
        mWebMockRule.enqueue(MockResponseFactory.create500());
        restApiUnderTest.requestInputControlsStates("any_id", Collections.EMPTY_MAP, true);
    }
}