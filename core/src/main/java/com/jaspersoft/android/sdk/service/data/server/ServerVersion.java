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


/**
 * @author Tom Koptel
 * @since 2.0
 */
public enum ServerVersion {
    UNKNOWN(0d),
    EMERALD(5.0d),
    EMERALD_MR1(5.2d),
    EMERALD_MR2(5.5d),
    EMERALD_MR3(5.6d),
    EMERALD_MR4(5.61d),
    AMBER(6.0d),
    AMBER_MR1(6.01d),
    AMBER_MR2(6.1d);

    private double mVersionCode;
    private String mRawValue;

    ServerVersion(double versionCode) {
        this.mVersionCode = versionCode;
    }

    public String getRawValue() {
        return mRawValue;
    }

    public double getVersionCode() {
        return mVersionCode;
    }

    void setVersionCode(double versionCode) {
        mVersionCode = versionCode;
    }

    void setRawValue(String rawValue) {
        mRawValue = rawValue;
    }

    public static Parser defaultParser() {
        return DefaultVersionParser.INSTANCE;
    }

    public interface Parser {
        ServerVersion parse(String rawVersion);
    }
}
