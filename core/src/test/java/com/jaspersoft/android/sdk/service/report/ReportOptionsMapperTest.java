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

public class ReportOptionsMapperTest {
    private static final String BASE_URL = "http://localhost";
    private static final String REPORT_URI = "my/uri";

    private ReportOptionsMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ReportOptionsMapper(BASE_URL){};
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
    public void should_map_markup() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withMarkupType(ReportMarkup.EMBEDDABLE)
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'markup' option", options.getMarkupType(), is("embeddable"));
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
    public void should_map_anchor() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withAnchor("anchor")
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'anchor' option", options.getAnchor(), is("anchor"));
    }


    @Test
    public void should_map_attachmentPrefix() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withAttachmentPrefix("./")
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'attachmentPrefix' option", options.getAttachmentsPrefix(), is("./"));
    }

    @Test
    public void should_map_allowInlineScripts_flag() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withAllowInlineScripts(true)
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'allowInlineScripts' option", options.getAllowInlineScripts(), is(true));
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
    public void should_map_baseUrl() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder().build();
        ReportExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'baseUrl' option", options.getBaseUrl(), is(BASE_URL));
    }

    @Test
    public void should_map_report_uri() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder().build();
        ReportExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map 'baseUrl' option", options.getReportUnitUri(), is(REPORT_URI));
    }

    @Test
    public void should_always_map_async_true() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder().build();
        ReportExecutionRequestOptions options = map(criteria);
        assertThat("Failed to map async flag", options.getAsync(), is(true));
    }

    private ReportExecutionRequestOptions map(ReportExecutionOptions criteria) {
        return mapper.transform(REPORT_URI, criteria);
    }
}