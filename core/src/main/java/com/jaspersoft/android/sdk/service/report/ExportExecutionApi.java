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

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.export.ExportOutputResource;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.jaspersoft.android.sdk.service.data.report.ReportExportOutput;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class ExportExecutionApi {
    private final ServiceExceptionMapper mExceptionMapper;
    private final ReportExportRestApi mReportExportRestApi;
    private final ExportOptionsMapper mExportOptionsMapper;

    private final ReportExportMapper mReportExportMapper;
    private final AttachmentExportMapper mAttachmentExportMapper;

    ExportExecutionApi(ServiceExceptionMapper exceptionMapper,
                       ReportExportRestApi reportExportRestApi,
                       ExportOptionsMapper exportOptionsMapper,
                       ReportExportMapper reportExportMapper,
                       AttachmentExportMapper attachmentExportMapper) {
        mExceptionMapper = exceptionMapper;
        mReportExportRestApi = reportExportRestApi;
        mExportOptionsMapper = exportOptionsMapper;
        mReportExportMapper = reportExportMapper;
        mAttachmentExportMapper = attachmentExportMapper;
    }

    public ExportExecutionDescriptor start(String execId, ReportExportOptions reportExportOptions) throws ServiceException {
        ExecutionRequestOptions options = mExportOptionsMapper.transform(reportExportOptions);
        try {
            return mReportExportRestApi.runExportExecution(execId, options);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public ExecutionStatus awaitReadyStatus(String execId, String exportId, String reportUri, long delay) throws ServiceException {
        ExecutionStatus executionStatus;
        ErrorDescriptor descriptor;
        Status status;

        do {
            executionStatus = getStatus(execId, exportId);
            status = Status.wrap(executionStatus.getStatus());
            descriptor = executionStatus.getErrorDescriptor();
            if (status.isCancelled()) {
                throw new ServiceException(
                        String.format("Export for report '%s' execution cancelled", reportUri), null,
                        StatusCodes.EXPORT_EXECUTION_CANCELLED);
            }
            if (status.isFailed()) {
                String message = "Failed to perform report export";
                if (descriptor != null) {
                    message = descriptor.getMessage();
                }
                throw new ServiceException(message, null, StatusCodes.EXPORT_EXECUTION_FAILED);
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                throw new ServiceException("Unexpected error", ex, StatusCodes.UNDEFINED_ERROR);
            }
        } while (!status.isReady());

        return executionStatus;
    }

    public ReportExportOutput downloadExport(String execId, String exportId) throws ServiceException {
        try {
            ExportOutputResource result = mReportExportRestApi.requestExportOutput(execId, exportId);
            return mReportExportMapper.transform(result);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public ResourceOutput downloadAttachment(String execId, String exportId, String attachmentId) throws ServiceException {
        try {
            OutputResource result = mReportExportRestApi.requestExportAttachment(execId, exportId, attachmentId);
            return mAttachmentExportMapper.transform(result);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    private ExecutionStatus getStatus(String execId, String exportId) throws ServiceException {
        try {
            return mReportExportRestApi.checkExportExecutionStatus(execId, exportId);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }
}
