package com.jaspersoft.android.sdk.widget.report.view;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class ReportRendererKey implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
    }

    protected ReportRendererKey(Parcel in) {
        this.key = in.readString();
    }

    public static final Creator<ReportRendererKey> CREATOR = new Creator<ReportRendererKey>() {
        @Override
        public ReportRendererKey createFromParcel(Parcel source) {
            return new ReportRendererKey(source);
        }

        @Override
        public ReportRendererKey[] newArray(int size) {
            return new ReportRendererKey[size];
        }
    };
}
