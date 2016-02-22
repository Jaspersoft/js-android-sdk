package com.jaspersoft.android.sdk.service.data.schedule;

import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class IntervalRecurrence extends Recurrence {

    private final int mInterval;
    @NotNull
    private final RecurrenceIntervalUnit mUnit;

    private IntervalRecurrence(
            int interval,
            @NotNull RecurrenceIntervalUnit unit
    ) {
        mInterval = interval;
        mUnit = unit;
    }

    public int getInterval() {
        return mInterval;
    }

    @NotNull
    public RecurrenceIntervalUnit getUnit() {
        return mUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntervalRecurrence that = (IntervalRecurrence) o;

        if (mInterval != that.mInterval) return false;
        if (mUnit != that.mUnit) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mInterval;
        result = 31 * result + (mUnit != null ? mUnit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SimpleRecurrence{" +
                ", recurrenceInterval=" + mInterval +
                ", unit=" + mUnit +
                '}';
    }

    public static class Builder {
        @NotNull
        private Integer mInterval;
        @NotNull
        private RecurrenceIntervalUnit mUnit;

        public Builder withInterval(int recurrenceInterval) {
            mInterval = recurrenceInterval;
            return this;
        }

        public Builder withUnit(RecurrenceIntervalUnit unit) {
            mUnit = unit;
            return this;
        }

        public IntervalRecurrence build() {
            Preconditions.checkNotNull(mInterval, "Interval recurrence can not be created without interval");
            Preconditions.checkNotNull(mUnit, "Interval recurrence can not be created without unit");
            return new IntervalRecurrence(mInterval, mUnit);
        }
    }
}
