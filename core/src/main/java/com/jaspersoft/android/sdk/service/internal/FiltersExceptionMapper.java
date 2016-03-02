package com.jaspersoft.android.sdk.service.internal;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptorItem;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class FiltersExceptionMapper extends AbstractServiceExceptionMapper {
    private static class SingletonHolder {
        private static final AbstractServiceExceptionMapper INSTANCE = new FiltersExceptionMapper();
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

    private FiltersExceptionMapper() {
        mDelegate = DefaultExceptionMapper.getInstance();
    }

    @Override
    protected ServiceException mapHttpCodesToState(HttpException e) {
        return mDelegate.mapHttpCodesToState(e);
    }

    @Override
    protected ServiceException mapDescriptorToState(HttpException e, ErrorDescriptor descriptor) {
        if (descriptor.getErrorCodes().contains("report.options.exception.label.exists.another.report")) {
            return new ServiceException("A Saved Options with that name already exists in this folder, under another report", e, StatusCodes.SAVED_VALUES_EXIST_IN_FOLDER);
        } else if (descriptor.getErrorCodes().contains("report.options.error.too.long.label")) {

            List<String> paramsList = new ArrayList<>(descriptor.getParameters());
            String errorArgument = paramsList.size() > 0 ? paramsList.get(0) : "";

            ServiceException exception = new ServiceException("The Saved Options label is too long. The maximum length is " + errorArgument + " characters", e, StatusCodes.SAVED_VALUES_LABEL_TOO_LONG);
            exception.setArguments(paramsList);
            return exception;
        }

        return mDelegate.transform(e);
    }
}
