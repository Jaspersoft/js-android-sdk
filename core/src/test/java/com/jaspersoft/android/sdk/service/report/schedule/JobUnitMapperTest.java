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

import com.jaspersoft.android.sdk.network.entity.schedule.JobStateEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobUnitEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobUnitMapperTest {
    @Mock
    JobUnitEntity mJobUnitEntity;
    @Mock
    JobStateEntity mStateEntity;
    @Mock
    JobUnitDateParser jobUnitDateParser;

    private JobUnitMapper mJobUnitMapper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(mJobUnitEntity.getId()).thenReturn(1);
        when(mJobUnitEntity.getVersion()).thenReturn(100);
        when(mJobUnitEntity.getLabel()).thenReturn("label");
        when(mJobUnitEntity.getDescription()).thenReturn("description");
        when(mJobUnitEntity.getReportUnitURI()).thenReturn("/my/uri");
        when(mJobUnitEntity.getReportLabel()).thenReturn("report label");
        when(mJobUnitEntity.getOwner()).thenReturn("jasperadmin|organization_1");

        when(mJobUnitEntity.getState()).thenReturn(mStateEntity);
        when(mStateEntity.getValue()).thenReturn("NORMAL");

        mJobUnitMapper = new JobUnitMapper(jobUnitDateParser);
    }

    @Test
    public void should_map_collection_of_units() throws Exception {
        List<JobUnitEntity> entities = Collections.singletonList(mJobUnitEntity);
        List<JobUnit> expected = mJobUnitMapper.transform(entities);
        assertThat(expected.size(), is(1));
    }

    @Test
    public void should_map_entity_to_service_counterpart() throws Exception {
        JobUnit expected = mJobUnitMapper.transform(mJobUnitEntity);

        assertThat(expected.getId(), is(1));
        assertThat(expected.getVersion(), is(100));
        assertThat(expected.getLabel(), is("label"));
        assertThat(expected.getDescription(), is("description"));
        assertThat(expected.getReportUri(), is("/my/uri"));
        assertThat(expected.getReportLabel(), is("report label"));
        assertThat(expected.getOwner().toString(), is("jasperadmin|organization_1"));
        assertThat(expected.getState().toString(), is("NORMAL"));
    }
}