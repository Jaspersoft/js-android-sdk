/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.InputControlRestApi;
import com.jaspersoft.android.sdk.network.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.ReportOptionRestApi;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class ReportServiceFactory {
    private final InfoCacheManager mCacheManager;
    private final ReportExecutionRestApi mReportExecutionRestApi;
    private final ReportExportRestApi mReportExportRestApi;
    private final ServiceExceptionMapper mExceptionMapper;
    private final String mBaseUrl;
    private final long mDelay;


    ReportServiceFactory(InfoCacheManager cacheManager,
                         ReportExecutionRestApi reportExecutionRestApi,
                         ReportExportRestApi reportExportRestApi,
                         ServiceExceptionMapper exceptionMapper,
                         String baseUrl, long delay) {
        mCacheManager = cacheManager;
        mReportExecutionRestApi = reportExecutionRestApi;
        mReportExportRestApi = reportExportRestApi;
        mExceptionMapper = exceptionMapper;
        mBaseUrl = baseUrl;
        mDelay = delay;
    }

    public ReportService newService() throws ServiceException {
        ServerInfo info = mCacheManager.getInfo();
        ServerVersion version = info.getVersion();

        ExportOptionsMapper exportOptionsMapper = ExportOptionsMapper.create(version, mBaseUrl);
        ReportOptionsMapper reportOptionsMapper = ReportOptionsMapper.create(version, mBaseUrl);

        ExportExecutionApi exportExecutionApi = createExportExecApi(exportOptionsMapper);
        ReportExecutionApi reportExecutionApi = createReportExecApi(reportOptionsMapper);

        AttachmentsFactory attachmentsFactory = new AttachmentsFactory(exportExecutionApi);
        ExportFactory exportFactory = new ExportFactory(exportExecutionApi, attachmentsFactory);

        if (version.lessThanOrEquals(ServerVersion.v5_5)) {
            return new ReportService5_5(
                    exportExecutionApi,
                    reportExecutionApi,
                    exportFactory,
                    mDelay);
        } else {
            return new ReportService5_6Plus(
                    exportExecutionApi,
                    reportExecutionApi,
                    exportFactory,
                    mDelay);
        }
    }

    @NotNull
    private ReportExecutionApi createReportExecApi(ReportOptionsMapper reportOptionsMapper) {
        return new ReportExecutionApi(mExceptionMapper, mReportExecutionRestApi, reportOptionsMapper);
    }

    @NotNull
    private ExportExecutionApi createExportExecApi(ExportOptionsMapper exportOptionsMapper) {
        ReportExportMapper reportExportMapper = new ReportExportMapper();
        AttachmentExportMapper attachmentExportMapper = new AttachmentExportMapper();
        return new ExportExecutionApi(mExceptionMapper, mReportExportRestApi, exportOptionsMapper, reportExportMapper, attachmentExportMapper);
    }
}
