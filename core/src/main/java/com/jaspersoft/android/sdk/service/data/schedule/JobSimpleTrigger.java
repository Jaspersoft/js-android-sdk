package com.jaspersoft.android.sdk.service.data.schedule;

import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobSimpleTrigger extends JobTrigger {
    @Nullable
    private final Integer mOccurrenceCount;
    @Nullable
    private final Integer mRecurrenceInterval;
    @Nullable
    private final RecurrenceIntervalUnit mRecurrenceIntervalUnit;

    protected JobSimpleTrigger(Builder builder) {
        super(
                builder.mTimeZone,
                builder.mCalendarName,
                builder.mStartType,
                builder.mStartDate,
                builder.mStopDate
        );

        mOccurrenceCount = builder.mOccurrenceCount;
        mRecurrenceInterval = builder.mRecurrenceInterval;
        mRecurrenceIntervalUnit = builder.mRecurrenceIntervalUnit;
    }

    @Nullable
    public Integer getOccurrenceCount() {
        return mOccurrenceCount;
    }

    @Nullable
    public Integer getRecurrenceInterval() {
        return mRecurrenceInterval;
    }

    @Nullable
    public RecurrenceIntervalUnit getRecurrenceIntervalUnit() {
        return mRecurrenceIntervalUnit;
    }

    public static class Builder {
        private TimeZone mTimeZone;
        private String mCalendarName;
        private TriggerStartType mStartType;
        private Date mStartDate;
        private Date mStopDate;
        private Integer mOccurrenceCount;
        private Integer mRecurrenceInterval;
        private RecurrenceIntervalUnit mRecurrenceIntervalUnit;

        public Builder withTimeZone(@Nullable TimeZone timeZone) {
            mTimeZone = timeZone;
            return this;
        }

        public Builder withCalendarName(String calendarName) {
            mCalendarName = calendarName;
            return this;
        }

        public Builder withStartType(@Nullable TriggerStartType startType) {
            mStartType = startType;
            return this;
        }

        public Builder withStartDate(@Nullable Date startDate) {
            mStartDate = startDate;
            return this;
        }

        public Builder withStopDate(@Nullable Date endDate) {
            mStopDate = endDate;
            return this;
        }

        public Builder withOccurrenceCount(int occurrenceCount) {
            mOccurrenceCount = occurrenceCount;
            return this;
        }

        public Builder withRecurrenceInterval(int recurrenceInterval) {
            mRecurrenceInterval = recurrenceInterval;
            return this;
        }

        public Builder withRecurrenceIntervalUnit(@Nullable RecurrenceIntervalUnit recurrenceIntervalUnit) {
            mRecurrenceIntervalUnit = recurrenceIntervalUnit;
            return this;
        }

        public JobSimpleTrigger build() {
            return new JobSimpleTrigger(this);
        }
    }
}
