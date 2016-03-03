/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
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

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class DefaultExceptionMapperTest extends BaseExceptionMapperTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setExceptionMapper(DefaultExceptionMapper.getInstance());
    }

    @Test
    public void testTransform500HttpException() throws Exception {
        givenHttpError(500);

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.INTERNAL_ERROR);
        thenShouldHaveMessage("Server encountered unexpected error");
    }

    @Test
    public void testTransform404HttpException() throws Exception {
        givenHttpError(404);

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.CLIENT_ERROR);
        thenShouldHaveMessage("Service exist but requested entity not found");
    }

    @Test
    public void testTransform400HttpException() throws Exception {
        givenHttpError(400);

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.CLIENT_ERROR);
        thenShouldHaveMessage("Some parameters in request not valid");
    }

    @Test
    public void testTransform403HttpException() throws Exception {
        givenHttpError(403);

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.PERMISSION_DENIED_ERROR);
        thenShouldHaveMessage("User has no access to resource");
    }

    @Test
    public void testTransform401HttpException() throws Exception {
        givenHttpError(401);

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.AUTHORIZATION_ERROR);
        thenShouldHaveMessage("User is not authorized");
    }

    @Test
    public void testTransformWithDescriptorWithMissingKey() throws Exception {
        givenHttpErrorWithDescriptor(403);
        givenErrorDescriptorByCode("missing.key");

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.PERMISSION_DENIED_ERROR);
    }

    @Test
    public void testTransformWithDescriptorWithResourceNotFound() throws Exception {
        givenHttpErrorWithDescriptor(400);
        givenErrorDescriptorByCode("resource.not.found");

        whenTransformsHttpException();

        thenShouldHaveStatusCode(StatusCodes.RESOURCE_NOT_FOUND);
    }

    @Test
    public void testTransformWillHandleIOExceptionForDescriptorMapping() throws Exception {
        givenIOError();

        whenTransformsIOException();

        thenShouldHaveStatusCode(StatusCodes.NETWORK_ERROR);
    }
}