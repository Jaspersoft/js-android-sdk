package com.jaspersoft.android.sdk.service;

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

    /**
     * TODO: Replace with abstract ASYNC policy
     */
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
