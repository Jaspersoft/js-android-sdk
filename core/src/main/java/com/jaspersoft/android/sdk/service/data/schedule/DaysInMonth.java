package com.jaspersoft.android.sdk.service.data.schedule;

/**
 * Following API reserved for future enhancement specific the pattern
 * The pattern can consist of the following tokens:
 * <ul>
 *    <li>
 *        a single day value between 1 and 31.
 *    </li>
 *    <li>
 *        a day value with an increment, for instance 1/5 which means that the trigger would fire every 5 days starting on 1st of the month.
 *    </li>
 *    <li>
 *        a days range, for instance 2-5 which means that the trigger should fire every on each day starting from 2nd to 5th. Day values and ranges can be concatenated using commas as separators.
 *    </li>
 *    <li>
 *        * which means the the job would fire every day.
 *    </li>
 * </ul>
 *
 * @author Tom Koptel
 * @since 2.3
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

    /**
     * Factory method that creates new pattern wrapper. Accepts any raw value.
     *
     * @param rawValue any raw value
     * @return new pattern wrapper
     */
    public static DaysInMonth valueOf(String rawValue) {
        return new DaysInMonth(rawValue);
    }
}
