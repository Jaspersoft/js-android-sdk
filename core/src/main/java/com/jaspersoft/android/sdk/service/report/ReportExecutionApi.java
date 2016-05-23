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

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class ReportExecutionApi {
    private final ServiceExceptionMapper mExceptionMapper;
    private final ReportExecutionRestApi mReportExecutionRestApi;
    private final ReportOptionsMapper mReportOptionsMapper;

    ReportExecutionApi(
            ServiceExceptionMapper exceptionMapper,
            ReportExecutionRestApi reportExecutionRestApi,
            ReportOptionsMapper reportOptionsMapper) {
        mExceptionMapper = exceptionMapper;
        mReportExecutionRestApi = reportExecutionRestApi;
        mReportOptionsMapper = reportOptionsMapper;
    }

    public ReportExecutionDescriptor start(String reportUri, ReportExecutionOptions execOptions) throws ServiceException {
        ReportExecutionRequestOptions options = mReportOptionsMapper.transform(reportUri, execOptions);
        try {
            return mReportExecutionRestApi.runReportExecution(options);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public void update(String executionId, List<ReportParameter> params) throws ServiceException {
        try {
            mReportExecutionRestApi.updateReportExecution(executionId, params);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public ReportExecutionDescriptor getDetails(String execId) throws ServiceException {
        try {
            return mReportExecutionRestApi.requestReportExecutionDetails(execId);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public ExecutionStatus awaitStatus(String execId, String reportUri, long delay, Status... statuses) throws ServiceException {
        ExecutionStatus nextDetails;
        ErrorDescriptor descriptor;
        Status status;

        List<Status> expectedStatuses = Arrays.asList(statuses);
        do {
            nextDetails = getStatus(execId);
            descriptor = nextDetails.getErrorDescriptor();
            status = Status.wrap(nextDetails.getStatus());

            if (status.isCancelled()) {
                throw new ServiceException(
                        String.format("Report '%s' execution cancelled", reportUri), null,
                        StatusCodes.REPORT_EXECUTION_CANCELLED);
            }
            if (status.isFailed()) {
                String message = "Failed to perform report execute";
                if (descriptor != null) {
                    message = descriptor.getMessage();
                }
                throw new ServiceException(message, null, StatusCodes.REPORT_EXECUTION_FAILED);
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                throw new ServiceException("Unexpected error", ex, StatusCodes.UNDEFINED_ERROR);
            }
        } while (!expectedStatuses.contains(status));

        return nextDetails;
    }

    private ExecutionStatus getStatus(String execId) throws ServiceException {
        try {
            return mReportExecutionRestApi.requestReportExecutionStatus(execId);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }
}
