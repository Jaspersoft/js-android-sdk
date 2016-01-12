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

import com.jaspersoft.android.sdk.network.entity.control.InputControl;
import com.jaspersoft.android.sdk.network.entity.control.InputControlState;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ProxyReportService extends ReportService {
    private final ReportServiceFactory mServiceFactory;
    private ReportService mDelegate;

    ProxyReportService(ReportServiceFactory serviceFactory) {
        mServiceFactory = serviceFactory;
    }

    @NotNull
    @Override
    public ReportExecution run(@Nullable String reportUri,
                               @Nullable ReportExecutionOptions execOptions) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        if (execOptions == null) {
            execOptions = ReportExecutionOptions.builder().build();
        }
        return getDelegate().run(reportUri, execOptions);
    }

    @NotNull
    @Override
    public List<InputControl> listControls(@Nullable String reportUri) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        return getDelegate().listControls(reportUri);
    }

    @NotNull
    @Override
    public List<InputControlState> listControlsValues(@Nullable String reportUri,
                                                      @Nullable List<ReportParameter> parameters) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(parameters, "Parameters should not be null");
        return getDelegate().listControlsValues(reportUri, parameters);
    }

    private ReportService getDelegate() throws ServiceException {
        if (mDelegate == null) {
            mDelegate = mServiceFactory.newService();
        }
        return mDelegate;
    }
}
