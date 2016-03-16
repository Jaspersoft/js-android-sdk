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
