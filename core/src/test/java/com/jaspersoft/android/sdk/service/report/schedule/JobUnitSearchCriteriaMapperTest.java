/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobOwner;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JobUnitSearchCriteriaMapperTest {

    private JobSearchCriteriaMapper mCriteriaMapper;

    @Before
    public void setUp() throws Exception {
        mCriteriaMapper = new JobSearchCriteriaMapper();
    }

    @Test
    public void should_map_report_uri() throws Exception {
        JobSearchCriteria criteria = JobSearchCriteria.builder()
                .withReportUri("/my/uri")
                .build();

        Map<String, Object> params = performMap(criteria);
        assertThat(params.containsKey("reportUnitURI"), is(true));
        assertThat(String.valueOf(params.get("reportUnitURI")), is("/my/uri"));
    }

    @Test
    public void should_map_owner() throws Exception {
        JobSearchCriteria criteria = JobSearchCriteria.builder()
                .withOwner(JobOwner.newOwner("jasperadmin", "organization_1"))
                .build();

        Map<String, Object> params = performMap(criteria);
        assertThat(params.containsKey("owner"), is(true));
        assertThat(String.valueOf(params.get("owner")), is("jasperadmin|organization_1"));
    }

    @Test
    public void should_map_label() throws Exception {
        JobSearchCriteria criteria = JobSearchCriteria.builder()
                .withLabel("my label")
                .build();
        Map<String, Object> params = performMap(criteria);
        assertThat(params.containsKey("label"), is(true));
        assertThat(String.valueOf(params.get("label")), is("my label"));
    }

    @Test
    public void should_map_start_index() throws Exception {
        JobSearchCriteria criteria = JobSearchCriteria.builder()
                .withOffset(100)
                .build();
        Map<String, Object> params = performMap(criteria);
        assertThat(params.containsKey("startIndex"), is(true));
        assertThat(String.valueOf(params.get("startIndex")), is("100"));
    }

    @Test
    public void should_map_number_of_rows() throws Exception {
        JobSearchCriteria criteria = JobSearchCriteria.builder()
                .withLimit(50)
                .build();
        Map<String, Object> params = performMap(criteria);
        assertThat(params.containsKey("numberOfRows"), is(true));
        assertThat(String.valueOf(params.get("numberOfRows")), is("50"));
    }

    @Test
    public void should_map_sort_type() throws Exception {
        JobSearchCriteria criteria = JobSearchCriteria.builder()
                .withSortType(JobSortType.SORTBY_JOBID)
                .build();
        Map<String, Object> params = performMap(criteria);
        assertThat(params.containsKey("sortType"), is(true));
        assertThat(String.valueOf(params.get("sortType")), is("SORTBY_JOBID"));
    }

    @Test
    public void should_map_ascending_flag() throws Exception {
        JobSearchCriteria criteria = JobSearchCriteria.builder()
                .withAscending(true)
                .build();
        Map<String, Object> params = performMap(criteria);
        assertThat(params.containsKey("isAscending"), is(true));
        assertThat(String.valueOf(params.get("isAscending")), is("true"));
    }

    @Test
    public void should_map_to_negative_if_unlimited() throws Exception {
        JobSearchCriteria criteria = JobSearchCriteria.builder()
                .withLimit(JobSearchCriteria.UNLIMITED_ROW_NUMBER)
                .build();
        Map<String, Object> params = performMap(criteria);
        assertThat(params.containsKey("numberOfRows"), is(true));
        assertThat(String.valueOf(params.get("numberOfRows")), is("-1"));
    }

    private Map<String, Object> performMap(JobSearchCriteria criteria) {
        return mCriteriaMapper.transform(criteria);
    }
}