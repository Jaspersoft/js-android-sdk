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
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class ReportOptionsMapper5_5Test {

    private static final String BASE_URL = "http://localhost";
    private static final String REPORT_URI = "my/uri";

    private ReportOptionsMapper5_5 mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ReportOptionsMapper5_5(BASE_URL);
    }

    @Test
    public void should_map_format() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withFormat(ReportFormat.PDF)
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'format' option", options.getOutputFormat(), is("pdf"));
    }

    @Test
    public void should_map_html_by_default() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder().build();
        ReportExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map HTML to 'format' option", options.getOutputFormat(), is("html"));
    }

    @Test
    public void should_not_map_markup() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withMarkupType(ReportMarkup.EMBEDDABLE)
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to remove 'markup' option", options.getMarkupType(), is(nullValue()));
    }

    @Test
    public void should_map_params() {
        List<ReportParameter> params = Collections.<ReportParameter>emptyList();
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withParams(params)
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'params' option", options.getParameters(), is(params));
    }

    @Test
    public void should_map_pages() {
        PageRange range = PageRange.parse("1-10");
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withPageRange(range)
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'pages' option", options.getPages(), is("1-10"));
    }

    @Test
    public void should_not_map_anchor() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withAnchor("anchor")
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to remove 'anchor' option", options.getAnchor(), is(nullValue()));
    }


    @Test
    public void should_map_attachmentPrefix() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withAttachmentPrefix("./")
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'attachmentPrefix' option", options.getAttachmentsPrefix(), is(".%2F"));
    }

    @Test
    public void should_not_map_allowInlineScripts_flag() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withAllowInlineScripts(true)
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to remove 'allowInlineScripts' option", options.getAllowInlineScripts(), is(nullValue()));
    }

    @Test
    public void should_map_fresh_data_flag() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withFreshData(true)
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'freshData' option", options.getFreshData(), is(true));
    }

    @Test
    public void should_map_save_snapshot_flag() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withSaveSnapshot(true)
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'saveSnapshot' option", options.getSaveDataSnapshot(), is(true));
    }

    @Test
    public void should_map_interactiveness_flag() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withInteractive(true)
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'interactiveness' option", options.getInteractive(), is(true));
    }

    @Test
    public void should_map_ignore_pagination_flag() throws Exception {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withIgnorePagination(true)
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'ignorePagination' option", options.getIgnorePagination(), is(true));
    }

    @Test
    public void should_map_transformer_key() throws Exception {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withTransformerKey("key")
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'transformerKey' option", options.getTransformerKey(), is("key"));
    }

    @Test
    public void should_not_map_baseUrl() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder().build();
        ReportExecutionRequestOptions options = map(criteria);
        assertThat("Failed to remove 'baseUrl' option", options.getBaseUrl(), is(nullValue()));
    }

    @Test
    public void should_map_report_uri() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder().build();
        ReportExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'baseUrl' option", options.getReportUnitUri(), is(REPORT_URI));
    }

    private ReportExecutionRequestOptions map(ReportExecutionOptions criteria) {
        return mapper.transform(REPORT_URI, criteria);
    }
}