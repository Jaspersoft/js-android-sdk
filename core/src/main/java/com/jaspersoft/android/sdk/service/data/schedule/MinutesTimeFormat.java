package com.jaspersoft.android.sdk.service.data.schedule;

import org.jetbrains.annotations.NotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class MinutesTimeFormat {
    private static final TimePattern TIME_PATTERN = new TimePattern(0, 59);
    private final String mValue;

    private MinutesTimeFormat(String value) {
        mValue = value;
    }

    @Override
    public String toString() {
        return mValue;
    }

    @NotNull
    public static MinutesTimeFormat parse(@NotNull String rawValue) {
        return new MinutesTimeFormat(TIME_PATTERN.parse(rawValue));
    }

    public static class Builder {
        public Builder() {
            TIME_PATTERN.setRawValue("0");
        }

        public MinutesTimeFormat parse(String rawValue) {
            return new MinutesTimeFormat(rawValue);
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

        public MinutesTimeFormat build() {
            return new MinutesTimeFormat(TIME_PATTERN.toString());
        }
    }
}
