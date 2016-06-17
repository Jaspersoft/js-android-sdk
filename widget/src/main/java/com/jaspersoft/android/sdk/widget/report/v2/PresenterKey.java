package com.jaspersoft.android.sdk.widget.report.v2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class PresenterKey implements Parcelable {
    private final String key;

    private PresenterKey(String key) {
        this.key = key;
    }

    static PresenterKey newKey() {
        return new PresenterKey(UUID.randomUUID().toString());
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

        PresenterKey presenterKey = (PresenterKey) o;

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

    protected PresenterKey(Parcel in) {
        this.key = in.readString();
    }

    public static final Creator<PresenterKey> CREATOR = new Creator<PresenterKey>() {
        @Override
        public PresenterKey createFromParcel(Parcel source) {
            return new PresenterKey(source);
        }

        @Override
        public PresenterKey[] newArray(int size) {
            return new PresenterKey[size];
        }
    };
}
