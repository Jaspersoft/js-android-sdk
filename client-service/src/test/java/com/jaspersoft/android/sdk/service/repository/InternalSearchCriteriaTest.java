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
public class InternalSearchCriteriaTest {

    @Test
    public void newBuilderShouldCopyCount() {
        InternalCriteria searchCriteria = InternalCriteria.builder()
                .limit(100)
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getLimit(), is(100));
    }

    @Test
    public void newBuilderShouldCopyOffset() {
        InternalCriteria searchCriteria = InternalCriteria.builder()
                .offset(100)
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getOffset(), is(100));
    }

    @Test
    public void newBuilderShouldCopyForceFullPageFlag() {
        InternalCriteria searchCriteria = InternalCriteria.builder()
                .forceFullPage(true)
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getForceFullPage(), is(true));
    }

    @Test
    public void newBuilderShouldCopyQuery() {
        InternalCriteria searchCriteria = InternalCriteria.builder()
                .query("q")
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getQuery(), is("q"));
    }

    @Test
    public void newBuilderShouldCopyRecursiveFlag() {
        InternalCriteria searchCriteria = InternalCriteria.builder()
                .recursive(true)
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getRecursive(), is(true));
    }

    @Test
    public void newBuilderShouldCopySortBy() {
        InternalCriteria searchCriteria = InternalCriteria.builder()
                .sortBy("creationDate")
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getSortBy(), is("creationDate"));
    }

    @Test
    public void newBuilderShouldCopyResourceMask() {
        InternalCriteria searchCriteria = InternalCriteria.builder()
                .resourceMask(SearchCriteria.REPORT | SearchCriteria.DASHBOARD)
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getResourceMask(), is(SearchCriteria.REPORT | SearchCriteria.DASHBOARD));
    }

    @Test
    public void newBuilderShouldCopyForceTotalPageFlag() {
        InternalCriteria searchCriteria = InternalCriteria.builder()
                .forceTotalCount(true)
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getForceTotalCount(), is(true));
    }

    @Test
    public void newBuilderShouldCopyFolderUri() {
        InternalCriteria searchCriteria = InternalCriteria.builder()
                .folderUri("/")
                .create();
        InternalCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getFolderUri(), is("/"));
    }

    @Test
    public void shouldAdaptFromUserCriteriaFieldLimit() {
        SearchCriteria searchCriteria = SearchCriteria.builder().limit(1).create();
        InternalCriteria criteria = InternalCriteria.from(searchCriteria);
        assertThat(criteria.getLimit(), is(1));
    }

    @Test
    public void shouldAdaptFromUserCriteriaFieldOffset() {
        SearchCriteria searchCriteria = SearchCriteria.builder().offset(2).create();
        InternalCriteria criteria = InternalCriteria.from(searchCriteria);
        assertThat(criteria.getOffset(), is(2));
    }

    @Test
    public void shouldAdaptFromUserCriteriaFieldResourceMask() {
        SearchCriteria searchCriteria = SearchCriteria.builder().resourceMask(SearchCriteria.REPORT).create();
        InternalCriteria criteria = InternalCriteria.from(searchCriteria);
        assertThat(criteria.getResourceMask(), is(SearchCriteria.REPORT));
    }

    @Test
    public void shouldAdaptFromUserCriteriaFieldRecursive() {
        SearchCriteria searchCriteria = SearchCriteria.builder().recursive(true).create();
        InternalCriteria criteria = InternalCriteria.from(searchCriteria);
        assertThat(criteria.getRecursive(), is(true));
    }

    @Test
    public void shouldAdaptFromUserCriteriaFieldFolderUri() {
        SearchCriteria searchCriteria = SearchCriteria.builder().folderUri("/").create();
        InternalCriteria criteria = InternalCriteria.from(searchCriteria);
        assertThat(criteria.getFolderUri(), is("/"));
    }

    @Test
    public void shouldAdaptFromUserCriteriaFieldQuery() {
        SearchCriteria searchCriteria = SearchCriteria.builder().query("query").create();
        InternalCriteria criteria = InternalCriteria.from(searchCriteria);
        assertThat(criteria.getQuery(), is("query"));
    }

    @Test
    public void shouldAdaptFromUserCriteriaFieldSortBy() {
        SearchCriteria searchCriteria = SearchCriteria.builder().sortByCreationDate().create();
        InternalCriteria criteria = InternalCriteria.from(searchCriteria);
        assertThat(criteria.getSortBy(), is("creationDate"));
    }
}