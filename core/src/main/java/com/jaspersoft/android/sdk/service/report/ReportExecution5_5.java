/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
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

import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
final class ReportExecution5_5 extends AbstractReportExecution {
    private final ExportExecutionApi mExportExecutionApi;
    private final ExportIdWrapper mExportIdWrapper;
    private final ExportFactory mExportFactory;
    private final ReportExecutionOptions mReportExecutionOptions;

    ReportExecution5_5(ExportExecutionApi exportExecutionApi,
                       ReportExecutionApi reportExecutionRestApi,
                       ExportIdWrapper exportIdWrapper,
                       ExportFactory exportFactory,
                       ReportExecutionOptions reportExecutionOptions,
                       String execId,
                       String reportUri,
                       long delay) {
        super(reportExecutionRestApi, execId, reportUri, delay);
        mExportExecutionApi = exportExecutionApi;
        mExportIdWrapper = exportIdWrapper;
        mExportFactory = exportFactory;
        mReportExecutionOptions = reportExecutionOptions;
    }

    @NotNull
    @Override
    protected ReportExport doExport(@NotNull ReportExportOptions options) throws ServiceException {
        ExportExecutionDescriptor exportDetails = mExportExecutionApi.start(mExecId, options);
        ReportExecutionDescriptor reportDescriptor = mReportExecutionApi.getDetails(mExecId);

        mExportIdWrapper.wrap(exportDetails, options);
        return mExportFactory.create(reportDescriptor, mExecId, mExportIdWrapper);
    }

    @NotNull
    @Override
    protected ReportExecution doUpdateExecution(@Nullable List<ReportParameter> newParameters) throws ServiceException {
        ReportExecutionOptions criteria = mReportExecutionOptions.newBuilder()
                .withParams(newParameters)
                .build();
        ReportExecutionDescriptor descriptor = mReportExecutionApi.start(mReportUri, criteria);

        String executionId = descriptor.getExecutionId();
        mReportExecutionApi.awaitStatus(executionId, mReportUri, mDelay, Status.execution(), Status.ready());

        return new ReportExecution5_5(
                mExportExecutionApi,
                mReportExecutionApi,
                mExportIdWrapper,
                mExportFactory,
                criteria,
                executionId,
                mReportUri,
                mDelay
        );
    }
}
