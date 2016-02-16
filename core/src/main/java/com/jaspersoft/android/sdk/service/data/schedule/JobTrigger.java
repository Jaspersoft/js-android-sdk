package com.jaspersoft.android.sdk.service.data.schedule;

import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public abstract class JobTrigger {
    @Nullable
    private final TimeZone mTimeZone;
    @Nullable
    private final String mCalendarName;
    @Nullable
    private final Date mStartDate;
    @Nullable
    private final Date mEndDate;

    protected JobTrigger(
            @Nullable TimeZone timeZone,
            @Nullable String calendarName,
            @Nullable Date startDate,
            @Nullable Date endDate
    ) {
        mTimeZone = timeZone;
        mCalendarName = calendarName;
        mStartDate = startDate;
        mEndDate = endDate;
    }

    @Nullable
    public TimeZone getTimeZone() {
        return mTimeZone;
    }

    @Nullable
    public String getCalendarName() {
        return mCalendarName;
    }

    @Nullable
    public Date getStartDate() {
        return mStartDate;
    }

    @Nullable
    public Date getEndDate() {
        return mEndDate;
    }

}
