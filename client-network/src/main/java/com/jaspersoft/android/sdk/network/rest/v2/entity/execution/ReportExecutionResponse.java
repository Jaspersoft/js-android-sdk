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

package com.jaspersoft.android.sdk.network.rest.v2.entity.execution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportExecutionResponse {
    @Expose
    @SerializedName("requestId")
    private String executionId;
    @Expose
    private String reportURI;
    @Expose
    private String status;
    @Expose
    private int currentPage;
    @Expose
    private int totalPages;
    @Expose
    private Set<ExportExecution> exports = Collections.emptySet();
    @Expose
    private ErrorDescriptor errorDescriptor;

    public String getReportURI() {
        return reportURI;
    }

    public String getExecutionId() {
        return executionId;
    }

    public String getStatus() {
        return status;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public ErrorDescriptor getErrorDescriptor() {
        return errorDescriptor;
    }

    public Set<ExportExecution> getExports() {
        return exports;
    }

    @Override
    public String toString() {
        return "ReportExecutionResponse{" +
                "currentPage=" + currentPage +
                ", executionId='" + executionId + '\'' +
                ", reportURI='" + reportURI + '\'' +
                ", status='" + status + '\'' +
                ", exports=" + Arrays.toString(exports.toArray()) +
                ", totalPages=" + totalPages +
                ", errorDescriptor=" + errorDescriptor +
                '}';
    }
}
