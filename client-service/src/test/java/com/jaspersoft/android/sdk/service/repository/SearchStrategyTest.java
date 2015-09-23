/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

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

/**
 * @author Tom Koptel
 * @since 2.0
 */
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
