/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.client;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.adapter.ReportExecutionRequestAdapter;
import com.jaspersoft.android.sdk.client.oxm.server.ServerInfo;
import com.jaspersoft.android.sdk.client.util.ServerVersion;

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
public class ReportExecutionRequestAdapterTest {
    private static final String v5_5 = String.valueOf(ServerInfo.VERSION_CODES.EMERALD_TWO);
    private static final String EMPTY_JSON = "{}";

    @Parameters(method = "getNullVersionCodes")
    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptInvalidVersionCode(String versionCode) {
        ReportExecutionRequestAdapter.newInstance(versionCode);
    }

    @Test
    public void shouldExcludeInlineScriptsForEMERALD_MR2() {
        ReportExecutionRequestAdapter adapter =
                ReportExecutionRequestAdapter.newInstance(ServerVersion.V5_5.get());

        ReportExecutionRequest request = new ReportExecutionRequest();
        request.setAllowInlineScripts(true);

        request = adapter.adapt(request);
        String json = new Gson().toJson(request);
        assertThat(json, is(EMPTY_JSON));
    }

    @Test
    public void shouldExcludeBaseUrlForEMERALD_MR2() {
        ReportExecutionRequestAdapter adapter =
                ReportExecutionRequestAdapter.newInstance(ServerVersion.V5_5.get());

        ReportExecutionRequest request = new ReportExecutionRequest();
        request.setBaseUrl("some_base_url");

        request = adapter.adapt(request);
        String json = new Gson().toJson(request);
        assertThat(json, is(EMPTY_JSON));
    }

    @Test
    public void shouldExcludeMarkupTypeForEMERALD_MR2() {
        ReportExecutionRequestAdapter adapter =
                ReportExecutionRequestAdapter.newInstance(ServerVersion.V5_5.get());

        ReportExecutionRequest request = new ReportExecutionRequest();
        request.setMarkupType("some_type");

        request = adapter.adapt(request);
        String json = new Gson().toJson(request);
        assertThat(json, is(EMPTY_JSON));
    }

    public Object[] getNullVersionCodes() {
        return new Object[] {
                null, "", "  "
        };
    }
}
