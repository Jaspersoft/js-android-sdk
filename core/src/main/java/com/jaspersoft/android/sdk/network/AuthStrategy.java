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

package com.jaspersoft.android.sdk.network;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class AuthStrategy {
    @NotNull
    private final SpringAuthServiceFactory mSpringAuthServiceFactory;

    @Nullable
    private SpringAuthService springAuthService;

    @TestOnly
    AuthStrategy(@NotNull SpringAuthServiceFactory springAuthServiceFactory) {
        mSpringAuthServiceFactory = springAuthServiceFactory;
    }

    void apply(SpringCredentials credentials) throws IOException, HttpException {
        if (springAuthService == null) {
            springAuthService = mSpringAuthServiceFactory.create();
        }
        springAuthService.authenticate(credentials);
    }
}
