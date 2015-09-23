package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.api.ServerRestApi;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class RepositoryService {
    private final RepositoryRestApi.Factory mRepositoryApiFactory;
    private final ServerRestApi.Factory mInfoApiFactory;

    public RepositoryService(RepositoryRestApi.Factory repositoryApiFactory,
                             ServerRestApi.Factory infoApiFactory) {
        mRepositoryApiFactory = repositoryApiFactory;
        mInfoApiFactory = infoApiFactory;
    }

    public SearchTask search(SearchCriteria criteria) {
        return new SearchTask(criteria, mRepositoryApiFactory, mInfoApiFactory);
    }
}
