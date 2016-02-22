package com.jaspersoft.android.sdk.service.data.schedule;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Tom Koptel
 * @since 2.0
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

        public Builder withCalendarName(String calendarName) {
            mCalendarName = calendarName;
            return this;
        }

        public SimpleTriggerBuilder withRecurrence(IntervalRecurrence recurrence) {
            mRecurrence = recurrence;
            return new SimpleTriggerBuilder(this);
        }

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

        public SimpleTriggerBuilder withEndDate(UntilEndDate endDate) {
            mEndDate = endDate;
            return this;
        }

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

        public CalendarTriggerBuilder withEndDate(UntilEndDate endDate) {
            mEndDate = endDate;
            return this;
        }

        public Trigger build() {
            return new Trigger(mBuilder.mCalendarName, mBuilder.mRecurrence, mEndDate);
        }
    }
}
