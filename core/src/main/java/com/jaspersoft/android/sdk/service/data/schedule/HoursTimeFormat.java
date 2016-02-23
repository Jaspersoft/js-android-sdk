package com.jaspersoft.android.sdk.service.data.schedule;

import org.jetbrains.annotations.NotNull;

/**
 * @author Tom Koptel
 * @since 2.0
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

        public Builder withRange(int start, int end) {
            TIME_PATTERN.setRange(start, end);
            return this;
        }

        public Builder withSingleValue(int value) {
            TIME_PATTERN.setValue(value);
            return this;
        }

        public Builder withAll() {
            TIME_PATTERN.setRawValue("*");
            return this;
        }

        public Builder withIncrement(int interval, int startFrom) {
            TIME_PATTERN.setIncrement(interval, startFrom);
            return this;
        }

        public HoursTimeFormat build() {
            return new HoursTimeFormat(TIME_PATTERN.toString());
        }
    }
}
