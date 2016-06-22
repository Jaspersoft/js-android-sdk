package com.jaspersoft.android.sdk.widget.report.v3;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportRendererKey implements Serializable {
    private final String key;

    private ReportRendererKey(String key) {
        this.key = key;
    }

    static ReportRendererKey newKey() {
        return new ReportRendererKey(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return "ReportKey{" +
                "key='" + key + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportRendererKey presenterKey = (ReportRendererKey) o;

        return key != null ? key.equals(presenterKey.key) : presenterKey.key == null;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
