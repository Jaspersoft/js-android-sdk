package com.jaspersoft.android.sdk.service.data.schedule;

import java.util.Date;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class UntilEndDate extends EndDate {
    private final Date mSpecifiedDate;

    public UntilEndDate(Date specifiedDate) {
        mSpecifiedDate = specifiedDate;
    }

    public Date getSpecifiedDate() {
        return mSpecifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UntilEndDate that = (UntilEndDate) o;

        if (mSpecifiedDate != null ? !mSpecifiedDate.equals(that.mSpecifiedDate) : that.mSpecifiedDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mSpecifiedDate != null ? mSpecifiedDate.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UntilEndDate{" +
                "specifiedDate=" + mSpecifiedDate +
                '}';
    }
}
