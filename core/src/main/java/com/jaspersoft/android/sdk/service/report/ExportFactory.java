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

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.execution.ExportDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class ExportFactory {
    private final ExportExecutionApi mExportExecutionApi;
    private final AttachmentsFactory mAttachmentsFactory;

    ExportFactory(ExportExecutionApi exportExecutionApi, AttachmentsFactory attachmentsFactory) {
        mExportExecutionApi = exportExecutionApi;
        mAttachmentsFactory = attachmentsFactory;
    }

    @NotNull
    public ReportExport create(ReportExecutionDescriptor descriptor, String execId, String exportId) throws ServiceException {
        ExportDescriptor export = findExportDescriptor(descriptor, exportId);
        if (export == null) {
            throw new ServiceException("Server returned malformed export details", null, StatusCodes.EXPORT_EXECUTION_FAILED);
        }
        List<ReportAttachment> attachments = mAttachmentsFactory.create(export, execId);
        return new ReportExport(mExportExecutionApi, attachments, execId, exportId);
    }

    @Nullable
    private ExportDescriptor findExportDescriptor(ReportExecutionDescriptor executionDetails, String exportId) {
        for (ExportDescriptor export : executionDetails.getExports()) {
            if (exportId.equals(export.getId())) {
                return export;
            }
        }
        return null;
    }
}
