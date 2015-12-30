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

import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ReportExecution5_6Plus extends AbstractReportExecution {
    private final ExportExecutionApi mExportExecutionApi;
    private final ExportFactory mExportFactory;

    ReportExecution5_6Plus(ExportExecutionApi exportExecutionApi,
                           ReportExecutionApi reportExecutionRestApi,
                           ExportFactory exportFactory,
                           String execId,
                           String reportUri,
                           long delay) {
        super(reportExecutionRestApi, execId, reportUri, delay);
        mExportExecutionApi = exportExecutionApi;
        mExportFactory = exportFactory;
    }

    @NotNull
    @Override
    public ReportExport export(@NotNull ReportExportOptions options) throws ServiceException {
        ExportExecutionDescriptor exportDetails = mExportExecutionApi.start(mExecId, options);
        String exportId = exportDetails.getExportId();

        mExportExecutionApi.awaitReadyStatus(mExecId, exportId, mReportUri, mDelay);
        ReportExecutionDescriptor reportDescriptor = mReportExecutionApi.getDetails(mExecId);

        return mExportFactory.create(reportDescriptor, mExecId, exportId);
    }

    @NotNull
    @Override
    public ReportExecution updateExecution(@Nullable List<ReportParameter> newParameters) throws ServiceException {
        mReportExecutionApi.update(mExecId, newParameters);
        mReportExecutionApi.awaitStatus(mExecId, mReportUri, mDelay, Status.execution(), Status.ready());
        return this;
    }
}
