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
 * Following API reserved for future enhancement specific the pattern
 * The pattern can consist of the following tokens:
 * <ul>
 *    <li>
 *        a single day value between 1 and 31.
 *    </li>
 *    <li>
 *        a day value with an increment, for instance 1/5 which means that the trigger would fire every 5 days starting on 1st of the month.
 *    </li>
 *    <li>
 *        a days range, for instance 2-5 which means that the trigger should fire every on each day starting from 2nd to 5th. Day values and ranges can be concatenated using commas as separators.
 *    </li>
 *    <li>
 *        * which means the the job would fire every day.
 *    </li>
 * </ul>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class DaysInMonth extends DaysType {
    private final String mValue;

    private DaysInMonth(String value) {
        mValue = value;
    }

    @Override
    public String toString() {
        return mValue;
    }

    /**
     * Factory method that creates new pattern wrapper. Accepts any raw value.
     *
     * @param rawValue any raw value
     * @return new pattern wrapper
     */
    public static DaysInMonth valueOf(String rawValue) {
        return new DaysInMonth(rawValue);
    }
}
