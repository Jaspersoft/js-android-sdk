package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;

import rx.Observable;

final class EmeraldMR2SearchStrategy implements SearchStrategy {
    public EmeraldMR2SearchStrategy(RepositoryRestApi.Factory repositoryApiFactory, SearchCriteria criteria) {

    }

    @Override
    public Observable<SearchResult> search() {
        return null;
    }
}
