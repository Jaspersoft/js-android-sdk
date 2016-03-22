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
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.report.ReportMetadata;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
abstract class AbstractReportExecution extends ReportExecution {
    protected final ReportExecutionApi mReportExecutionApi;
    protected final String mExecId;
    protected final String mReportUri;
    protected final long mDelay;

    protected AbstractReportExecution(ReportExecutionApi reportExecutionApi,
                                      String execId,
                                      String reportUri,
                                      long delay) {
        mReportExecutionApi = reportExecutionApi;
        mExecId = execId;
        mReportUri = reportUri;
        mDelay = delay;
    }

    @NotNull
    @Override
    public final ReportMetadata waitForReportCompletion() throws ServiceException {
        mReportExecutionApi.awaitStatus(mExecId, mReportUri, mDelay, Status.ready());
        ReportExecutionDescriptor reportDescriptor = mReportExecutionApi.getDetails(mExecId);
        return new ReportMetadata(mReportUri, reportDescriptor.getTotalPages());
    }

    @NotNull
    @Override
    public final ReportExport export(@NotNull ReportExportOptions options) throws ServiceException {
        Preconditions.checkNotNull(options, "Export options should not be null");
        return doExport(options);
    }

    @NotNull
    public final ReportExecution updateExecution(@Nullable List<ReportParameter> newParameters) throws ServiceException {
        if (newParameters == null) {
            newParameters = Collections.emptyList();
        }
        return doUpdateExecution(newParameters);
    }

    @NotNull
    protected abstract ReportExecution doUpdateExecution(@NotNull List<ReportParameter> newParameters) throws ServiceException;

    @NotNull
    protected abstract ReportExport doExport(@NotNull ReportExportOptions options) throws ServiceException;
}
