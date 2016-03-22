package com.jaspersoft.android.sdk.service.data.schedule;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class RepeatedEndDate extends EndDate {
    private final int mOccurrenceCount;

    public RepeatedEndDate(int occurrenceCount) {
        mOccurrenceCount = occurrenceCount;
    }

    public int getOccurrenceCount() {
        return mOccurrenceCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepeatedEndDate that = (RepeatedEndDate) o;

        if (mOccurrenceCount != that.mOccurrenceCount) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mOccurrenceCount;
    }

    @Override
    public String toString() {
        return "RepeatedEndDate{" +
                "occurrenceCount=" + mOccurrenceCount +
                '}';
    }
}
