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
    private final TriggerStartType mStartType;
    @Nullable
    private final Date mStartDate;
    @Nullable
    private final Date mStopDate;

    protected JobTrigger(
            @Nullable TimeZone timeZone,
            @Nullable String calendarName,
            @Nullable TriggerStartType startType,
            @Nullable Date startDate,
            @Nullable Date stopDate
    ) {
        mTimeZone = timeZone;
        mCalendarName = calendarName;
        mStartType = startType;
        mStartDate = startDate;
        mStopDate = stopDate;
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
    public TriggerStartType getStartType() {
        return mStartType;
    }

    @Nullable
    public Date getStartDate() {
        return mStartDate;
    }

    @Nullable
    public Date getStopDate() {
        return mStopDate;
    }

}
