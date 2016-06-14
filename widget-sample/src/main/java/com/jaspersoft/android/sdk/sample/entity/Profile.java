package com.jaspersoft.android.sdk.sample.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class Profile implements Parcelable {
    private final String url;


    public Profile(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
    }

    protected Profile(Parcel in) {
        this.url = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel source) {
            return new Profile(source);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
}
