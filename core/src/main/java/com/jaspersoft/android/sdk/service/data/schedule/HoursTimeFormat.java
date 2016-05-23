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

import org.jetbrains.annotations.NotNull;

/**
 * Specifies the pattern that determines the hours at which the trigger should fire.
 *
 * <ul>
 *     <li>a single hour value between 0 and 23.</li>
 *     <li>a hours range, for instance 8-16 which means that the trigger should fire every hour starting from 8 AM to 4 PM. Hour values and ranges can be concatenated using commas as separators.</li>
 *     <li>a hour value with an increment, for instance 10/2 which means that the trigger would fire every 2 hours starting from 10 AM.</li>
 *     <li>* which means the the job would fire every hour.</li>
 * </ul>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class HoursTimeFormat {
    private static final TimePattern TIME_PATTERN = new TimePattern(0, 23);
    private final String mValue;

    private HoursTimeFormat(String value) {
        mValue = value;
    }

    @Override
    public String toString() {
        return mValue;
    }

    @NotNull
    public static HoursTimeFormat parse(@NotNull String rawValue) {
        return new HoursTimeFormat(TIME_PATTERN.parse(rawValue));
    }

    public static class Builder {
        public Builder() {
            TIME_PATTERN.setRawValue("0");
        }

        public HoursTimeFormat parse(String rawValue) {
            return new HoursTimeFormat(rawValue);
        }

        /**
         * Allows to specify range
         *
         * @param start lower bound
         * @param end upper bound
         * @return builder for convenient configuration
         */
        public Builder withRange(int start, int end) {
            TIME_PATTERN.setRange(start, end);
            return this;
        }

        /**
         * Allows to specify single value
         *
         * @param value single value
         * @return builder for convenient configuration
         */
        public Builder withSingleValue(int value) {
            TIME_PATTERN.setValue(value);
            return this;
        }

        /**
         * Allows to specify all values at once
         *
         * @return builder for convenient configuration
         */
        public Builder withAll() {
            TIME_PATTERN.setRawValue("*");
            return this;
        }

        /**
         * Allows to specify increment value
         *
         * @param interval how often
         * @param startFrom starting from hour
         * @return builder for convenient configuration
         */
        public Builder withIncrement(int interval, int startFrom) {
            TIME_PATTERN.setIncrement(interval, startFrom);
            return this;
        }

        public HoursTimeFormat build() {
            return new HoursTimeFormat(TIME_PATTERN.toString());
        }
    }
}
