/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.network.api;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ResponseBody.class})
public class RetrofitOutputResourceTest {

    private RetrofitOutputResource objectUnderTest;
    private ResponseBody mTypedInput;
    @Mock
    private InputStream input;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mTypedInput = PowerMockito.mock(ResponseBody.class);
        objectUnderTest = new RetrofitOutputResource(mTypedInput);
    }

    @Test
    public void shouldDelegateMimeType() {
        when(mTypedInput.contentType()).thenReturn(MediaType.parse("multipart/form-data"));
        objectUnderTest.getMimeType();
        verify(mTypedInput, times(1)).contentType();
    }

    @Test
    public void shouldDelegateLength()  throws IOException {
        objectUnderTest.getLength();
        verify(mTypedInput, times(1)).contentLength();
    }

    @Test
    public void shouldDelegateInputStream() throws IOException {
        when(mTypedInput.byteStream()).thenReturn(input);
        objectUnderTest.getStream();
        verify(mTypedInput, times(1)).byteStream();
    }
}
