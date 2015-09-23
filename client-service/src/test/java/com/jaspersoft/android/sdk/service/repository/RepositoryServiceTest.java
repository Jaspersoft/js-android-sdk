package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.api.ServerRestApi;
import com.jaspersoft.android.sdk.service.TokenProvider;
import com.jaspersoft.android.sdk.service.server.ServerRestApiFactory;

import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class RepositoryServiceTest {
    @Mock
    TokenProvider mTokenProvider;

    String mServerUrl = "http://localhost";

    @Test
    public void shouldProvideListOfResources() {
        RepositoryRestApi.Factory repoApi = new RepositoryRestApiFactory(mServerUrl, mTokenProvider);
        ServerRestApi.Factory infoApi = new ServerRestApiFactory(mServerUrl);
        RepositoryService service = new RepositoryService(repoApi, infoApi);

        SearchCriteria criteria = SearchCriteria.builder()
                .limitCount(10)
                .resourceMask(SearchCriteria.REPORT | SearchCriteria.DASHBOARD)
                .create();
        SearchTask task = service.search(criteria);
        // 10
        task.nextLookup().subscribe();
        // 20
        task.nextLookup().subscribe();
        // 5
        task.nextLookup().subscribe();
        // 0
        task.nextLookup().subscribe();
    }
}