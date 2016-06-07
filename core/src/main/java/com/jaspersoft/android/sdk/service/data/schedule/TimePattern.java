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

package com.jaspersoft.android.sdk.service.data.schedule;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class TimePattern {
    private final int mLowerBound;
    private final int mHigherBound;
    private final StringBuilder mPattern;

    TimePattern(int lowerBound, int higherBound) {
        mLowerBound = lowerBound;
        mHigherBound = higherBound;
        mPattern = new StringBuilder();
    }

    public void setRange(int start, int end) {
        if (start < mLowerBound) {
            throw new IllegalArgumentException("start cannot be less than lower bound.");
        }
        if (end > mHigherBound) {
            throw new IllegalArgumentException("end cannot be more than upper bound.");
        }
        if (start > end) {
            throw new IllegalArgumentException("start must be lesser than end.");
        }
        mPattern.setLength(0);
        mPattern.append(String.valueOf(start))
                .append("-")
                .append(String.valueOf(end));
    }

    @Override
    public String toString() {
        return mPattern.toString();
    }

    public void setValue(int value) {
        validateWithinBounds(value, String.format("Value should be within bounds [ %d, %d ]", mLowerBound, mHigherBound));
        mPattern.setLength(0);
        mPattern.append(String.valueOf(value));
    }

    public void setIncrement(int interval, int from) {
        validateWithinBounds(interval, String.format("Interval should be within bounds [ %d, %d ]", mLowerBound, mHigherBound));
        validateWithinBounds(from, String.format("From should be within bounds [ %d, %d ]", mLowerBound, mHigherBound));
        mPattern.setLength(0);
        mPattern.append(String.valueOf(interval))
                .append("/")
                .append(String.valueOf(from));
    }

    public void setRawValue(String rawValue) {
        mPattern.setLength(0);
        mPattern.append(rawValue);
    }

    private void validateWithinBounds(int value, String message) {
        if (value < mLowerBound || value > mHigherBound) {
            throw new IllegalArgumentException(message);
        }
    }

    public String parse(String rawValue) {
        // TODO provide parsing logic
        return rawValue;
    }
}
