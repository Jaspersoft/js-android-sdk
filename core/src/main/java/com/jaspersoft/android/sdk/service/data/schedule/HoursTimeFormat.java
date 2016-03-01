package com.jaspersoft.android.sdk.service.data.schedule;

import org.jetbrains.annotations.NotNull;

/**
 * Specifies the pattern that determines the hours at which the trigger should fire.
 *
 * <ul>
 *     <li>a single hour value between 0 and 23.</li>
 *     <li>a hours range, for instance 8-16 which means that the trigger should fire every hour starting from 8 AM to 4 PM. Hour values and ranges can be concatenated using commas as separators.</li>
 *     <li>a hour value with an increment, for instance 10/2 which means that the trigger would fire every 2 hours starting from 10 AM.</li>
 *     <li>* which means the the job would fire every hour.</li>
 * </ul>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class HoursTimeFormat {
    private static final TimePattern TIME_PATTERN = new TimePattern(0, 23);
    private final String mValue;

    private HoursTimeFormat(String value) {
        mValue = value;
    }

    @Override
    public String toString() {
        return mValue;
    }

    @NotNull
    public static HoursTimeFormat parse(@NotNull String rawValue) {
        return new HoursTimeFormat(TIME_PATTERN.parse(rawValue));
    }

    public static class Builder {
        public Builder() {
            TIME_PATTERN.setRawValue("0");
        }

        public HoursTimeFormat parse(String rawValue) {
            return new HoursTimeFormat(rawValue);
        }

        /**
         * Allows to specify range
         *
         * @param start lower bound
         * @param end upper bound
         * @return builder for convenient configuration
         */
        public Builder withRange(int start, int end) {
            TIME_PATTERN.setRange(start, end);
            return this;
        }

        /**
         * Allows to specify single value
         *
         * @param value single value
         * @return builder for convenient configuration
         */
        public Builder withSingleValue(int value) {
            TIME_PATTERN.setValue(value);
            return this;
        }

        /**
         * Allows to specify all values at once
         *
         * @return builder for convenient configuration
         */
        public Builder withAll() {
            TIME_PATTERN.setRawValue("*");
            return this;
        }

        /**
         * Allows to specify increment value
         *
         * @param interval how often
         * @param startFrom starting from hour
         * @return builder for convenient configuration
         */
        public Builder withIncrement(int interval, int startFrom) {
            TIME_PATTERN.setIncrement(interval, startFrom);
            return this;
        }

        public HoursTimeFormat build() {
            return new HoursTimeFormat(TIME_PATTERN.toString());
        }
    }
}
