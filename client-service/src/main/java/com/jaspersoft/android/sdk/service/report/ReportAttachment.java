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
package com.jaspersoft.android.sdk.service.report;

import android.support.annotation.NonNull;

import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.export.ExportInput;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportAttachment {
    private final ReportExportRestApi.Factory mExportApiFactory;
    private final String mFileName;
    private final String mExportId;
    private final String mExecutionId;

    ReportAttachment(String fileName,
                     String executionId,
                     String exportId,
                     ReportExportRestApi.Factory exportApiFactory) {
        mFileName = fileName;
        mExportId = exportId;
        mExecutionId = executionId;
        mExportApiFactory = exportApiFactory;
    }

    @NonNull
    public ExportInput download() {
        return mExportApiFactory.get().requestExportAttachment(mExecutionId, mExportId, mFileName);
    }
}
