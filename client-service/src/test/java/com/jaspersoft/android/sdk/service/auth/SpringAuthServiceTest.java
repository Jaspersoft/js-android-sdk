package com.jaspersoft.android.sdk.service.auth;

import com.jaspersoft.android.sdk.network.api.AuthenticationRestApi;
import com.jaspersoft.android.sdk.network.api.JSEncryptionAlgorithm;
import com.jaspersoft.android.sdk.network.entity.server.AuthResponse;
import com.jaspersoft.android.sdk.network.entity.server.EncryptionKey;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AuthResponse.class, EncryptionKey.class, JSEncryptionAlgorithm.class})
public class SpringAuthServiceTest {

    @Mock
    AuthenticationRestApi mRestApi;
    @Mock
    AuthResponse mAuthResponse;
    @Mock
    JSEncryptionAlgorithm mAlgorithm;
    @Mock
    EncryptionKey mKey;

    private SpringAuthService objectUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectUnderTest = new SpringAuthService(
                mAlgorithm,
                mRestApi,
                "user",
                "1234",
                "organization"
        );

        when(mRestApi.requestEncryptionMetadata()).thenReturn(mKey);
        when(mRestApi.authenticate(anyString(), anyString(), anyString(), anyMap())).thenReturn(mAuthResponse);
    }

    @Test
    public void shouldAuthenticateWithHashedPasswordIfEncryptionKeyIsMissing()  {
        when(mKey.isAvailable()).thenReturn(true);
        when(mKey.getExponent()).thenReturn("e");
        when(mKey.getModulus()).thenReturn("m");
        when(mAlgorithm.encrypt(anyString(), anyString(), anyString())).thenReturn("hashed password");

        objectUnderTest.authenticate().subscribe();

        verify(mRestApi, times(1)).authenticate("user", "hashed password", "organization", null);
        verify(mRestApi, times(1)).requestEncryptionMetadata();
        verify(mAlgorithm, times(1)).encrypt("m", "e", "1234");
    }

    @Test
    public void shouldAuthenticateWithOpenPasswordIfEncryptionKeyIsMissing()  {
        when(mKey.isAvailable()).thenReturn(false);

        objectUnderTest.authenticate().subscribe();

        verify(mRestApi, times(1)).authenticate("user", "1234", "organization", null);
        verify(mRestApi, times(1)).requestEncryptionMetadata();
        verifyZeroInteractions(mAlgorithm);
    }
}