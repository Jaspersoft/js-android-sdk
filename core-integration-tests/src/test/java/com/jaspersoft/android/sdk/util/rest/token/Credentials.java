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

package com.jaspersoft.android.sdk.util.rest.token;

import com.jaspersoft.android.sdk.util.rest.exception.HttpException;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public abstract class Credentials {
    protected abstract String delegateSelf(TokenFactory factory) throws IOException, HttpException;

    public static class Factory {
        public static Credentials create(String data) {
            String[] items = data.split("\\|");
            String type = items[0];
            if ("spring".equals(type)) {
                return SpringCredentials.builder()
                        .setUsername(items[1])
                        .setPassword(items[2])
                        .setOrganization("null".equals(items[3]) ? null : items[3])
                        .create();
            }
            throw new UnsupportedOperationException("Test could not create credential of type: " + type);
        }
    }
}
