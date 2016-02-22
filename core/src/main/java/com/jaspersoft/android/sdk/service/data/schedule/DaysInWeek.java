package com.jaspersoft.android.sdk.service.data.schedule;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
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
