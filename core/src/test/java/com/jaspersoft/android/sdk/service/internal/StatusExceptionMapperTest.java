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
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.exception.StatusException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.SocketTimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class StatusExceptionMapperTest {
    @Mock
    HttpException mHttpException;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTransformIOException() throws Exception {
        IOException ioException = new IOException("Socket timed out", new SocketTimeoutException());
        StatusException statusException = StatusExceptionMapper.transform(ioException);
        assertThat(statusException.code(), is(StatusCodes.NETWORK_ERROR));
        assertThat(statusException.getMessage(), is("Socket timed out"));
        assertThat(statusException.getCause(), is(instanceOf(SocketTimeoutException.class)));
    }

    @Test
    public void testTransformHttpException() throws Exception {
        // TODO implement cases
    }
}