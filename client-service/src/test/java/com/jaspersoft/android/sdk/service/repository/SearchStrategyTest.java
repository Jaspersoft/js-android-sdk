package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public final class SearchStrategyTest {

    @Mock
    RepositoryRestApi.Factory mFactory;
    SearchCriteria mSearchCriteria = SearchCriteria.builder().create();

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters({
            "5.0",
            "5.5",
    })
    public void factoryCreatesEmeraldMR2Strategy(String version) {
        SearchStrategy searchStrategy = SearchStrategy.Factory.get(version, mFactory, mSearchCriteria);
        assertThat(searchStrategy, instanceOf(EmeraldMR2SearchStrategy.class));
    }

    @Test
    @Parameters({
            "6.0",
            "6.0.1",
    })
    public void factoryCreatesEmeraldMR3Strategy(String version) {
        SearchStrategy searchStrategy = SearchStrategy.Factory.get(version, mFactory, mSearchCriteria);
        assertThat(searchStrategy, instanceOf(EmeraldMR3SearchStrategy.class));
    }
}
