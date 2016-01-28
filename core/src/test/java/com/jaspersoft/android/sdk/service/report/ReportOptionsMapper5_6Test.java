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
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ReportOptionsMapper5_6Test {
    private static final String BASE_URL = "http://localhost";
    private static final String REPORT_URI = "my/uri";

    private ReportOptionsMapper5_6 mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ReportOptionsMapper5_6(BASE_URL);
    }

    @Test
    public void should_always_disable_interactiveness() {
        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withInteractive(true)
                .build();
        ExecutionRequestOptions options = map(criteria);
        assertThat("Failed to disable 'interactiveness' option", options.getInteractive(), is(false));
    }

    private ReportExecutionRequestOptions map(ReportExecutionOptions criteria) {
        return mapper.transform(REPORT_URI, criteria);
    }
}