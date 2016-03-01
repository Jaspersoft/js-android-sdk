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

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class ReportExportOptionsMapper5_6And6_0Test {
    private static final String BASE_URL = "http://localhost";

    private ExportOptionsMapper5_6and6_0 mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ExportOptionsMapper5_6and6_0(BASE_URL);
    }

    @Test
    public void should_exclude_ignore_pagination_flag() throws Exception {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .withIgnorePagination(true)
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to remove 'ignorePagination' option", options.getIgnorePagination(), is(nullValue()));
    }

    @Test
    public void should_exclude_ignore_markup() throws Exception {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .withMarkupType(ReportMarkup.EMBEDDABLE)
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to remove 'markup' option", options.getIgnorePagination(), is(nullValue()));
    }
}