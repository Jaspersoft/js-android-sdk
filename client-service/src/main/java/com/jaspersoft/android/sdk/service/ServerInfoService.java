package com.jaspersoft.android.sdk.service;

import com.jaspersoft.android.sdk.network.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.server.ServerInfoData;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;

import org.jetbrains.annotations.TestOnly;

import java.text.SimpleDateFormat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ServerInfoService {
    private final ServerRestApi mRestApi;
    private final ServerInfoTransformer mTransformer;

    @TestOnly
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
        ServerInfoData response = mRestApi.requestServerInfo();
        return mTransformer.transform(response);
    }

    public ServerVersion requestServerVersion() {
        String version = mRestApi.requestVersion();
        return ServerVersion.defaultParser().parse(version);
    }

    public SimpleDateFormat requestServerDateTimeFormat() {
        String dateTimeFormat = mRestApi.requestDateTimeFormatPattern();
        return new SimpleDateFormat(dateTimeFormat);
    }
}
