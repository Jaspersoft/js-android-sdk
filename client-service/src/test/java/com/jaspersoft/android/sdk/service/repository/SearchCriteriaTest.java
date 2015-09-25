/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
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

import java.util.HashMap;
import java.util.Map;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class SearchCriteriaTest {

    @Test
    public void shouldIncludeCountInParams() {
        SearchCriteria criteria = SearchCriteria.builder()
                .limit(101)
                .create();

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("limit", "101");

        assertThat(criteria.toMap(), is(resultMap));
    }

    @Test
    public void shouldIncludeOffsetInParams() {
        SearchCriteria criteria = SearchCriteria.builder()
                .offset(100)
                .create();

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("offset", "100");

        assertThat(criteria.toMap(), is(resultMap));
    }

    @Test
    public void shouldIncludeRecursiveInParams() {
        SearchCriteria criteria = SearchCriteria.builder()
                .recursive(true)
                .create();

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("recursive", "true");

        assertThat(criteria.toMap(), is(resultMap));
    }

    @Test
    public void shouldIncludeForceFullPageInParams() {
        SearchCriteria criteria = SearchCriteria.builder()
                .forceFullPage(true)
                .create();

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("forceFullPage", "true");

        assertThat(criteria.toMap(), is(resultMap));
    }

    @Test
    public void shouldIncludeForceTotalCountPageInParams() {
        SearchCriteria criteria = SearchCriteria.builder()
                .forceTotalCount(true)
                .create();

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("forceTotalCount", "true");

        assertThat(criteria.toMap(), is(resultMap));
    }

    @Test
    public void shouldIncludeQueryPageInParams() {
        SearchCriteria criteria = SearchCriteria.builder()
                .query("any")
                .create();

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("q", "any");

        assertThat(criteria.toMap(), is(resultMap));
    }

    @Test
    public void shouldIncludeSortByLabelInParams() {
        SearchCriteria criteria = SearchCriteria.builder()
                .sortByLabel()
                .create();

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("sortBy", "label");

        assertThat(criteria.toMap(), is(resultMap));
    }

    @Test
    public void shouldIncludeSortByCreationDateInParams() {
        SearchCriteria criteria = SearchCriteria.builder()
                .sortByCreationDate()
                .create();

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("sortBy", "creationDate");

        assertThat(criteria.toMap(), is(resultMap));
    }

    @Test
    public void shouldIncludeFolderUriInParams() {
        SearchCriteria criteria = SearchCriteria.builder()
                .folderUri("/")
                .create();

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("folderUri", "/");

        assertThat(criteria.toMap(), is(resultMap));
    }

    @Test
    public void shouldIgnoreEmptyQuery() {
        SearchCriteria criteria = SearchCriteria.builder()
                .query("")
                .create();

        Map<String, String> emptytMap = new HashMap<>();
        assertThat(criteria.toMap(), is(emptytMap));
    }

    @Test
    @Parameters({
            "REPORT|reportUnit",
            "DASHBOARD|dashboard",
            "LEGACY_DASHBOARD|legacyDashboard",
            "ALL|reportUnit:dashboard:legacyDashboard",
    })
    public void criteriaShouldIncludeTypeInParams(String flag, String types) throws Exception {
        Integer resource = (Integer) SearchCriteria.class.getField(flag).get(null);

        SearchCriteria criteria = SearchCriteria.builder()
                .resourceMask(resource)
                .create();

        Map<String, String> resultMap = new HashMap<>();
        if (types.contains(":")) {
            for (String type : types.split(":")) {
                resultMap.put("type", type);
            }
        } else {
            resultMap.put("type", types);
        }


        assertThat(criteria.toMap(), is(resultMap));
    }

    @Test
    public void shouldReturnEmptyParamsIfNoSupplied() {
        SearchCriteria criteria = SearchCriteria.builder().create();
        Map<String, String> resultMap = new HashMap<>();
        assertThat(criteria.toMap(), is(resultMap));
    }

    @Test
    public void newBuilderShouldCopyCount() {
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .limit(100)
                .create();
        SearchCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getLimit(), is(100));
    }

    @Test
    public void newBuilderShouldCopyOffset() {
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .offset(100)
                .create();
        SearchCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getOffset(), is(100));
    }

    @Test
    public void newBuilderShouldCopyForceFullPageFlag() {
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .forceFullPage(true)
                .create();
        SearchCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getForceFullPage(), is(true));
    }

    @Test
    public void newBuilderShouldCopyQuery() {
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .query("q")
                .create();
        SearchCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getQuery(), is("q"));
    }

    @Test
    public void newBuilderShouldCopyRecursiveFlag() {
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .recursive(true)
                .create();
        SearchCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getRecursive(), is(true));
    }

    @Test
    public void newBuilderShouldCopySortBy() {
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .sortByCreationDate()
                .create();
        SearchCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getSortBy(), is("creationDate"));
    }

    @Test
    public void newBuilderShouldCopyResourceMask() {
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .resourceMask(SearchCriteria.REPORT | SearchCriteria.DASHBOARD)
                .create();
        SearchCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getResourceMask(), is(SearchCriteria.REPORT | SearchCriteria.DASHBOARD));
    }

    @Test
    public void newBuilderShouldCopyForceTotalPageFlag() {
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .forceTotalCount(true)
                .create();
        SearchCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getForceTotalCount(), is(true));
    }

    @Test
    public void newBuilderShouldCopyFolderUri() {
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .folderUri("/")
                .create();
        SearchCriteria newCriteria = searchCriteria.newBuilder().create();
        assertThat(newCriteria.getFolderUri(), is("/"));
    }
}