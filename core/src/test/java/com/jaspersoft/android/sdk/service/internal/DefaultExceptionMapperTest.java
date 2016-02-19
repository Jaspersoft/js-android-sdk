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
import com.jaspersoft.android.sdk.service.exception.ServiceException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collections;

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
public class DefaultExceptionMapperTest {
    @Mock
    HttpException mHttpException;
    @Mock
    ErrorDescriptor mDescriptor;

    private DefaultExceptionMapper defaultExceptionMapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        defaultExceptionMapper = new DefaultExceptionMapper();
    }

    @Test
    public void testTransformIOException() throws Exception {
        IOException ioException = new IOException("Socket timed out", new SocketTimeoutException());
        ServiceException serviceException = defaultExceptionMapper.transform(ioException);
        assertThat(serviceException.code(), is(StatusCodes.NETWORK_ERROR));
        assertThat(serviceException.getMessage(), is("Failed to perform network request. Check network!"));
        assertThat(serviceException.getCause(), is(instanceOf(IOException.class)));
    }

    @Test
    public void testTransform500HttpException() throws Exception {
        when(mHttpException.code()).thenReturn(500);
        when(mHttpException.getDescriptor()).thenReturn(null);

        ServiceException serviceException = defaultExceptionMapper.transform(mHttpException);
        assertThat(serviceException.code(), is(StatusCodes.INTERNAL_ERROR));
        assertThat(serviceException.getMessage(), is("Server encountered unexpected error"));
        assertThat(serviceException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void testTransform404HttpException() throws Exception {
        when(mHttpException.code()).thenReturn(404);
        when(mHttpException.getDescriptor()).thenReturn(null);

        ServiceException serviceException = defaultExceptionMapper.transform(mHttpException);
        assertThat(serviceException.code(), is(StatusCodes.CLIENT_ERROR));
        assertThat(serviceException.getMessage(), is("Service exist but requested entity not found"));
        assertThat(serviceException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void testTransform400HttpException() throws Exception {
        when(mHttpException.code()).thenReturn(400);
        when(mHttpException.getDescriptor()).thenReturn(null);

        ServiceException serviceException = defaultExceptionMapper.transform(mHttpException);
        assertThat(serviceException.code(), is(StatusCodes.CLIENT_ERROR));
        assertThat(serviceException.getMessage(), is("Some parameters in request not valid"));
        assertThat(serviceException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void testTransform403HttpException() throws Exception {
        when(mHttpException.code()).thenReturn(403);
        when(mHttpException.getDescriptor()).thenReturn(null);

        ServiceException serviceException = defaultExceptionMapper.transform(mHttpException);
        assertThat(serviceException.code(), is(StatusCodes.PERMISSION_DENIED_ERROR));
        assertThat(serviceException.getMessage(), is("User has no access to resource"));
        assertThat(serviceException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void testTransform401HttpException() throws Exception {
        when(mHttpException.code()).thenReturn(401);
        when(mHttpException.getDescriptor()).thenReturn(null);

        ServiceException serviceException = defaultExceptionMapper.transform(mHttpException);
        assertThat(serviceException.code(), is(StatusCodes.AUTHORIZATION_ERROR));
        assertThat(serviceException.getMessage(), is("User is not authorized"));
        assertThat(serviceException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void testTransformWithDescriptorWithMissingKey() throws IOException {
        when(mDescriptor.getErrorCodes()).thenReturn(Collections.singleton("missing.key"));
        when(mHttpException.code()).thenReturn(403);
        when(mHttpException.getDescriptor()).thenReturn(mDescriptor);

        ServiceException serviceException = defaultExceptionMapper.transform(mHttpException);
        assertThat(serviceException.code(), is(StatusCodes.PERMISSION_DENIED_ERROR));
        assertThat(serviceException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void testTransformWillHandleIOExceptionForDescriptorMapping() throws IOException {
        when(mHttpException.getDescriptor()).thenThrow(new IOException("Failed IO"));

        ServiceException serviceException = defaultExceptionMapper.transform(mHttpException);
        assertThat(serviceException.code(), is(StatusCodes.NETWORK_ERROR));
        assertThat(serviceException.getCause(), is(instanceOf(IOException.class)));
    }

    @Test
    public void should_transform_job_output_filename_duplication() throws Exception {
        when(mDescriptor.getErrorCodes()).thenReturn(Collections.singleton("error.duplicate.report.job.output.filename"));
        when(mHttpException.code()).thenReturn(400);
        when(mHttpException.getDescriptor()).thenReturn(mDescriptor);

        ServiceException serviceException = defaultExceptionMapper.transform(mHttpException);
        assertThat(serviceException.code(), is(StatusCodes.JOB_DUPLICATE_OUTPUT_FILE_NAME));
        assertThat(serviceException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void should_transform_job_output_folder_does_not_exist() throws Exception {
        when(mDescriptor.getErrorCodes()).thenReturn(Collections.singleton("error.report.job.output.folder.inexistent"));
        when(mHttpException.code()).thenReturn(400);
        when(mHttpException.getDescriptor()).thenReturn(mDescriptor);

        ServiceException serviceException = defaultExceptionMapper.transform(mHttpException);
        assertThat(serviceException.code(), is(StatusCodes.JOB_OUTPUT_FOLDER_DOES_NOT_EXIST));
        assertThat(serviceException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void should_transform_job_output_folder_is_not_writable() throws Exception {
        when(mDescriptor.getErrorCodes()).thenReturn(Collections.singleton("error.report.job.output.folder.notwriteable"));
        when(mHttpException.code()).thenReturn(400);
        when(mHttpException.getDescriptor()).thenReturn(mDescriptor);

        ServiceException serviceException = defaultExceptionMapper.transform(mHttpException);
        assertThat(serviceException.code(), is(StatusCodes.JOB_OUTPUT_FOLDER_IS_NOT_WRITABLE));
        assertThat(serviceException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void should_transform_job_output_filename_invalid_chars() throws Exception {
        when(mDescriptor.getErrorCodes()).thenReturn(Collections.singleton("error.invalid.chars"));
        when(mDescriptor.getFieldByCode("error.invalid.chars")).thenReturn("baseOutputFilename");
        when(mHttpException.code()).thenReturn(400);
        when(mHttpException.getDescriptor()).thenReturn(mDescriptor);

        ServiceException serviceException = defaultExceptionMapper.transform(mHttpException);
        assertThat(serviceException.code(), is(StatusCodes.JOB_OUTPUT_FILENAME_INVALID_CHARS));
        assertThat(serviceException.getCause(), is(instanceOf(HttpException.class)));
    }

    @Test
    public void should_transform_job_start_date_in_the_past() throws Exception {
        when(mDescriptor.getErrorCodes()).thenReturn(Collections.singleton("error.before.current.date"));
        when(mHttpException.code()).thenReturn(400);
        when(mHttpException.getDescriptor()).thenReturn(mDescriptor);

        ServiceException serviceException = defaultExceptionMapper.transform(mHttpException);
        assertThat(serviceException.code(), is(StatusCodes.JOB_START_DATE_IN_THE_PAST));
        assertThat(serviceException.getCause(), is(instanceOf(HttpException.class)));
    }
}