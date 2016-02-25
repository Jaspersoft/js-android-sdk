package com.jaspersoft.android.sdk.service.internal;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptorItem;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobExceptionMapper implements ServiceExceptionMapper {
    private final ServiceExceptionMapper mDelegate;

    public JobExceptionMapper(ServiceExceptionMapper delegate) {
        mDelegate = delegate;
    }

    @NotNull
    @Override
    public ServiceException transform(HttpException e) {
        try {
            ErrorDescriptor descriptor = e.getDescriptor();
            if (descriptor == null) {
                return mDelegate.transform(e);
            } else {
                return mapDescriptorToState(e, descriptor);
            }
        } catch (IOException ioEx) {
            return transform(ioEx);
        }
    }

    private ServiceException mapDescriptorToState(HttpException e, ErrorDescriptor descriptor) {
        if (descriptor.getErrorCodes().contains("error.duplicate.report.job.output.filename")) {
            return new ServiceException("Duplicate job output file name", e, StatusCodes.JOB_DUPLICATE_OUTPUT_FILE_NAME);
        } else if (descriptor.getErrorCodes().contains("error.before.current.date")) {
            return new ServiceException("Start date cannot be in the past", e, StatusCodes.JOB_START_DATE_IN_THE_PAST);
        } else if (descriptor.getErrorCodes().contains("error.report.job.output.folder.inexistent")) {
            return new ServiceException("Output folder does not exist", e, StatusCodes.JOB_OUTPUT_FOLDER_DOES_NOT_EXIST);
        } else if (descriptor.getErrorCodes().contains("error.report.job.output.folder.notwriteable")) {
            return new ServiceException("Output folder does not exist", e, StatusCodes.JOB_OUTPUT_FOLDER_IS_NOT_WRITABLE);
        } else if (descriptor.getErrorCodes().contains("error.invalid.chars")) {
            ErrorDescriptorItem innerError = descriptor.getInnerError("error.invalid.chars");
            String field = innerError.getField();

            if ("baseOutputFilename".equals(field)) {
                return new ServiceException("Start date cannot be in the past", e, StatusCodes.JOB_OUTPUT_FILENAME_INVALID_CHARS);
            }
        } else if (descriptor.getErrorCodes().contains("error.length")) {
            ErrorDescriptorItem innerError = descriptor.getInnerError("error.length");

            String field = innerError.getField();
            List<String> errorArguments = innerError.getErrorArguments();
            String errorArgument = innerError.getErrorArgument(0);

            if ("label".equals(field)) {
                ServiceException exception = new ServiceException("Maximum length is " + errorArgument, e, StatusCodes.JOB_LABEL_TOO_LONG);
                exception.setArguments(errorArguments);
                return exception;
            } else if ("baseOutputFilename".equals(field)) {
                ServiceException exception = new ServiceException("Maximum length is " + errorArgument, e, StatusCodes.JOB_OUTPUT_FILENAME_TOO_LONG);
                exception.setArguments(errorArguments);
                return exception;
            }
        }

        return mDelegate.transform(e);
    }

    @NotNull
    @Override
    public ServiceException transform(IOException e) {
        return mDelegate.transform(e);
    }
}
