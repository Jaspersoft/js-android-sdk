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

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface ReportExecutionRestApi {

    @NotNull
    ReportExecutionDescriptor runReportExecution(@NotNull String token, @NotNull ReportExecutionRequestOptions executionOptions);

    @NotNull
    ReportExecutionDescriptor requestReportExecutionDetails(@NotNull String token, @NotNull String executionId);

    @NotNull
    ExecutionStatus requestReportExecutionStatus(@NotNull String token, @NotNull String executionId);

    boolean cancelReportExecution(@NotNull String token, @NotNull String executionId);

    boolean updateReportExecution(@NotNull String token,
                                  @NotNull String executionId,
                                  @NotNull Collection<Map<String, Set<String>>> params);

     // TODO: API is broken requires investigation before release
    @NotNull
    ReportExecutionSearchResponse searchReportExecution(@NotNull String token, Map<String, String> params);

    final class Builder extends GenericBuilder<Builder, ReportExecutionRestApi> {
        @Override
        ReportExecutionRestApi createApi() {
            return new ReportExecutionRestApiImpl(getAdapter().build());
        }
    }
}
