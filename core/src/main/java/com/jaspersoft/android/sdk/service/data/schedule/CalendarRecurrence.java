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
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class CalendarRecurrence extends Recurrence {
    @NotNull
    private final Set<Integer> mMonths;
    @Nullable
    private final DaysType mDaysType;
    @NotNull
    private final MinutesTimeFormat mMinutes;
    @NotNull
    private final HoursTimeFormat mHours;

    private CalendarRecurrence(
            @NotNull Set<Integer> months,
            @Nullable DaysType daysType,
            @NotNull MinutesTimeFormat minutes,
            @NotNull HoursTimeFormat hours
    ) {
        mMonths = months;
        mDaysType = daysType;
        mMinutes = minutes;
        mHours = hours;
    }

    @NotNull
    public Set<Integer> getMonths() {
        return mMonths;
    }

    @Nullable
    public DaysType getDaysType() {
        return mDaysType;
    }

    @NotNull
    public MinutesTimeFormat getMinutes() {
        return mMinutes;
    }

    @NotNull
    public HoursTimeFormat getHours() {
        return mHours;
    }

    public static class Builder {
        private static final HashSet<Integer> ALL_MONTHS = new HashSet<>(Arrays.asList(
                Calendar.JANUARY,
                Calendar.FEBRUARY,
                Calendar.MARCH,
                Calendar.APRIL,
                Calendar.MAY,
                Calendar.JUNE,
                Calendar.JULY,
                Calendar.AUGUST,
                Calendar.SEPTEMBER,
                Calendar.OCTOBER,
                Calendar.NOVEMBER,
                Calendar.DECEMBER
        ));

        private Set<Integer> mMonths = Collections.emptySet();
        private DaysType mDaysType;
        private MinutesTimeFormat mMinutes;
        private HoursTimeFormat mHours;

        public Builder() {
            mMonths = ALL_MONTHS;
            mMinutes = new MinutesTimeFormat.Builder().build();
            mHours = new HoursTimeFormat.Builder().build();
        }

        /**
         * Allows to specify the months on which the trigger should fire.
         *
         * @param months Use 0-indexed month indexes. I.e. 0 for Jan. and 11 for Dec.
         * @return builder for convenient configuration
         */
        public Builder withMonths(Integer... months) {
            mMonths = new HashSet<>(Arrays.asList(months));
            return this;
        }

        /**
         * Allows to specify the months on which the trigger should fire.
         *
         * @param months Use 0-indexed month indexes. I.e. 0 for Jan. and 11 for Dec.
         * @return builder for convenient configuration
         */
        public Builder withMonths(Set<Integer> months) {
            mMonths = months;
            return this;
        }

        /**
         * Allows to specify all months at once
         *
         * @return builder for convenient configuration
         */
        public Builder withAllMonths() {
            mMonths = ALL_MONTHS;
            return this;
        }

        /**
         * Allows to specify the week days on which the trigger should fire.
         *
         * @param daysInWeek Use 1-indexed week day indexes. I.e. 1 for Sunday and 7 for Saturday
         * @return builder for convenient configuration
         */
        public Builder withDaysInWeek(Integer... daysInWeek) {
            mDaysType = DaysInWeek.create(daysInWeek);
            return this;
        }

        /**
         * Allows to specify the week days on which the trigger should fire.
         *
         * @param daysInWeek Use 1-indexed week day indexes. I.e. 1 for Sunday and 7 for Saturday
         * @return builder for convenient configuration
         */
        public Builder withDaysInWeek(Set<Integer> daysInWeek) {
            mDaysType = DaysInWeek.create(daysInWeek);
            return this;
        }

        /**
         * Allows to specify the value that determines the month days on which the trigger should fire.
         *
         * @param daysInMonth the pattern that determines the month days
         * @return builder for convenient configuration
         */
        public Builder withDaysInMonth(DaysInMonth daysInMonth) {
            mDaysType = daysInMonth;
            return this;
        }

        /**
         * Allows to specify the value that determines the minutes part of the trigger fire times.
         *
         * @param minutes the pattern can consist of the following tokens
         * @return builder for convenient configuration
         */
        public Builder withMinutes(MinutesTimeFormat minutes) {
            mMinutes = minutes;
            return this;
        }

        /**
         * Allows to specify the pattern that determines the hours at which the trigger should fire.
         *
         * @param hours the pattern can consist of the following tokens:
         * @return builder for convenient configuration
         */
        public Builder withHours(HoursTimeFormat hours) {
            mHours = hours;
            return this;
        }

        public CalendarRecurrence build() {
            validateMonths();
            return new CalendarRecurrence(mMonths, mDaysType, mMinutes, mHours);
        }

        private void validateMonths() {
            for (Integer month : mMonths) {
                if (!ALL_MONTHS.contains(month)) {
                    throw new IllegalArgumentException("Found incorrect value of month. Should be within range between 1-12");
                }
            }
        }
    }
}
