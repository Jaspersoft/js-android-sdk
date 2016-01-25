package com.jaspersoft.android.sdk.service.data.schedule;

import org.jetbrains.annotations.NotNull;
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
    @NotNull
    private final JobStartType mStartType;
    @Nullable
    private final Date mStopDate;

    protected JobTrigger(
            @Nullable TimeZone timeZone,
            @Nullable String calendarName,
            @NotNull JobStartType startType,
            @Nullable Date stopDate
    ) {
        mTimeZone = timeZone;
        mCalendarName = calendarName;
        mStartType = startType;
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

    @NotNull
    public JobStartType getStartType() {
        return mStartType;
    }

    @Nullable
    public Date getStopDate() {
        return mStopDate;
    }

}
