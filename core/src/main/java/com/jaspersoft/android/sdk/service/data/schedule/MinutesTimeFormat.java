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
 * Specifies the pattern that determines the minutes part of the trigger fire times.
 *
 * <ul>
 *     <li>a single minute value between 0 and 59.</li>
 *     <li>a minutes range, for instance 0-10 which means that the trigger should fire every minute starting from HH:00 to HH:10. Minute values and ranges can be concatenated using commas as separators.</li>
 *     <li>a minute value with an increment, for instance 5/10 which means that the trigger would fire every 10 minutes starting from HH:05.</li>
 *     <li>* which means the the job would fire every hour.</li>
 * </ul>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class MinutesTimeFormat {
    private static final TimePattern TIME_PATTERN = new TimePattern(0, 59);
    private final String mValue;

    private MinutesTimeFormat(String value) {
        mValue = value;
    }

    @Override
    public String toString() {
        return mValue;
    }

    @NotNull
    public static MinutesTimeFormat parse(@NotNull String rawValue) {
        return new MinutesTimeFormat(TIME_PATTERN.parse(rawValue));
    }

    public static class Builder {
        public Builder() {
            TIME_PATTERN.setRawValue("0");
        }

        public MinutesTimeFormat parse(String rawValue) {
            return new MinutesTimeFormat(rawValue);
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
         * @param startFrom starting from minute
         * @return builder for convenient configuration
         */
        public Builder withIncrement(int interval, int startFrom) {
            TIME_PATTERN.setIncrement(interval, startFrom);
            return this;
        }

        public MinutesTimeFormat build() {
            return new MinutesTimeFormat(TIME_PATTERN.toString());
        }
    }
}
