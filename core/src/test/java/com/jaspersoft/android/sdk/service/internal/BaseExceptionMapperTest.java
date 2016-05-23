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


import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptorItem;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.collections.Sets;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public abstract class BaseExceptionMapperTest {
    @Mock
    HttpException mHttpException;
    @Mock
    ErrorDescriptor mDescriptor;
    @Mock
    ErrorDescriptorItem mDescriptorItem;
    @Mock
    ServiceExceptionMapper mDelegate;

    ServiceExceptionMapper mExceptionMapper;
    ServiceException mServiceException;
    IOException mIOException;

    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    public void setExceptionMapper(ServiceExceptionMapper exceptionMapper) {
        mExceptionMapper = exceptionMapper;
    }

    void givenIOError() {
        mIOException = new IOException("Failed IO");
    }

    void givenHttpError(int code) throws Exception {
        when(mHttpException.code()).thenReturn(code);
    }

    void givenHttpErrorWithDescriptor(int code) throws Exception {
        when(mHttpException.code()).thenReturn(code);
        when(mHttpException.getDescriptor()).thenReturn(mDescriptor);
    }

    void givenErrorDescriptorByCode(String errorCode) {
        when(mDescriptor.getErrorCodes()).thenReturn(Collections.singleton(errorCode));
        when(mDescriptor.getInnerError(errorCode)).thenReturn(mDescriptorItem);
    }

    void givenErrorDescriptorWithField(String field) {
        when(mDescriptorItem.getField()).thenReturn(field);
    }

    void givenErrorDescriptorWithArguments(String... args) {
        when(mDescriptorItem.getErrorArguments()).thenReturn(Arrays.asList(args));
    }

    void givenErrorDescriptorWithParams(String... params) {
        when(mDescriptor.getParameters()).thenReturn(Sets.newSet(params));
    }

    void whenTransformsHttpException() {
        mServiceException = mExceptionMapper.transform(mHttpException);
    }

    void whenTransformsIOException() {
        mServiceException = mExceptionMapper.transform(mIOException);
    }

    void thenShouldHaveStatusCode(int code) {
        assertThat(mServiceException.code(), is(code));
    }

    void thenShouldHaveMessage(String message) {
        assertThat(mServiceException.getMessage(), is(message));
    }
}
