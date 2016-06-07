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