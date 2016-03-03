package com.jaspersoft.android.sdk.service.internal;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptor;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public abstract class AbstractServiceExceptionMapper implements ServiceExceptionMapper {
    @NotNull
    public final ServiceException transform(IOException e) {
        return new ServiceException("Failed to perform network request. Check network!", e, StatusCodes.NETWORK_ERROR);
    }

    @NotNull
    public final ServiceException transform(HttpException e) {
        try {
            ErrorDescriptor descriptor = e.getDescriptor();
            if (descriptor == null) {
                return mapHttpCodesToState(e);
            } else {
                return mapDescriptorToState(e, descriptor);
            }
        } catch (IOException ioEx) {
            return transform(ioEx);
        }
    }

    protected abstract ServiceException mapHttpCodesToState(HttpException e);

    protected abstract ServiceException mapDescriptorToState(HttpException e, ErrorDescriptor descriptor);
}
