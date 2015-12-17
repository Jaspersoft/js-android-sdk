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


import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.export.ExportOutputResource;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface ReportExportRestApi {

    @NotNull
    ExportExecutionDescriptor runExportExecution(@NotNull Cookies cookies,
                                                 @NotNull String executionId,
                                                 @NotNull ExecutionRequestOptions executionOptions) throws HttpException, IOException;

    @NotNull
    ExecutionStatus checkExportExecutionStatus(@NotNull Cookies cookies,
                                               @NotNull String executionId,
                                               @NotNull String exportId) throws HttpException, IOException;

    @NotNull
    ExportOutputResource requestExportOutput(@NotNull Cookies cookies,
                                             @NotNull String executionId,
                                             @NotNull String exportId) throws HttpException, IOException;

    @NotNull
    OutputResource requestExportAttachment(@NotNull Cookies cookies,
                                           @NotNull String executionId,
                                           @NotNull String exportId,
                                           @NotNull String attachmentId) throws HttpException, IOException;

    final class Builder extends GenericBuilder<Builder, ReportExportRestApi> {
        @Override
        ReportExportRestApi createApi() {
            return new ReportExportRestApiImpl(getAdapter().build());
        }
    }
}
