package com.jaspersoft.android.sdk.service.auth;

import com.jaspersoft.android.sdk.network.Cookies;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 */
public class AuthenticationServiceTest {

    @Mock
    AuthPolicy mAuthPolicy;
    @Mock
    Credentials mCredentials;

    private AuthenticationService authenticatorUnderTest;

    @Rule
    public ExpectedException mExpectedException = ExpectedException.none();
    private final Cookies fakeCookies = Cookies.parse("key=value");

    @Before
    public void setupMocks() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mCredentials.applyPolicy(any(AuthPolicy.class))).thenReturn(fakeCookies);
        authenticatorUnderTest = new AuthenticationService(mAuthPolicy);
    }

    @Test
    public void testAuthenticate() throws Exception {
        authenticatorUnderTest.authenticate(mCredentials);

        verify(mCredentials).applyPolicy(mAuthPolicy);
        verifyNoMoreInteractions(mCredentials);
        verifyNoMoreInteractions(mAuthPolicy);
    }
}