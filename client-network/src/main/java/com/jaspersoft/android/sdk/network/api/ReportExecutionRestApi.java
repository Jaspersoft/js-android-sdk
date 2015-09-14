/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
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

package com.jaspersoft.android.sdk.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatusResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionSearchResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportParameter;

import java.util.Collection;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface ReportExecutionRestApi {

    @NonNull
    @WorkerThread
    ReportExecutionDetailsResponse runReportExecution(@NonNull ReportExecutionRequestOptions executionOptions);

    @NonNull
    @WorkerThread
    ReportExecutionDetailsResponse requestReportExecutionDetails(@NonNull String executionId);

    @NonNull
    @WorkerThread
    ExecutionStatusResponse requestReportExecutionStatus(@NonNull String executionId);

    @WorkerThread
    boolean cancelReportExecution(@NonNull String executionId);

    @WorkerThread
    boolean updateReportExecution(@NonNull String executionId, @NonNull Collection<ReportParameter> params);

    /**
     * TODO: API is broken requires investigation before release
     */
    @NonNull
    @WorkerThread
    ReportExecutionSearchResponse searchReportExecution(Map<String, String> params);

    final class Builder extends GenericAuthBuilder<Builder, ReportExecutionRestApi> {
        @Override
        ReportExecutionRestApi createApi() {
            return new ReportExecutionRestApiImpl(createAdapter());
        }
    }
}
