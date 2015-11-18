package com.jaspersoft.android.sdk.service.auth;

import com.jaspersoft.android.sdk.network.AuthenticationRestApi;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Tom Koptel
 */
public class JrsAuthenticatorTest {

    @Mock
    AuthPolicy mAuthPolicy;
    @Mock
    Credentials mCredentials;

    private JrsAuthenticator authenticatorUnderTest;

    @Rule
    public ExpectedException mExpectedException = ExpectedException.none();

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        authenticatorUnderTest = new JrsAuthenticator(mAuthPolicy);
    }

    @Test
    public void testAuthenticate() throws Exception {
        authenticatorUnderTest.authenticate(mCredentials);

        verify(mCredentials).applyPolicy(mAuthPolicy);
        verifyNoMoreInteractions(mCredentials);
        verifyNoMoreInteractions(mAuthPolicy);
    }

    @Test
    public void factoryMethodShouldNotAcceptNullBaseUrl() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Base url should not be null");

        String baseUrl = null;
        JrsAuthenticator.create(baseUrl);
    }

    @Test
    public void factoryMethodShouldNotAcceptNullApi() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Authentication API should not be null");

        AuthenticationRestApi restApi = null;
        JrsAuthenticator.create(restApi);
    }

    @Test
    public void authenticateShouldNotAcceptNullCredentials() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Credentials should not be null");

        authenticatorUnderTest.authenticate(null);
    }
}