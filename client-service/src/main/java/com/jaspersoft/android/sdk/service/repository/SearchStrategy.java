package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;

import rx.Observable;

public interface SearchStrategy {
    Observable<SearchResult> search();

    class Factory {
        public static SearchStrategy get(String serverVersion,
                                         RepositoryRestApi.Factory repositoryApiFactory,
                                         SearchCriteria criteria) {
            ServerVersion version = ServerVersion.defaultParser().parse(serverVersion);
            if (version.getVersionCode() <= ServerVersion.EMERALD_MR2.getVersionCode()) {
                return new EmeraldMR2SearchStrategy(repositoryApiFactory, criteria);
            }
            if (version.getVersionCode() >= ServerVersion.EMERALD_MR3.getVersionCode()) {
                return new EmeraldMR3SearchStrategy(repositoryApiFactory, criteria);
            }
            throw new UnsupportedOperationException("Could not resolve search strategy for serverVersion: " + serverVersion);
        }
    }
}
