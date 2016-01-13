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

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.report.ReportMetadata;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class RetryReportExecution extends ReportExecution {
    private final ReportExecution mDelegate;

    RetryReportExecution(ReportExecution delegate) {
        mDelegate = delegate;
    }

    @NotNull
    @Override
    public ReportExport export(@NotNull ReportExportOptions options) throws ServiceException {
        try {
            return mDelegate.export(options);
        } catch (ServiceException ex) {
            boolean isCancelled = (ex.code() == StatusCodes.EXPORT_EXECUTION_CANCELLED ||
                    ex.code() == StatusCodes.REPORT_EXECUTION_CANCELLED);
            if (isCancelled) {
                /**
                 * Cancelled by technical reason. User applied Jive(for e.g. have applied new filter).
                 * Cancelled when report execution finished. This event flags that we need rerun export.
                 */
                return mDelegate.export(options);
            }
            throw ex;
        }
    }

    @NotNull
    @Override
    public ReportMetadata waitForReportCompletion() throws ServiceException {
        return mDelegate.waitForReportCompletion();
    }

    @NotNull
    @Override
    public ReportExecution updateExecution(@Nullable List<ReportParameter> newParameters) throws ServiceException {
        return mDelegate.updateExecution(newParameters);
    }
}
