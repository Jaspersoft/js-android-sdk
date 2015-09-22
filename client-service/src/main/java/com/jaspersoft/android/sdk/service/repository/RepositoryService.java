package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class RepositoryService {
    private final RepositoryRestApi.Factory mRestApiFactory;

    public RepositoryService(RepositoryRestApi.Factory apiFactory) {
        mRestApiFactory = apiFactory;
    }

    public SearchTask search(SearchCriteria criteria) {
        return new SearchTask(criteria, mRestApiFactory);
    }
}
