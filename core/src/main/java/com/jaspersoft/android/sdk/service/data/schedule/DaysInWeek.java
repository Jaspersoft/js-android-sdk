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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class DaysInWeek extends DaysType {
    private final Set<Integer> mDays;

    private DaysInWeek(Set<Integer> days) {
        mDays = days;
    }

    public static DaysInWeek create(Set<Integer> days) {
        return new DaysInWeek(days);
    }

    public static DaysInWeek create(Integer... days) {
        return new DaysInWeek(new HashSet<>(Arrays.asList(days)));
    }

    public Set<Integer> getDays() {
        return mDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DaysInWeek that = (DaysInWeek) o;

        if (mDays != null ? !mDays.equals(that.mDays) : that.mDays != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mDays != null ? mDays.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DaysInWeek{" +
                "days=" + mDays +
                '}';
    }
}
