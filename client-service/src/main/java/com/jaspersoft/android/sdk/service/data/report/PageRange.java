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

package com.jaspersoft.android.sdk.service.data.report;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class PageRange {
    private final int mLowerBound;
    private final int mUpperBound;

    public PageRange(String rawRange) {
        mLowerBound = parseLowerBound(rawRange);
        mUpperBound = parseUpperBound(rawRange);
    }

    public int getLowerBound() {
        return mLowerBound;
    }

    public int getUpperBound() {
        return mUpperBound;
    }

    public boolean isRange() {
        return mUpperBound != Integer.MAX_VALUE;
    }

    private int parseLowerBound(String rawRange) {
        String[] split = rawRange.split("-");
        return Integer.parseInt(split[0]);
    }

    private int parseUpperBound(String rawRange) {
        String[] split = rawRange.split("-");
        if (split.length == 1) {
            return Integer.MAX_VALUE;
        }

        return Integer.parseInt(split[1]);
    }
}
