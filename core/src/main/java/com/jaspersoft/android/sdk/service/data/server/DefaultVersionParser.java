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

import java.math.BigDecimal;

/**
 * @author Tom Koptel
 * @since 2.0
 */
enum DefaultVersionParser {
    INSTANCE;

    @NotNull
    public ServerVersion parse(String rawVersion) {
        double value = convertToDouble(rawVersion);
        ServerVersion serverVersion = findReleaseByCode(value);
        serverVersion.setRawValue(rawVersion);
        return serverVersion;
    }

    private ServerVersion findReleaseByCode(final double versionCode) {
        for (ServerVersion release : ServerVersion.values()) {
            if (Double.compare(release.code(), versionCode) == 0) {
                return release;
            }
        }
        ServerVersion.UNKNOWN.setVersionCode(versionCode);
        return ServerVersion.UNKNOWN;
    }

    double convertToDouble(String version) {
        double versionCode = 0d;
        // update version code
        if (version != null) {
            String[] subs = version.split("\\.");

            BigDecimal decimalSubVersion, decimalFactor, decimalResult;
            BigDecimal decimalVersion = new BigDecimal("0");
            for (int i = 0; i < subs.length; i++) {
                try {
                    decimalSubVersion = new BigDecimal(Integer.parseInt(subs[i]));
                } catch (NumberFormatException ex) {
                    decimalSubVersion = new BigDecimal("0");
                }

                decimalFactor = new BigDecimal(String.valueOf(Math.pow(10, i * -1)));
                decimalResult = decimalSubVersion.multiply(decimalFactor);
                decimalVersion = decimalVersion.add(decimalResult);
            }
            versionCode = decimalVersion.doubleValue();
        }
        return versionCode;
    }
}