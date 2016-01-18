/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.repository;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class InternalRepositorySearchCriteriaTest {

    @Test
    public void newBuilderShouldCopyCount() {
        InternalCriteria searchCriteria = new InternalCriteria.Builder()
                .limit(100)
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getLimit(), is(100));
    }

    @Test
    public void newBuilderShouldCopyOffset() {
        InternalCriteria searchCriteria = new InternalCriteria.Builder()
                .offset(100)
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getOffset(), is(100));
    }

    @Test
    public void newBuilderShouldCopyForceFullPageFlag() {
        InternalCriteria searchCriteria = new InternalCriteria.Builder()
                .forceFullPage(true)
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getForceFullPage(), is(true));
    }

    @Test
    public void newBuilderShouldCopyQuery() {
        InternalCriteria searchCriteria = new InternalCriteria.Builder()
                .query("q")
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getQuery(), is("q"));
    }

    @Test
    public void newBuilderShouldCopyRecursiveFlag() {
        InternalCriteria searchCriteria = new InternalCriteria.Builder()
                .recursive(true)
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getRecursive(), is(true));
    }

    @Test
    public void newBuilderShouldCopySortBy() {
        InternalCriteria searchCriteria = new InternalCriteria.Builder()
                .sortBy(SortType.CREATION_DATE)
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getSortBy(), is(SortType.CREATION_DATE));
    }

    @Test
    public void newBuilderShouldCopyResourceMask() {
        InternalCriteria searchCriteria = new InternalCriteria.Builder()
                .resourceMask(RepositorySearchCriteria.REPORT | RepositorySearchCriteria.DASHBOARD)
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getResourceMask(), is(RepositorySearchCriteria.REPORT | RepositorySearchCriteria.DASHBOARD));
    }

    @Test
    public void newBuilderShouldCopyForceTotalPageFlag() {
        InternalCriteria searchCriteria = new InternalCriteria.Builder()
                .forceTotalCount(true)
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getForceTotalCount(), is(true));
    }

    @Test
    public void newBuilderShouldCopyFolderUri() {
        InternalCriteria searchCriteria = new InternalCriteria.Builder()
                .folderUri("/")
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getFolderUri(), is("/"));
    }

    @Test
    public void shouldAdaptFromUserCriteriaFieldLimit() {
        RepositorySearchCriteria repositorySearchCriteria = RepositorySearchCriteria.builder().withLimit(1).build();
        InternalCriteria criteria = InternalCriteria.from(repositorySearchCriteria);
        assertThat(criteria.getLimit(), is(1));
    }

    @Test
    public void shouldAdaptFromUserCriteriaFieldOffset() {
        RepositorySearchCriteria repositorySearchCriteria = RepositorySearchCriteria.builder().withOffset(2).build();
        InternalCriteria criteria = InternalCriteria.from(repositorySearchCriteria);
        assertThat(criteria.getOffset(), is(2));
    }

    @Test
    public void shouldAdaptFromUserCriteriaFieldResourceMask() {
        RepositorySearchCriteria repositorySearchCriteria = RepositorySearchCriteria.builder().withResourceMask(RepositorySearchCriteria.REPORT).build();
        InternalCriteria criteria = InternalCriteria.from(repositorySearchCriteria);
        assertThat(criteria.getResourceMask(), is(RepositorySearchCriteria.REPORT));
    }

    @Test
    public void shouldAdaptFromUserCriteriaFieldRecursive() {
        RepositorySearchCriteria repositorySearchCriteria = RepositorySearchCriteria.builder().withRecursive(true).build();
        InternalCriteria criteria = InternalCriteria.from(repositorySearchCriteria);
        assertThat(criteria.getRecursive(), is(true));
    }

    @Test
    public void shouldAdaptFromUserCriteriaFieldFolderUri() {
        RepositorySearchCriteria repositorySearchCriteria = RepositorySearchCriteria.builder().withFolderUri("/").build();
        InternalCriteria criteria = InternalCriteria.from(repositorySearchCriteria);
        assertThat(criteria.getFolderUri(), is("/"));
    }

    @Test
    public void shouldAdaptFromUserCriteriaFieldQuery() {
        RepositorySearchCriteria repositorySearchCriteria = RepositorySearchCriteria.builder().withQuery("query").build();
        InternalCriteria criteria = InternalCriteria.from(repositorySearchCriteria);
        assertThat(criteria.getQuery(), is("query"));
    }

    @Test
    public void shouldAdaptFromUserCriteriaFieldSortBy() {
        RepositorySearchCriteria repositorySearchCriteria = RepositorySearchCriteria.builder().withSortType(SortType.CREATION_DATE).build();
        InternalCriteria criteria = InternalCriteria.from(repositorySearchCriteria);
        assertThat(criteria.getSortBy(), is(SortType.CREATION_DATE));
    }
}