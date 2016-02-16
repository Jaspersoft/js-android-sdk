package com.jaspersoft.android.sdk.service.data.schedule;

import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobSimpleTrigger extends JobTrigger {
    private final int mOccurrenceCount;
    private final int mRecurrenceInterval;
    @NotNull
    private final RecurrenceIntervalUnit mRecurrenceIntervalUnit;

    protected JobSimpleTrigger(Builder builder) {
        super(
                builder.mTimeZone,
                builder.mCalendarName,
                builder.mStartDate,
                builder.mEndDate
        );

        mOccurrenceCount = builder.mOccurrenceCount;
        mRecurrenceInterval = builder.mRecurrenceInterval;
        mRecurrenceIntervalUnit = builder.mRecurrenceIntervalUnit;
    }

    public int getOccurrenceCount() {
        return mOccurrenceCount;
    }

    public int getRecurrenceInterval() {
        return mRecurrenceInterval;
    }

    @NotNull
    public RecurrenceIntervalUnit getRecurrenceIntervalUnit() {
        return mRecurrenceIntervalUnit;
    }

    public static class Builder {
        private TimeZone mTimeZone;
        private String mCalendarName;
        private Date mStartDate;
        private Date mEndDate;
        private Integer mOccurrenceCount;
        private Integer mRecurrenceInterval;
        private RecurrenceIntervalUnit mRecurrenceIntervalUnit;

        public Builder withTimeZone(@Nullable TimeZone timeZone) {
            mTimeZone = timeZone;
            return this;
        }

        public Builder withCalendarName(@Nullable String calendarName) {
            mCalendarName = calendarName;
            return this;
        }

        public Builder withStartDate(@Nullable Date startDate) {
            mStartDate = startDate;
            return this;
        }

        public Builder withEndDate(@Nullable Date endDate) {
            mEndDate = endDate;
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

        public Builder withRecurrenceIntervalUnit(@NotNull RecurrenceIntervalUnit recurrenceIntervalUnit) {
            Preconditions.checkNotNull(recurrenceIntervalUnit, "Recurrent interval should not be null");
            mRecurrenceIntervalUnit = recurrenceIntervalUnit;
            return this;
        }

        public JobSimpleTrigger build() {
            assertState();
            return new JobSimpleTrigger(this);
        }

        private void assertState() {
            Preconditions.checkNotNull(mOccurrenceCount, "Occurrence count should be specified");
            Preconditions.checkNotNull(mRecurrenceInterval, "Recurrence interval should be specified");
            Preconditions.checkNotNull(mRecurrenceIntervalUnit, "Recurrent interval unit should be specified");
        }
    }
}
