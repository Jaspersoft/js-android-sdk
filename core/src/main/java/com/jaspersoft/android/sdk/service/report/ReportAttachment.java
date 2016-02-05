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

import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportAttachment {
    private final ExportExecutionApi mExportExecutionApi;
    private final String mExecutionId;
    private final String mExportId;
    private final String mFileName;

    @TestOnly
    ReportAttachment(ExportExecutionApi exportExecutionApi,
                     String executionId,
                     String exportId,
                     String fileName) {
        mExportExecutionApi = exportExecutionApi;
        mExecutionId = executionId;
        mExportId = exportId;
        mFileName = fileName;
    }

    @NotNull
    public String getFileName() {
        return mFileName;
    }

    @NotNull
    public ResourceOutput download() throws ServiceException {
        return mExportExecutionApi.downloadAttachment(
                mExecutionId,
                mExportId,
                mFileName
        );
    }
}
