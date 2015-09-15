package com.jaspersoft.android.sdk.service.server;

import com.jaspersoft.android.sdk.network.api.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.server.ServerInfoResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ServerInfoResponse.class})
public class ServerInfoServiceTest {

    @Mock
    ServerRestApi mockApi;
    @Mock
    ServerInfoTransformer mockTransformer;
    @Mock
    ServerInfoResponse mockResponse;

    private ServerInfoService serviceUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        serviceUnderTest = new ServerInfoService(mockApi, mockTransformer);
    }

    @Test
    public void requestInfoShouldProvideServerInfoDataObject() {
        when(mockApi.requestServerInfo()).thenReturn(mockResponse);

        serviceUnderTest.requestServerInfo().subscribe();

        verify(mockTransformer, times(1)).transform(mockResponse);
        verify(mockApi, times(1)).requestServerInfo();
    }
}