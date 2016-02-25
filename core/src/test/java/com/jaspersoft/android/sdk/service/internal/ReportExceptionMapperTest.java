package com.jaspersoft.android.sdk.service.internal;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ReportExceptionMapperTest extends BaseExceptionMapperTest {
    @Before
    public void setUp() throws Exception {
        super.setUp();
        setExceptionMapper(new ReportExceptionMapper(mDelegate));
    }

    @Test
    public void should_override_resource_not_found_error() throws Exception {
        givenHttpErrorWithDescriptor(400);
        givenErrorDescriptorByCode("resource.not.found");
        givenDelegateReturnsCode(StatusCodes.RESOURCE_NOT_FOUND);

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.REPORT_EXECUTION_INVALID);
    }

    private void givenDelegateReturnsCode(int code) {
        when(mDelegate.transform(any(HttpException.class))).thenReturn(new ServiceException(null, null, code));
    }
}