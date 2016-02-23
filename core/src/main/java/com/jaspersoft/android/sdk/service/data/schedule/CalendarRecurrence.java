package com.jaspersoft.android.sdk.service.data.schedule;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.0
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
                Calendar.JANUARY + 1,
                Calendar.FEBRUARY + 1,
                Calendar.MARCH + 1,
                Calendar.APRIL + 1,
                Calendar.MAY + 1,
                Calendar.JUNE + 1,
                Calendar.JULY + 1,
                Calendar.AUGUST + 1,
                Calendar.SEPTEMBER + 1,
                Calendar.OCTOBER + 1,
                Calendar.NOVEMBER + 1,
                Calendar.DECEMBER + 1
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

        public Builder withMonths(Integer... months) {
            mMonths = new HashSet<>(Arrays.asList(months));
            return this;
        }

        public Builder withMonths(Set<Integer> months) {
            mMonths = months;
            return this;
        }

        public Builder withAllMonths() {
            mMonths = ALL_MONTHS;
            return this;
        }

        public Builder withDaysInWeek(Integer... daysInWeek) {
            mDaysType = DaysInWeek.create(daysInWeek);
            return this;
        }

        public Builder withDaysInWeek(Set<Integer> daysInWeek) {
            mDaysType = DaysInWeek.create(daysInWeek);
            return this;
        }

        public Builder withDaysInMonth(DaysInMonth daysInMonth) {
            mDaysType = daysInMonth;
            return this;
        }

        public Builder withMinutes(MinutesTimeFormat minutes) {
            mMinutes = minutes;
            return this;
        }

        public Builder withHours(HoursTimeFormat hours) {
            mHours = hours;
            return this;
        }

        public CalendarRecurrence build() {
            if (mMonths.isEmpty()) {
                throw new IllegalArgumentException("At lease one month should be specified");
            }
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
