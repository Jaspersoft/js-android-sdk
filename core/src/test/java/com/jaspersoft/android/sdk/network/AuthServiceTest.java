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
public class AuthServiceTest {

    @Mock
    AuthStrategy mAuthStrategy;
    @Mock
    Credentials mCredentials;

    private AuthService mAuthServiceUnderTest;

    @Rule
    public ExpectedException mExpectedException = ExpectedException.none();
    private final Cookies fakeCookies = Cookies.parse("key=value");

    @Before
    public void setupMocks() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mCredentials.applyPolicy(any(AuthStrategy.class))).thenReturn(fakeCookies);
        mAuthServiceUnderTest = new AuthService(mAuthStrategy);
    }

    @Test
    public void testAuthenticate() throws Exception {
        mAuthServiceUnderTest.authenticate(mCredentials);

        verify(mCredentials).applyPolicy(mAuthStrategy);
        verifyNoMoreInteractions(mCredentials);
        verifyNoMoreInteractions(mAuthStrategy);
    }
}