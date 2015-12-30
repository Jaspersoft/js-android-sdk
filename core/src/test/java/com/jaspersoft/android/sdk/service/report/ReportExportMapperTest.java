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

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.export.ExportOutputResource;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import com.jaspersoft.android.sdk.service.data.report.ReportExportOutput;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ExportOutputResource.class,
})
public class ReportExportMapperTest {

    private static final String TOTAL_PAGES = "9999";
    private static final PageRange PAGE_RANGE = PageRange.parse(TOTAL_PAGES);

    @Mock
    ExportOutputResource mExportOutputResource;
    @Mock
    OutputResource mOutput;
    @Mock
    InputStream mStream;

    private ReportExportMapper mapper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mapper = new ReportExportMapper();
    }

    @Test
    public void testTransform() throws Exception {
        when(mExportOutputResource.getPages()).thenReturn(TOTAL_PAGES);
        when(mExportOutputResource.isFinal()).thenReturn(Boolean.FALSE);

        when(mOutput.getStream()).thenReturn(mStream);
        when(mExportOutputResource.getOutputResource()).thenReturn(mOutput);

        ReportExportOutput export = mapper.transform(mExportOutputResource);
        assertThat(export.getPages(), is(PAGE_RANGE));
        assertThat(export.isFinal(), is(Boolean.FALSE));
        assertThat(export.getStream(), is(mStream));
    }
}