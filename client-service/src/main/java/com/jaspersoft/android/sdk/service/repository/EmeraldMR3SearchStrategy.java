package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;

import rx.Observable;

final class EmeraldMR3SearchStrategy implements SearchStrategy {
    public EmeraldMR3SearchStrategy(RepositoryRestApi.Factory repositoryApiFactory, SearchCriteria criteria) {

    }

    @Override
    public Observable<SearchResult> search() {
        return null;
    }
}
