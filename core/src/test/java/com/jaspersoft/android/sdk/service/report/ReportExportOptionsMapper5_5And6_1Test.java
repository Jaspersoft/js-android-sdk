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
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class ReportExportOptionsMapper5_5And6_1Test {
    private static final String BASE_URL = "http://localhost";

    private ExportOptionsMapper5_5and6_1 mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ExportOptionsMapper5_5and6_1(BASE_URL);
    }

    @Test
    public void should_map_format() {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to map 'format' option", options.getOutputFormat(), is("pdf"));
    }

    @Test
    public void should_map_pages() {
        PageRange range = PageRange.parse("1-10");
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .withPageRange(range)
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to map 'pages' option", options.getPages(), is("1-10"));
    }

    @Test
    public void should_include_ignore_pagination_flag() throws Exception {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .withIgnorePagination(true)
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to remove 'ignorePagination' option", options.getIgnorePagination(), is(nullValue()));
    }

    @Test
    public void should_map_anchor() {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .withAnchor("anchor")
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to map 'anchor' option", options.getAnchor(), is("anchor"));
    }

    @Test
    public void should_map_decoded_attachmentPrefix() {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .withAttachmentPrefix("./")
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to map 'attachmentPrefix' option", options.getAttachmentsPrefix(), is(".%2F"));
    }

    @Test
    public void should_map_allowInlineScripts() {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .withAllowInlineScripts(true)
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to map 'allowInlineScripts' option", options.getAllowInlineScripts(), is(true));
    }

    @Test
    public void should_map_baseUrl() {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to map 'baseUrl' option", options.getBaseUrl(), is(BASE_URL));
    }
}