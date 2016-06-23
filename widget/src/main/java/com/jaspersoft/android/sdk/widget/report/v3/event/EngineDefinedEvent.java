package com.jaspersoft.android.sdk.widget.report.v3.event;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class EngineDefinedEvent implements Event {
    private final double versionCode;
    private final boolean isPro;

    EngineDefinedEvent(double versionCode, boolean isPro) {
        this.versionCode = versionCode;
        this.isPro = isPro;
    }

    public double getVersionCode() {
        return versionCode;
    }

    public boolean isPro() {
        return isPro;
    }
}
