package com.jaspersoft.android.sdk.service.data.schedule;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class DaysInMonth extends DaysType {
    private final String mValue;

    private DaysInMonth(String value) {
        mValue = value;
    }

    @Override
    public String toString() {
        return mValue;
    }

    public static DaysInMonth valueOf(String rawValue) {
        return new DaysInMonth(rawValue);
    }
}
