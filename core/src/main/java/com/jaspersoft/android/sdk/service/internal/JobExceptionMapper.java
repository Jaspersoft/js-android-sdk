package com.jaspersoft.android.sdk.service.internal;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptorItem;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;

import java.util.List;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobExceptionMapper extends AbstractServiceExceptionMapper {
    private static class SingletonHolder {
        private static final AbstractServiceExceptionMapper INSTANCE = new JobExceptionMapper();
    }

    /**
     * Initialization-on-demand holder idiom
     *
     * <a href="https://en.wikipedia.org/wiki/Singleton_pattern">SOURCE</a>

     * @return job exception mapper
     */
    public static AbstractServiceExceptionMapper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private final AbstractServiceExceptionMapper mDelegate;

    private JobExceptionMapper() {
        mDelegate = DefaultExceptionMapper.getInstance();
    }

    @Override
    protected ServiceException mapHttpCodesToState(HttpException e) {
        return mDelegate.mapHttpCodesToState(e);
    }

    @Override
    protected ServiceException mapDescriptorToState(HttpException e, ErrorDescriptor descriptor) {
        Set<String> errorCodes = descriptor.getErrorCodes();

        if (errorCodes.contains("error.duplicate.report.job.output.filename")) {
            return new ServiceException("Duplicate job output file name", e, StatusCodes.JOB_DUPLICATE_OUTPUT_FILE_NAME);
        } else if (errorCodes.contains("error.before.current.date")) {
            return new ServiceException("Start date cannot be in the past", e, StatusCodes.JOB_START_DATE_IN_THE_PAST);
        } else if (errorCodes.contains("error.report.job.output.folder.inexistent")) {
            return new ServiceException("Output folder does not exist", e, StatusCodes.JOB_OUTPUT_FOLDER_DOES_NOT_EXIST);
        } else if (errorCodes.contains("error.report.job.output.folder.notwriteable")) {
            return new ServiceException("Output folder does not exist", e, StatusCodes.JOB_OUTPUT_FOLDER_IS_NOT_WRITABLE);
        } else if (errorCodes.contains("error.invalid.chars")) {
            ErrorDescriptorItem innerError = descriptor.getInnerError("error.invalid.chars");
            String field = innerError.getField();

            if ("baseOutputFilename".equals(field)) {
                return new ServiceException("Start date cannot be in the past", e, StatusCodes.JOB_OUTPUT_FILENAME_INVALID_CHARS);
            }
        } else if (errorCodes.contains("error.length")) {
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
        } else if (errorCodes.contains("error.not.empty")) {
            ErrorDescriptorItem innerError = descriptor.getInnerError("error.not.empty");
            String field = innerError.getField();

            if ("trigger.weekDays".equals(field)) {
                return new ServiceException("At lease one day from week should be specified", e, StatusCodes.JOB_TRIGGER_WEEK_DAYS_EMPTY);
            } else if ("trigger.months".equals(field)) {
                return new ServiceException("At lease one month should be specified", e, StatusCodes.JOB_TRIGGER_MONTHS_EMPTY);
            }
        } else if (errorCodes.contains("unexpected.error")) {
            return new ServiceException(descriptor.getMessage(), e, StatusCodes.JOB_CREATION_INTERNAL_ERROR);
        } else if (errorCodes.contains("error.pattern")) {
            ErrorDescriptorItem innerError = descriptor.getInnerError("error.pattern");
            String field = innerError.getField();

            if ("trigger.monthDays".equals(field)) {
                return new ServiceException("Wrong format for days in the month for calendar recurrence", e, StatusCodes.JOB_CALENDAR_PATTERN_ERROR_DAYS_IN_MONTH);
            }
        }

        return mDelegate.transform(e);
    }
}
