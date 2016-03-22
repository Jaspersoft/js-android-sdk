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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Public API allows to wait for report completion, wait for report completion and performs export.
 *
 * @author Tom Koptel
 * @since 2.3
 */
public abstract class ReportExecution {
    /**
     * Initiates report export process
     *
     * @param options allows to configure export process of JRS side
     * @return export for corresponding execution
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public abstract ReportExport export(@NotNull ReportExportOptions options) throws ServiceException;

    /**
     * Performs series of requests until the status reach completion or fail response
     *
     * @return details of execution
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public abstract ReportMetadata waitForReportCompletion() throws ServiceException;

    /**
     * Updates and restart report execution
     *
     * @param newParameters that force execution process to be restarted
     * @return new report execution
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public abstract ReportExecution updateExecution(@Nullable List<ReportParameter> newParameters) throws ServiceException;
}
