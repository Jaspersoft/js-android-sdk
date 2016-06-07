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

package com.jaspersoft.android.sdk.env;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class ReportTestBundle {
    private final String reportUri;
    private Map<String, Set<String>> params;
    private final ServerTestBundle serverTestBundle;
    private final PendingTask<Map<String, Set<String>>> paramsRequest;

    public ReportTestBundle(String reportUri, PendingTask<Map<String, Set<String>>> paramsRequest, ServerTestBundle serverTestBundle) {
        this.reportUri = reportUri;
        this.paramsRequest = paramsRequest;
        this.serverTestBundle = serverTestBundle;
    }

    public boolean hasParams() {
        return !loadParamsLazily().isEmpty();
    }

    private Map<String, Set<String>> loadParamsLazily() {
        if (params == null) {
            try {
                params = paramsRequest.perform();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return params;
    }

    public List<ReportParameter> getParams() {
        Map<String, Set<String>> params = loadParamsLazily();
        List<ReportParameter> parameters = new ArrayList<>(params.size());
        for (Map.Entry<String, Set<String>> entry : params.entrySet()) {
            parameters.add(new ReportParameter(entry.getKey(), entry.getValue()));
        }
        return Collections.unmodifiableList(parameters);
    }

    public String getUri() {
        return reportUri;
    }

    public SpringCredentials getCredentials() {
        return serverTestBundle.getCredentials();
    }

    public AuthorizedClient getClient() {
        return serverTestBundle.getClient();
    }

    @Override
    public String toString() {
        return "ReportTestBundle{" +
                "server=" + serverTestBundle +
                ", reportUri='" + reportUri + '\'' +
                '}';
    }
}
