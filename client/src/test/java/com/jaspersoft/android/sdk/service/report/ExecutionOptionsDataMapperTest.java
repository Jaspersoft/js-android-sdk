/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
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

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ExecutionOptionsDataMapperTest {

    private static final String REPORT_URI = "/report/uri";
    private static final String BASE_URL = "http:://localhost";
    public static final Map<String, Set<String>> REPORT_PARAMS = Collections.emptyMap();

    private ExecutionOptionsDataMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ExecutionOptionsDataMapper(BASE_URL);
    }

    @Test
    public void testTransformReportOptions() throws Exception {
        RunReportCriteria criteria = RunReportCriteria.builder()
                .format(ExecutionCriteria.Format.HTML)
                .freshData(true)
                .interactive(false)
                .saveSnapshot(true)
                .pages("1-100")
                .params(REPORT_PARAMS)
                .attachmentPrefix("./")
                .create();
        ReportExecutionRequestOptions options = mapper.transformRunReportOptions(REPORT_URI, criteria);
        assertThat(options.getReportUnitUri(), is(REPORT_URI));
        assertThat(options.getParameters(), is(REPORT_PARAMS));
        assertThat(options.getAsync(), is(true));
        assertOptions(options);
    }

    @Test
    public void testTransformExportOptions() throws Exception {
        RunExportCriteria criteria = RunExportCriteria.builder()
                .format(ExecutionCriteria.Format.HTML)
                .freshData(true)
                .interactive(false)
                .saveSnapshot(true)
                .pages("1-100")
                .attachmentPrefix("./")
                .create();
        ExecutionRequestOptions options = mapper.transformExportOptions(criteria);
        assertOptions(options);
    }

    private void assertOptions(ExecutionRequestOptions options) {
        assertThat(options.getFreshData(), is(true));
        assertThat(options.getSaveDataSnapshot(), is(true));
        assertThat(options.getInteractive(), is(false));
        assertThat(options.getSaveDataSnapshot(), is(true));

        assertThat(options.getAllowInlineScripts(), is(nullValue()));
        assertThat(options.getTransformerKey(), is(nullValue()));

        assertThat(options.getBaseUrl(), is(BASE_URL));
        assertThat(options.getAttachmentsPrefix(), is(".%2F"));
        assertThat(options.getOutputFormat(), is("HTML"));
        assertThat(options.getPages(), is("1-100"));
    }
}