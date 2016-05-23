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

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.TestConstants;
import com.jaspersoft.android.sdk.network.entity.resource.ReportLookup;
import com.jaspersoft.android.sdk.service.data.report.ReportResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReportResourceMapperTest {
    @Mock
    ReportLookup mReportLookup;

    private ReportResourceMapper mapper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mapper = new ReportResourceMapper(TestConstants.DATE_TIME_FORMAT);
        mReportLookup = MockResourceFactory.mockCommonFields(mReportLookup);
    }

    @Test
    public void should_map_file_lookup() throws Exception {
        mockFileLookup();
        ReportResource resource = mapper.transform(mReportLookup);
        assertThat(resource.alwaysPromptControls(), is(true));
    }

    private void mockFileLookup() {
        when(mReportLookup.alwaysPromptControls()).thenReturn(true);
        when(mReportLookup.getResourceType()).thenReturn("reportUnit");
    }
}