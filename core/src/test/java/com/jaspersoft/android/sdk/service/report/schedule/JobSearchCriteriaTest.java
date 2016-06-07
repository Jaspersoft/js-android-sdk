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
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class JobSearchCriteriaTest {
    @Test
    public void should_copy_label_value() throws Exception {
        JobSearchCriteria oldCriteria = JobSearchCriteria.builder().withLabel("label").build();
        JobSearchCriteria newCriteria = oldCriteria.newBuilder().build();
        assertThat(oldCriteria.getLabel(), is(newCriteria.getLabel()));
    }

    @Test
    public void should_copy_offset_value() throws Exception {
        JobSearchCriteria oldCriteria = JobSearchCriteria.builder().withOffset(100).build();
        JobSearchCriteria newCriteria = oldCriteria.newBuilder().build();
        assertThat(oldCriteria.getOffset(), is(newCriteria.getOffset()));
    }

    @Test
    public void should_copy_limit_value() throws Exception {
        JobSearchCriteria oldCriteria = JobSearchCriteria.builder().withLimit(100).build();
        JobSearchCriteria newCriteria = oldCriteria.newBuilder().build();
        assertThat(oldCriteria.getLimit(), is(newCriteria.getLimit()));
    }

    @Test
    public void should_copy_report_uri_value() throws Exception {
        JobSearchCriteria oldCriteria = JobSearchCriteria.builder().withReportUri("/temp").build();
        JobSearchCriteria newCriteria = oldCriteria.newBuilder().build();
        assertThat(oldCriteria.getReportUri(), is(newCriteria.getReportUri()));
    }

    @Test
    public void should_copy_owner_value() throws Exception {
        JobOwner owner = JobOwner.newOwner("foo", "bar");
        JobSearchCriteria oldCriteria = JobSearchCriteria.builder().withOwner(owner).build();
        JobSearchCriteria newCriteria = oldCriteria.newBuilder().build();
        assertThat(oldCriteria.getOwner(), is(newCriteria.getOwner()));
    }

    @Test
    public void should_copy_sort_type_value() throws Exception {
        JobSearchCriteria oldCriteria = JobSearchCriteria.builder().withSortType(JobSortType.SORTBY_JOBID).build();
        JobSearchCriteria newCriteria = oldCriteria.newBuilder().build();
        assertThat(oldCriteria.getSortType(), is(newCriteria.getSortType()));
    }

    @Test
    public void should_copy_order_value() throws Exception {
        JobSearchCriteria oldCriteria = JobSearchCriteria.builder().withAscending(true).build();
        JobSearchCriteria newCriteria = oldCriteria.newBuilder().build();
        assertThat(oldCriteria.getAscending(), is(newCriteria.getAscending()));
    }

    @Test
    public void should_substitute_label_with_null_if_empty() throws Exception {
        JobSearchCriteria criteria = JobSearchCriteria.builder().withLabel("").build();
        assertThat(criteria.getLabel(), is(nullValue()));
    }

    @Test
    public void should_substitute_report_uri_with_null_if_empty() throws Exception {
        JobSearchCriteria criteria = JobSearchCriteria.builder().withReportUri("").build();
        assertThat(criteria.getLabel(), is(nullValue()));
    }
}