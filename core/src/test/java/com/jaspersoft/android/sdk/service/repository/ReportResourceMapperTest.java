/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.entity.resource.ReportLookup;
import com.jaspersoft.android.sdk.service.data.report.ReportResource;
import com.jaspersoft.android.sdk.service.data.repository.PermissionMask;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReportResourceMapperTest {
    public static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault());

    @Mock
    ReportLookup mReportLookup;
    private ReportResourceMapper mapper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mapper = new ReportResourceMapper();

        when(mReportLookup.getCreationDate()).thenReturn("2013-10-03 16:32:05");
        when(mReportLookup.getUpdateDate()).thenReturn("2013-11-03 16:32:05");
        when(mReportLookup.getResourceType()).thenReturn("reportUnit");
        when(mReportLookup.getDescription()).thenReturn("description");
        when(mReportLookup.getLabel()).thenReturn("label");
        when(mReportLookup.alwaysPromptControls()).thenReturn(false);
        when(mReportLookup.getPermissionMask()).thenReturn(0);
        when(mReportLookup.getVersion()).thenReturn(100);
    }

    @Test
    public void testTransform() throws Exception {
        long creationTime = DATE_FORMAT.parse("2013-10-03 16:32:05").getTime();
        long updateTime = DATE_FORMAT.parse("2013-11-03 16:32:05").getTime();

        ReportResource resource = mapper.transform(mReportLookup, DATE_FORMAT);
        assertThat(resource.getCreationDate().getTime(), is(creationTime));
        assertThat(resource.getUpdateDate().getTime(), is(updateTime));
        assertThat(resource.getDescription(), is("description"));
        assertThat(resource.getLabel(), is("label"));
        assertThat(resource.getResourceType(), is(ResourceType.reportUnit));
        assertThat(resource.getResourceType(), is(ResourceType.reportUnit));
        assertThat(resource.alwaysPromptControls(), is(false));
        assertThat(resource.getVersion(), is(100));
        assertThat(resource.getPermissionMask(), is(PermissionMask.NO_ACCESS));
    }
}