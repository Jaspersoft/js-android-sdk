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

package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionSearchResponse;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface ReportExecutionRestApi {

    @NotNull
    ReportExecutionDescriptor runReportExecution(@NotNull ReportExecutionRequestOptions executionOptions) throws HttpException, IOException;

    @NotNull
    ReportExecutionDescriptor requestReportExecutionDetails(@NotNull String executionId) throws HttpException, IOException;

    @NotNull
    ExecutionStatus requestReportExecutionStatus(@NotNull String executionId) throws HttpException, IOException;

    boolean cancelReportExecution(@NotNull String executionId) throws HttpException, IOException;

    boolean updateReportExecution(@NotNull String executionId,
                                  @NotNull List<ReportParameter> params) throws HttpException, IOException;

     // TODO: API is broken requires investigation before release
    @NotNull
    ReportExecutionSearchResponse searchReportExecution(Map<String, String> params) throws HttpException, IOException;
}
