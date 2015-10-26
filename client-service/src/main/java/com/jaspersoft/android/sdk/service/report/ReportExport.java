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
import com.jaspersoft.android.sdk.network.entity.export.ExportOutputResource;
import com.jaspersoft.android.sdk.service.auth.TokenProvider;

import java.util.Collection;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportExport {
    private final Collection<ReportAttachment> mAttachments;
    private final String mExecutionId;
    private final String mExportId;

    private final ReportExportRestApi mExportApi;
    private final TokenProvider mTokenProvider;

    ReportExport(String executionId,
                 String exportId,
                 Collection<ReportAttachment> attachments,
                 TokenProvider tokenProvider,
                 ReportExportRestApi exportApi) {
        mExecutionId = executionId;
        mExportId = exportId;
        mAttachments = attachments;

        mTokenProvider = tokenProvider;
        mExportApi = exportApi;
    }

    @NonNull
    public Collection<ReportAttachment> getAttachments() {
        return mAttachments;
    }

    @NonNull
    public ExportOutputResource download() {
        return mExportApi.requestExportOutput(mTokenProvider.provideToken(), mExecutionId, mExportId);
    }
}
