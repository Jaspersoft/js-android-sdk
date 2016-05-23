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

package com.jaspersoft.android.sdk.service.data.server;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tom Koptel
 * @since 2.3
 */
final class VersionParser {

    private static final double INVALID_VERSION = -1d;

    private VersionParser() {
    }

    public static double toDouble(String version) {
        if (version != null) {
            try {
                return Double.parseDouble(version);
            } catch (NumberFormatException ex) {
                if (version.contains(".")) {
                    return parseAsVersionName(version);
                } else {
                    return parseAsNumber(version);
                }
            }
        }
        return INVALID_VERSION;
    }

    private static double parseAsNumber(String version) {
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(version);
        if (matcher.find()) {
            return Double.valueOf(matcher.group());
        }
        return INVALID_VERSION;
    }

    private static double parseAsVersionName(String version) {
        double versionCode;
        String[] subs = version.split("\\.");

        BigDecimal decimalSubVersion, decimalFactor, decimalResult;
        BigDecimal decimalVersion = new BigDecimal("0");
        for (int i = 0; i < subs.length; i++) {
            try {
                decimalSubVersion = new BigDecimal(Integer.parseInt(subs[i]));
            } catch (NumberFormatException ex) {
                return INVALID_VERSION;
            }

            decimalFactor = new BigDecimal(String.valueOf(Math.pow(10, i * -1)));
            decimalResult = decimalSubVersion.multiply(decimalFactor);
            decimalVersion = decimalVersion.add(decimalResult);
        }
        versionCode = decimalVersion.doubleValue();
        return versionCode;
    }
}