package com.jaspersoft.android.sdk.service.auth;

import org.junit.Before;
import org.junit.Test;
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

    private JrsAuthenticator mJrsAuthenticator;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        mJrsAuthenticator = new JrsAuthenticator(mAuthPolicy);
    }

    @Test
    public void testAuthenticate() throws Exception {
        mJrsAuthenticator.authenticate(mCredentials);

        verify(mCredentials).applyPolicy(mAuthPolicy);
        verifyNoMoreInteractions(mCredentials);
        verifyNoMoreInteractions(mAuthPolicy);
    }
}