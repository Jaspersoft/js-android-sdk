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

package com.jaspersoft.android.sdk.service.dashboard;

import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DashboardExportMapperTest {
    private static final long OUTPUT_LENGTH = 123L;
    private static final String MIME_TYPE = "image";

    @Mock
    OutputResource outputResource;
    @Mock
    InputStream stream;

    private DashboardExportMapper mapper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mapper = new DashboardExportMapper();
    }

    @Test
    public void testTransform() throws Exception {
        when(outputResource.getLength()).thenReturn(OUTPUT_LENGTH);
        when(outputResource.getMimeType()).thenReturn(MIME_TYPE);
        when(outputResource.getStream()).thenReturn(stream);

        ResourceOutput export = mapper.transform(outputResource);
        assertThat(export.getStream(), is(stream));
    }
}