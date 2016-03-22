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

import com.jaspersoft.android.sdk.service.data.report.ReportExportOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.Collections;
import java.util.List;

/**
 * Public API that wraps concrete export requested by user.
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class ReportExport {
    private final ExportExecutionApi mExportExecutionApi;
    private final List<ReportAttachment> mAttachments;
    private final String mExecutionId;
    private final String mExportId;

    @TestOnly
    ReportExport(ExportExecutionApi exportExecutionApi,
                 List<ReportAttachment> attachments,
                 String executionId,
                 String exportId) {
        mExportExecutionApi = exportExecutionApi;
        mAttachments = attachments;
        mExecutionId = executionId;
        mExportId = exportId;
    }

    /**
     * List of available attachments related to particular export
     *
     * @return attachments list or empty collection
     */
    @NotNull
    public List<ReportAttachment> getAttachments() {
        return Collections.unmodifiableList(mAttachments);
    }

    /**
     * Performs network operation that returns output of export
     *
     * @return output of export that wraps {@link java.io.InputStream}
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public ReportExportOutput download() throws ServiceException {
        return mExportExecutionApi.downloadExport(mExecutionId, mExportId);
    }
}
