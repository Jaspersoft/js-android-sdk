package com.jaspersoft.android.sdk.service.server;

import android.support.annotation.VisibleForTesting;

import com.jaspersoft.android.sdk.network.api.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.server.ServerInfoResponse;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ServerService {
    private final ServerRestApi mRestApi;
    private final ServerInfoTransformer mTransformer;

    @VisibleForTesting
    ServerService(ServerRestApi restApi, ServerInfoTransformer transformer) {
        mRestApi = restApi;
        mTransformer = transformer;
    }

    public static ServerService newInstance(ServerRestApi restApi) {
        return new ServerService(restApi, ServerInfoTransformer.getInstance());
    }

    public static ServerService newInstance(String baseUrl) {
        ServerRestApi restApi = new ServerRestApi.Builder()
                .baseUrl(baseUrl)
                .build();

        return new ServerService(restApi, ServerInfoTransformer.getInstance());
    }

    public rx.Observable<ServerInfo> requestServerInfo() {
        return requestApiCall().flatMap(new Func1<ServerInfoResponse, Observable<ServerInfo>>() {
            @Override
            public Observable<ServerInfo> call(ServerInfoResponse response) {
                return Observable.just(mTransformer.transform(response));
            }
        });
    }

    private rx.Observable<ServerInfoResponse> requestApiCall() {
        return Observable.defer(new Func0<Observable<ServerInfoResponse>>() {
            @Override
            public Observable<ServerInfoResponse> call() {
                return Observable.just(mRestApi.requestServerInfo());
            }
        });
    }
}
