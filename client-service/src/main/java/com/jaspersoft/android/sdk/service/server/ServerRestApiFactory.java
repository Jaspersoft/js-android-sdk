package com.jaspersoft.android.sdk.service.server;

import com.jaspersoft.android.sdk.network.api.ServerRestApi;

public final class ServerRestApiFactory implements ServerRestApi.Factory {
    private final String mServerUrl;

    public ServerRestApiFactory(String serverUrl) {
        mServerUrl = serverUrl;
    }

    @Override
    public ServerRestApi get() {
        return new ServerRestApi.Builder()
                .baseUrl(mServerUrl)
                .build();
    }
}
