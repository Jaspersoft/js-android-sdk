package com.jaspersoft.android.sdk.service.repository;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(JUnitParamsRunner.class)
public class SearchCriteriaTest {

    @Test
    public void shouldIncludeCountInParams() {
        SearchCriteria criteria = SearchCriteria.builder()
                .limitCount(100)
                .create();

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("limit", "100");

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
}