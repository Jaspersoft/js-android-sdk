package com.jaspersoft.android.sdk.widget.report.v3;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class SetupOptions {
    private final int deviceWidth;
    private final float initialScale;

    public SetupOptions() {
        this(0, 1);
    }

    public SetupOptions(int deviceWidth) {
        this(deviceWidth, 0);
    }

    public SetupOptions(float initialScale) {
        this(0, initialScale);
    }

    private SetupOptions(int deviceWidth, float initialScale) {
        this.deviceWidth = deviceWidth;
        this.initialScale = initialScale;
    }
}
