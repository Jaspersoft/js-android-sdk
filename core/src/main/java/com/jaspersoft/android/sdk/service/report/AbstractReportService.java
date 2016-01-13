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
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
abstract class AbstractReportService extends ReportService {
    protected final ExportExecutionApi mExportExecutionApi;
    protected final ReportExecutionApi mReportExecutionApi;
    protected final ReportOptionsUseCase mReportOptionsUseCase;
    protected final ControlsApi mControlsApi;
    protected final ExportFactory mExportFactory;
    protected final long mDelay;

    protected AbstractReportService(ExportExecutionApi exportExecutionApi,
                                    ReportExecutionApi reportExecutionApi,
                                    ReportOptionsUseCase reportOptionsUseCase,
                                    ControlsApi controlsApi,
                                    ExportFactory exportFactory,
                                    long delay) {
        mExportExecutionApi = exportExecutionApi;
        mReportExecutionApi = reportExecutionApi;
        mReportOptionsUseCase = reportOptionsUseCase;
        mControlsApi = controlsApi;
        mExportFactory = exportFactory;
        mDelay = delay;
    }

    @NotNull
    @Override
    public final ReportExecution run(@NotNull String reportUri, @NotNull ReportExecutionOptions execOptions) throws ServiceException {
        ReportExecutionDescriptor descriptor = mReportExecutionApi.start(reportUri, execOptions);
        String executionId = descriptor.getExecutionId();
        mReportExecutionApi.awaitStatus(executionId, reportUri, mDelay, Status.execution(), Status.ready());

        return buildExecution(reportUri, executionId, execOptions);
    }

    @NotNull
    @Override
    public final List<InputControl> listControls(@NotNull String reportUri) throws ServiceException {
        return mControlsApi.requestControls(reportUri, false);
    }

    @NotNull
    @Override
    public final List<InputControlState> listControlsValues(@NotNull String reportUri,
                                                            @NotNull List<ReportParameter> parameters) throws ServiceException {
        return mControlsApi.requestControlsValues(reportUri, parameters, true);
    }

    @NotNull
    @Override
    public final Set<ReportOption> listReportOptions(@NotNull String reportUnitUri) throws ServiceException {
        return mReportOptionsUseCase.requestReportOptionsList(reportUnitUri);
    }

    @NotNull
    @Override
    public final ReportOption createReportOption(@NotNull String reportUri,
                                           @NotNull String optionLabel,
                                           @NotNull List<ReportParameter> parameters,
                                           boolean overwrite) throws ServiceException {
        return mReportOptionsUseCase.createReportOption(reportUri, optionLabel, parameters, overwrite);
    }

    @Override
    public void updateReportOption(@NotNull String reportUri, @NotNull String optionId, @NotNull List<ReportParameter> parameters) throws ServiceException {
        mReportOptionsUseCase.updateReportOption(reportUri, optionId, parameters);
    }

    @Override
    public void deleteReportOption(@NotNull String reportUri, @NotNull String optionId) throws ServiceException {
        mReportOptionsUseCase.deleteReportOption(reportUri, optionId);
    }

    protected abstract ReportExecution buildExecution(String reportUri, String executionId, ReportExecutionOptions criteria);
}