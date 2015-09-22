package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.service.TokenProvider;

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
        RepositoryRestApi.Factory apiFactory = new RepositoryRestApiFactory(mServerUrl, mTokenProvider);
        RepositoryService service = new RepositoryService(apiFactory);

        SearchCriteria criteria = new SearchCriteria.Builder()
                .limitCount(10)
                .resourceMask(SearchCriteria.REPORT | SearchCriteria.DASHBOARD)
                .build();
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