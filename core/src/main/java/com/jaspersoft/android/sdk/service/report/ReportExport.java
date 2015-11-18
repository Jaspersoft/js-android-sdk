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

import com.jaspersoft.android.sdk.service.data.report.ReportOutput;
import com.jaspersoft.android.sdk.service.exception.StatusException;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportExport {
    private final Collection<ReportAttachment> mAttachments;
    private final String mExecutionId;
    private final String mExportId;
    private final ReportExportUseCase mExportUseCase;

    ReportExport(String executionId,
                 String exportId,
                 Collection<ReportAttachment> attachments,
                 ReportExportUseCase exportUseCase) {
        mExecutionId = executionId;
        mExportId = exportId;
        mAttachments = attachments;
        mExportUseCase = exportUseCase;
    }

    @NotNull
    public Collection<ReportAttachment> getAttachments() {
        return mAttachments;
    }

    @NotNull
    public ReportOutput download() throws StatusException {
        return mExportUseCase.requestExportOutput(mExecutionId, mExportId);
    }
}
