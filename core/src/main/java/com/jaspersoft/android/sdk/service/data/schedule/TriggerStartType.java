package com.jaspersoft.android.sdk.service.data.schedule;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public enum TriggerStartType {
    IMMEDIATE(1), DEFERRED(2);

    private final int mValue;

    TriggerStartType(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }
}
