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

package com.jaspersoft.android.sdk.network.rest.v2.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import retrofit.client.Header;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class SafeHeader {
    private final Header mHeader;

    SafeHeader(@Nullable Header header) {
        mHeader = header;
    }

    public int asInt() {
        if (mHeader == null) {
            return 0;
        }
        try {
            return Integer.parseInt(mHeader.getValue());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public boolean asBoolean() {
        return mHeader != null && Boolean.parseBoolean(mHeader.getValue());
    }

    @NonNull
    public String asString() {
        if (mHeader == null) {
            return "";
        }
        String value = mHeader.getValue();
        if (value == null) {
            return "";
        }
        return value;
    }
}
