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

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class Trigger {
    @Nullable
    private final String mCalendarName;
    @NotNull
    private final Recurrence mRecurrence;
    @Nullable
    private final EndDate mEndDate;

    private Trigger(String calendarName, @NotNull Recurrence recurrence, @NotNull EndDate endDate) {
        mCalendarName = calendarName;
        mRecurrence = recurrence;
        mEndDate = endDate;
    }

    @Nullable
    public String getCalendarName() {
        return mCalendarName;
    }

    @NotNull
    public Recurrence getRecurrence() {
        return mRecurrence;
    }

    @Nullable
    public EndDate getEndDate() {
        return mEndDate;
    }

    public static class Builder {
        private String mCalendarName;
        private Recurrence mRecurrence;

        /**
         * Allows to specify name of the calendar to follow
         *
         * @param calendarName registered calendar name. User can register one on JRS side
         * @return builder for convenient configuration
         */
        public Builder withCalendarName(String calendarName) {
            mCalendarName = calendarName;
            return this;
        }

        /**
         * Allows to supply interval recurrence. This tells JRS to repeat schedule on interval basis.
         *
         * @param recurrence concrete implementation of interval recurrence
         * @return builder for convenient configuration
         */
        public SimpleTriggerBuilder withRecurrence(IntervalRecurrence recurrence) {
            mRecurrence = recurrence;
            return new SimpleTriggerBuilder(this);
        }

        /**
         * Allows to supply interval recurrence. This tells JRS to repeat schedule on calendar basis.
         *
         * @param recurrence concrete implementation of calendar recurrence
         * @return builder for convenient configuration
         */
        public CalendarTriggerBuilder withRecurrence(CalendarRecurrence recurrence) {
            mRecurrence = recurrence;
            return new CalendarTriggerBuilder(this);
        }
    }

    public static class SimpleTriggerBuilder {
        private EndDate mEndDate;

        private final Builder mBuilder;

        public SimpleTriggerBuilder(Builder builder) {
            mBuilder = builder;
        }

        /**
         * Allows to specify concrete end date for simple trigger
         *
         * @param endDate should be any date in future
         * @return builder for convenient configuration
         */
        public SimpleTriggerBuilder withEndDate(UntilEndDate endDate) {
            mEndDate = endDate;
            return this;
        }

        /**
         * Allows to specify concrete repeat count in future
         *
         * @param endDate encapsulated repeat count
         * @return builder for convenient configuration
         */
        public SimpleTriggerBuilder withEndDate(RepeatedEndDate endDate) {
            mEndDate = endDate;
            return this;
        }

        public Trigger build() {
            return new Trigger(mBuilder.mCalendarName, mBuilder.mRecurrence, mEndDate);
        }
    }

    public static class CalendarTriggerBuilder {
        private EndDate mEndDate;

        private final Builder mBuilder;

        public CalendarTriggerBuilder(Builder builder) {
            mBuilder = builder;
        }

        /**
         * Allows to specify concrete end date for calendar trigger
         *
         * @param endDate should be any date in future
         * @return builder for convenient configuration builder for convenient configuration
         */
        public CalendarTriggerBuilder withEndDate(UntilEndDate endDate) {
            mEndDate = endDate;
            return this;
        }

        public Trigger build() {
            return new Trigger(mBuilder.mCalendarName, mBuilder.mRecurrence, mEndDate);
        }
    }
}
