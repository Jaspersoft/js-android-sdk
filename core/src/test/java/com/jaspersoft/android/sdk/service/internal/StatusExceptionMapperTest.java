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

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptor;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.exception.StatusException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.SocketTimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ErrorDescriptor.class})
public class StatusExceptionMapperTest {
    @Mock
    HttpException mHttpException;
    @Mock
    ErrorDescriptor mDescriptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTransformIOException() throws Exception {
        IOException ioException = new IOException("Socket timed out", new SocketTimeoutException());
        StatusException statusException = StatusExceptionMapper.transform(ioException);
        assertThat(statusException.code(), is(StatusCodes.NETWORK_ERROR));
        assertThat(statusException.getMessage(), is("Failed to perform network request. Check network!"));
        assertThat(statusException.getCause(), is(instanceOf(IOException.class)));
    }

    @Test
    public void testTransform500HttpException() throws Exception {
        when(mHttpException.code()).thenReturn(500);
        when(mHttpException.getDescriptor()).thenReturn(null);

        StatusException statusException = StatusExceptionMapper.transform(mHttpException);
        assertThat(statusException.code(), is(StatusCodes.INTERNAL_ERROR));
        assertThat(statusException.getMessage(), is("Server encountered unexpected error"));
        assertThat(statusException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void testTransform404HttpException() throws Exception {
        when(mHttpException.code()).thenReturn(404);
        when(mHttpException.getDescriptor()).thenReturn(null);

        StatusException statusException = StatusExceptionMapper.transform(mHttpException);
        assertThat(statusException.code(), is(StatusCodes.CLIENT_ERROR));
        assertThat(statusException.getMessage(), is("Service exist but requested entity not found"));
        assertThat(statusException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void testTransform400HttpException() throws Exception {
        when(mHttpException.code()).thenReturn(400);
        when(mHttpException.getDescriptor()).thenReturn(null);

        StatusException statusException = StatusExceptionMapper.transform(mHttpException);
        assertThat(statusException.code(), is(StatusCodes.CLIENT_ERROR));
        assertThat(statusException.getMessage(), is("Some parameters in request not valid"));
        assertThat(statusException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void testTransform403HttpException() throws Exception {
        when(mHttpException.code()).thenReturn(403);
        when(mHttpException.getDescriptor()).thenReturn(null);

        StatusException statusException = StatusExceptionMapper.transform(mHttpException);
        assertThat(statusException.code(), is(StatusCodes.PERMISSION_DENIED_ERROR));
        assertThat(statusException.getMessage(), is("User has no access to resource"));
        assertThat(statusException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void testTransform401HttpException() throws Exception {
        when(mHttpException.code()).thenReturn(401);
        when(mHttpException.getDescriptor()).thenReturn(null);

        StatusException statusException = StatusExceptionMapper.transform(mHttpException);
        assertThat(statusException.code(), is(StatusCodes.AUTHORIZATION_ERROR));
        assertThat(statusException.getMessage(), is("User is not authorized"));
        assertThat(statusException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void testTransformWithDescriptorWithMissingKey() throws IOException {
        when(mDescriptor.getErrorCode()).thenReturn("missing.key");
        when(mHttpException.code()).thenReturn(403);
        when(mHttpException.getDescriptor()).thenReturn(mDescriptor);

        StatusException statusException = StatusExceptionMapper.transform(mHttpException);
        assertThat(statusException.code(), is(StatusCodes.PERMISSION_DENIED_ERROR));
        assertThat(statusException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void testTransformWillHandleIOExceptionForDescriptorMapping() throws IOException {
        when(mDescriptor.getErrorCode()).thenReturn("missing.key");
        when(mHttpException.code()).thenReturn(403);
        when(mHttpException.getDescriptor()).thenThrow(new IOException("Failed IO"));

        StatusException statusException = StatusExceptionMapper.transform(mHttpException);
        assertThat(statusException.code(), is(StatusCodes.NETWORK_ERROR));
        assertThat(statusException.getCause(), is(instanceOf(IOException.class)));
    }
}