package com.jaspersoft.android.sdk.service;

import android.support.annotation.VisibleForTesting;

import com.jaspersoft.android.sdk.network.api.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.server.ServerInfoResponse;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ServerInfoService {
    private final ServerRestApi mRestApi;
    private final ServerInfoTransformer mTransformer;

    @VisibleForTesting
    ServerInfoService(ServerRestApi restApi, ServerInfoTransformer transformer) {
        mRestApi = restApi;
        mTransformer = transformer;
    }

    public static ServerInfoService newInstance(ServerRestApi restApi) {
        return new ServerInfoService(restApi, ServerInfoTransformer.getInstance());
    }

    public static ServerInfoService newInstance(String baseUrl) {
        ServerRestApi restApi = new ServerRestApi.Builder()
                .baseUrl(baseUrl)
                .build();

        return new ServerInfoService(restApi, ServerInfoTransformer.getInstance());
    }

    public ServerInfo requestServerInfo() {
        ServerInfoResponse response = mRestApi.requestServerInfo();
        return mTransformer.transform(response);
    }
}
