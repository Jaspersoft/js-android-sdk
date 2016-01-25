package com.jaspersoft.android.sdk.service.data.schedule;

import java.util.Date;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class DeferredStartType extends JobStartType {
    private final Date mStartDate;

    public DeferredStartType(Date startDate) {
        mStartDate = startDate;
    }

    public Date getStartDate() {
        return mStartDate;
    }
}
