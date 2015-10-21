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

package com.jaspersoft.android.sdk.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.adapter.ExecutionRequestAdapter;
import com.jaspersoft.android.sdk.client.util.ServerVersion;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 1.10
 */
@RunWith(JUnitParamsRunner.class)
public class ExecutionRequestAdapterTest {
    private static final String EMPTY_JSON = "{}";
    private Gson gson;

    @Parameters(method = "getNullVersionCodes")
    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptInvalidVersionCode(String versionCode) {
        ExecutionRequestAdapter.newInstance(versionCode);
    }

    @Before
    public void setup() {
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Test
    public void shouldExcludeInlineScriptsForEMERALD_MR2() {
        ExecutionRequestAdapter adapter =
                ExecutionRequestAdapter.newInstance(ServerVersion.V5_5.get());

        ReportExecutionRequest request = new ReportExecutionRequest();
        request.setAllowInlineScripts(true);

        request = adapter.adapt(request);
        String json = gson.toJson(request);
        assertThat(json, is(EMPTY_JSON));
    }

    @Test
    public void shouldExcludeBaseUrlForEMERALD_MR2() {
        ExecutionRequestAdapter adapter =
                ExecutionRequestAdapter.newInstance(ServerVersion.V5_5.get());

        ReportExecutionRequest request = new ReportExecutionRequest();
        request.setBaseUrl("some_base_url");

        request = adapter.adapt(request);
        String json = gson.toJson(request);
        assertThat(json, is(EMPTY_JSON));
    }

    @Test
    public void shouldExcludeMarkupTypeForEMERALD_MR2() {
        ExecutionRequestAdapter adapter =
                ExecutionRequestAdapter.newInstance(ServerVersion.V5_5.get());

        ReportExecutionRequest request = new ReportExecutionRequest();
        request.setMarkupType("some_type");

        request = adapter.adapt(request);
        String json = gson.toJson(request);
        assertThat(json, is(EMPTY_JSON));
    }

    public Object[] getNullVersionCodes() {
        return new Object[] {
                null, "", "  "
        };
    }
}
