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

import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(JUnitParamsRunner.class)
public class JobSearchTaskFactoryTest {
    @Mock
    ReportScheduleUseCase mUseCase;
    private JobSearchTaskFactory factoryUnderTest;
    private JobSearchCriteria searchCriteria;
    private JobSearchTask searchTask;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    @Parameters({
            "5.5", "6.1.1"
    })
    public void should_use_legacy_setup_for_jrs_lower_than(String version) throws Exception {
        givenCriteriaWithLabel();
        givenTaskFactory();

        whenCreatesSearchTask(version);

        thenShouldCreateTaskOfType(MemoryFilterSearchTask.class);
    }

    @Test
    @Parameters({
            "5.5", "6.1.1"
    })
    public void should_not_use_legacy_setup_for_criteria_without_query(String version) throws Exception {
        givenCriteriaWithoutLabel();
        givenTaskFactory();

        whenCreatesSearchTask(version);

        thenShouldCreateTaskOfType(RestFilterSearchTask.class);
    }

    @Test
    @Parameters({
            "6.2", "6.3"
    })
    public void should_use_query_capable_setup_for_jrs_greater_than(String version) throws Exception {
        givenCriteriaWithLabel();
        givenTaskFactory();

        whenCreatesSearchTask(version);

        thenShouldCreateTaskOfType(RestFilterSearchTask.class);
    }


    private void thenShouldCreateTaskOfType(Class<? extends JobSearchTask> type) {
        assertThat(searchTask, is(instanceOf(type)));
    }

    private void whenCreatesSearchTask(String version) {
        searchTask = factoryUnderTest.create(ServerVersion.valueOf(version));
    }

    private void givenCriteriaWithLabel() {
        searchCriteria = JobSearchCriteria.builder()
                .withLabel("label")
                .build();
    }

    private void givenCriteriaWithoutLabel() {
        searchCriteria = JobSearchCriteria.builder().build();
    }

    private void givenTaskFactory() {
        factoryUnderTest = new JobSearchTaskFactory(mUseCase, searchCriteria);
    }
}