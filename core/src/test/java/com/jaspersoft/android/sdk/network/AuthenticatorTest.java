package com.jaspersoft.android.sdk.network;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Tom Koptel
 */
public class AuthenticatorTest {

    @Mock
    AuthPolicy mAuthPolicy;
    @Mock
    Credentials mCredentials;

    private Authenticator authenticatorUnderTest;

    @Rule
    public ExpectedException mExpectedException = ExpectedException.none();
    private final Cookies fakeCookies = Cookies.parse("key=value");

    @Before
    public void setupMocks() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mCredentials.applyPolicy(any(AuthPolicy.class))).thenReturn(fakeCookies);
        authenticatorUnderTest = new Authenticator(mAuthPolicy);
    }

    @Test
    public void testAuthenticate() throws Exception {
        authenticatorUnderTest.authenticate(mCredentials);

        verify(mCredentials).applyPolicy(mAuthPolicy);
        verifyNoMoreInteractions(mCredentials);
        verifyNoMoreInteractions(mAuthPolicy);
    }
}