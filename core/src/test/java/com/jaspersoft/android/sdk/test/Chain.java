/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.test;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class Chain<Response> implements Answer<Response> {
    private final Response[] mChain;
    private int invocationCount = 0;

    private Chain(Response... chain) {
        mChain = chain;
    }

    public static <Response> Chain<Response> of(Response... values) {
        return new Chain<>(values);
    }

    @Override
    public Response answer(InvocationOnMock invocation) throws Throwable {
        int statusIndex = invocationCount;
        if (statusIndex >= mChain.length) {
            statusIndex = mChain.length - 1;
        }
        invocationCount++;
        return mChain[statusIndex];
    }
}
