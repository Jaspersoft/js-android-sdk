package com.jaspersoft.android.sdk.service;

import com.jaspersoft.android.sdk.network.api.AuthenticationRestApi;
import com.jaspersoft.android.sdk.network.entity.server.AuthResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AuthResponse.class})
public class AuthServiceTest {
    @Mock
    AuthenticationRestApi mRestApi;
    private AuthService objectUnderTest;
    private AuthResponse authResponse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        authResponse = PowerMockito.mock(AuthResponse.class);
        objectUnderTest = new AuthService(mRestApi);
    }

    @Test
    public void testAuthenticate()  {
        when(mRestApi.authenticate(anyString(), anyString(), anyString(), anyMap())).thenReturn(authResponse);
        objectUnderTest.authenticate("user", "1234", "organization").subscribe();
        verify(mRestApi, times(1)).authenticate("user", "1234", "organization", null);
    }
}