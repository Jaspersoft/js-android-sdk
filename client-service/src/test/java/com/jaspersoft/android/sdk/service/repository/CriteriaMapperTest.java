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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class CriteriaMapperTest {

    @Test
    public void shouldIncludeCountInParams() {
        InternalCriteria criteria = InternalCriteria.builder()
                .limit(101)
                .create();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("limit", "101");

        assertThat(toMap(criteria), is(resultMap));
    }

    @Test
    public void shouldIncludeOffsetInParams() {
        InternalCriteria criteria = InternalCriteria.builder()
                .offset(100)
                .create();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("offset", "100");

        assertThat(toMap(criteria), is(resultMap));
    }

    @Test
    public void shouldIncludeRecursiveInParams() {
        InternalCriteria criteria = InternalCriteria.builder()
                .recursive(true)
                .create();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("recursive", "true");

        assertThat(toMap(criteria), is(resultMap));
    }

    @Test
    public void shouldIncludeForceFullPageInParams() {
        InternalCriteria criteria = InternalCriteria.builder()
                .forceFullPage(true)
                .create();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("forceFullPage", "true");

        assertThat(toMap(criteria), is(resultMap));
    }

    @Test
    public void shouldIncludeForceTotalCountPageInParams() {
        InternalCriteria criteria = InternalCriteria.builder()
                .forceTotalCount(true)
                .create();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("forceTotalCount", "true");

        assertThat(toMap(criteria), is(resultMap));
    }

    @Test
    public void shouldIncludeQueryPageInParams() {
        InternalCriteria criteria = InternalCriteria.builder()
                .query("any")
                .create();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("q", "any");

        assertThat(toMap(criteria), is(resultMap));
    }

    @Test
    public void shouldIncludeFolderUriInParams() {
        InternalCriteria criteria = InternalCriteria.builder()
                .folderUri("/")
                .create();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("folderUri", "/");

        assertThat(toMap(criteria), is(resultMap));
    }

    @Test
    public void shouldIncludeSortByInParams() {
        InternalCriteria criteria = InternalCriteria.builder()
                .sortBy("description")
                .create();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("sortBy", "description");

        assertThat(toMap(criteria), is(resultMap));
    }

    @Test
    public void shouldIgnoreEmptyQuery() {
        InternalCriteria criteria = InternalCriteria.builder()
                .query("")
                .create();

        Map<String, Object> emptytMap = new HashMap<>();
        assertThat(toMap(criteria), is(emptytMap));
    }

    @Test
    @Parameters({
            "REPORT|reportUnit",
            "DASHBOARD|dashboard",
            "LEGACY_DASHBOARD|legacyDashboard",
            "ALL|reportUnit:dashboard:legacyDashboard",
            "REPORT:DASHBOARD|reportUnit:dashboard",
    })
    public void criteriaShouldIncludeTypeInParams(String flags, String types) throws Exception {
        int mask = 0;
        if (flags.contains(":")) {
            for (String flag : flags.split(":")) {
                mask |= (Integer) SearchCriteria.class.getField(flag).get(null);
            }
        } else {
            mask = (Integer) SearchCriteria.class.getField(flags).get(null);
        }

        InternalCriteria criteria = InternalCriteria.builder()
                .resourceMask(mask)
                .create();

        Map<String, Object> resultMap = new HashMap<>();
        Set<String> typeSet = new HashSet<>();
        if (types.contains(":")) {
            typeSet.addAll(Arrays.asList(types.split(":")));
        } else {
            typeSet.add(types);
        }
        resultMap.put("type", typeSet);

        assertThat(toMap(criteria), is(resultMap));
    }

    @Test
    public void shouldReturnEmptyParamsIfNoSupplied() {
        InternalCriteria criteria = InternalCriteria.builder().create();
        Map<String, Object> resultMap = new HashMap<>();
        assertThat(toMap(criteria), is(resultMap));
    }

    private Map<String, Object> toMap(InternalCriteria criteria) {
        return CriteriaMapper.map(criteria);
    }
}