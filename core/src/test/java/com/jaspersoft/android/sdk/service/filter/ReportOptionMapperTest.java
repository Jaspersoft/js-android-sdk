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

package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.entity.report.option.ReportOptionEntity;
import com.jaspersoft.android.sdk.service.data.report.option.ReportOption;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReportOptionMapperTest {

    @Mock
    ReportOptionEntity mEntity;

    private ReportOptionMapper mReportOptionMapper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mReportOptionMapper = new ReportOptionMapper();
        when(mEntity.getId()).thenReturn("id");
        when(mEntity.getUri()).thenReturn("/my/uri");
        when(mEntity.getLabel()).thenReturn("label");
    }

    @Test
    public void should_map_entities_to_data_objects() throws Exception {
        Set<ReportOption> expected = mReportOptionMapper.transform(Collections.singleton(mEntity));
        assertThat(expected, is(not(empty())));
    }

    @Test
    public void should_map_entity_to_data_object() throws Exception {
        ReportOption expected = mReportOptionMapper.transform(mEntity);
        assertThat(expected.getId(), is("id"));
        assertThat(expected.getUri(), is("/my/uri"));
        assertThat(expected.getLabel(), is("label"));
    }
}