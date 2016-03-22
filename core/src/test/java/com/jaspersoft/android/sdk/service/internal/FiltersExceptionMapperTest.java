package com.jaspersoft.android.sdk.service.internal;

import com.jaspersoft.android.sdk.service.exception.StatusCodes;

import org.junit.Before;
import org.junit.Test;

public class FiltersExceptionMapperTest extends BaseExceptionMapperTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setExceptionMapper(FiltersExceptionMapper.getInstance());
    }

    @Test
    public void should_transform_saved_values_label_duplication() throws Exception {
        givenHttpErrorWithDescriptor(400);
        givenErrorDescriptorByCode("report.options.exception.label.exists.another.report");

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.SAVED_VALUES_EXIST_IN_FOLDER);
    }

    @Test
    public void should_transform_saved_values_label_too_long() throws Exception {
        givenHttpErrorWithDescriptor(400);
        givenErrorDescriptorByCode("report.options.error.too.long.label");
        givenErrorDescriptorWithParams("100");

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.SAVED_VALUES_LABEL_TOO_LONG);
    }


}