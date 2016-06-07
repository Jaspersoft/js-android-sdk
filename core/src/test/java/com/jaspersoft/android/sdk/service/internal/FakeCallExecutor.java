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
import com.jaspersoft.android.sdk.service.exception.ServiceException;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class FakeCallExecutor implements CallExecutor {
    private final AbstractServiceExceptionMapper mExMapper;

    public FakeCallExecutor() {
        mExMapper = DefaultExceptionMapper.getInstance();
    }

    @Override
    public <T> T execute(Call<T> call) throws ServiceException {
        try {
            return call.perform();
        } catch (IOException e) {
            throw mExMapper.transform(e);
        } catch (HttpException e) {
            throw mExMapper.transform(e);
        }
    }
}
