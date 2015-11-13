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

package com.jaspersoft.android.sdk.testkit.dto;

import com.google.gson.annotations.Expose;

import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class ReportExecConfig {
    @Expose
    private final Map<String, Set<String>> parameters;
    @Expose
    private final String reportUnitUri;

    ReportExecConfig(Map<String, Set<String>> parameters, String reportUnitUri) {
        this.parameters = parameters;
        this.reportUnitUri = reportUnitUri;
    }

    public String getUri() {
        return reportUnitUri;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Map<String, Set<String>> mParameters;
        private String mReportUnitUri;

        private Builder() {}

        public Builder params(Map<String, Set<String>> parameters) {
            mParameters = parameters;
            return this;
        }

        public Builder uri(String reportUnitUri) {
            mReportUnitUri = reportUnitUri;
            return this;
        }

        public ReportExecConfig create() {
            return new ReportExecConfig(mParameters, mReportUnitUri);
        }
    }
}
