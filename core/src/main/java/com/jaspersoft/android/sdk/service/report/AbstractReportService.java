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
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Tom Koptel
 * @since 2.0
 */
abstract class AbstractReportService extends ReportService {
    protected final ExportExecutionApi mExportExecutionApi;
    protected final ReportExecutionApi mReportExecutionApi;
    protected final ExportFactory mExportFactory;
    protected final long mDelay;

    protected AbstractReportService(ExportExecutionApi exportExecutionApi,
                                    ReportExecutionApi reportExecutionApi,
                                    ExportFactory exportFactory, long delay) {
        mExportExecutionApi = exportExecutionApi;
        mReportExecutionApi = reportExecutionApi;
        mExportFactory = exportFactory;
        mDelay = delay;
    }

    @NotNull
    @Override
    public ReportExecution run(@NotNull String reportUri, @Nullable ReportExecutionOptions execOptions) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        if (execOptions == null) {
            execOptions = ReportExecutionOptions.builder().build();
        }

        ReportExecutionDescriptor descriptor = mReportExecutionApi.start(reportUri, execOptions);
        String executionId = descriptor.getExecutionId();
        mReportExecutionApi.awaitStatus(executionId, reportUri, mDelay, Status.execution(), Status.ready());

        return buildExecution(reportUri, executionId, execOptions);
    }

    protected abstract ReportExecution buildExecution(String reportUri, String executionId, ReportExecutionOptions criteria);
}