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

package com.jaspersoft.android.sdk.network.api;


import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.export.ExportOutputResource;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;

import org.jetbrains.annotations.NotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface ReportExportRestApi {

    @NotNull
    ExportExecutionDescriptor runExportExecution(@NotNull String token,
                                                 @NotNull String executionId,
                                                 @NotNull ExecutionRequestOptions executionOptions);

    @NotNull
    ExecutionStatus checkExportExecutionStatus(@NotNull String token,
                                               @NotNull String executionId,
                                               @NotNull String exportId);

    @NotNull
    ExportOutputResource requestExportOutput(@NotNull String token,
                                             @NotNull String executionId,
                                             @NotNull String exportId);

    @NotNull
    OutputResource requestExportAttachment(@NotNull String token,
                                           @NotNull String executionId,
                                           @NotNull String exportId,
                                           @NotNull String attachmentId);

    final class Builder extends GenericBuilder<Builder, ReportExportRestApi> {
        @Override
        ReportExportRestApi createApi() {
            return new ReportExportRestApiImpl(getAdapter().build());
        }
    }
}
