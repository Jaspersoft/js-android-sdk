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

package com.jaspersoft.android.sdk.service.data.server;


import org.jetbrains.annotations.NotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public enum ServerVersion {
    UNKNOWN(0d),
    v5(5.0d),
    v5_2(5.2d),
    v5_5(5.5d),
    v5_6(5.6d),
    v5_6_1(5.61d),
    v6(6.0d),
    v6_0_1(6.01d),
    v6_1(6.1d),
    v6_1_1(6.11d);

    private double mVersionCode;
    private String mRawValue;

    ServerVersion(double versionCode) {
        this.mVersionCode = versionCode;
    }

    void setVersionCode(double versionCode) {
        mVersionCode = versionCode;
    }

    void setRawValue(String rawValue) {
        mRawValue = rawValue;
    }

    public double code() {
        return mVersionCode;
    }

    @NotNull
    public String rawCode() {
        return mRawValue;
    }

    @NotNull
    public static ServerVersion parse(String versionName) {
        return DefaultVersionParser.INSTANCE.parse(versionName);
    }
}
