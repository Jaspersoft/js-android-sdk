package com.jaspersoft.android.sdk.service.data.schedule;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class TimePattern {
    private final int mLowerBound;
    private final int mHigherBound;
    private final StringBuilder mPattern;

    TimePattern(int lowerBound, int higherBound) {
        mLowerBound = lowerBound;
        mHigherBound = higherBound;
        mPattern = new StringBuilder();
    }

    public void setRange(int start, int end) {
        if (start < mLowerBound) {
            throw new IllegalArgumentException("start cannot be less than lower bound.");
        }
        if (end > mHigherBound) {
            throw new IllegalArgumentException("end cannot be more than upper bound.");
        }
        if (start > end) {
            throw new IllegalArgumentException("start must be lesser than end.");
        }
        mPattern.setLength(0);
        mPattern.append(String.valueOf(start))
                .append("-")
                .append(String.valueOf(end));
    }

    @Override
    public String toString() {
        return mPattern.toString();
    }

    public void setValue(int value) {
        validateWithinBounds(value, String.format("Value should be within bounds [ %d, %d ]", mLowerBound, mHigherBound));
        mPattern.setLength(0);
        mPattern.append(String.valueOf(value));
    }

    public void setIncrement(int interval, int from) {
        validateWithinBounds(interval, String.format("Interval should be within bounds [ %d, %d ]", mLowerBound, mHigherBound));
        validateWithinBounds(from, String.format("From should be within bounds [ %d, %d ]", mLowerBound, mHigherBound));
        mPattern.setLength(0);
        mPattern.append(String.valueOf(interval))
                .append("/")
                .append(String.valueOf(from));
    }

    public void setRawValue(String rawValue) {
        mPattern.setLength(0);
        mPattern.append(rawValue);
    }

    private void validateWithinBounds(int value, String message) {
        if (value < mLowerBound || value > mHigherBound) {
            throw new IllegalArgumentException(message);
        }
    }

    public String parse(String rawValue) {
        // TODO provide parsing logic
        return rawValue;
    }
}
