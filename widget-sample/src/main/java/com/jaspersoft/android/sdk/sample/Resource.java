package com.jaspersoft.android.sdk.sample;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class Resource implements Parcelable {
    private final String uri;
    private final String name;
    private final Type type;
    private final Profile profile;

    private Resource(String uri, String name, Type type, Profile profile) {
        this.uri = uri;
        this.name = name;
        this.type = type;
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getUri() {
        return uri;
    }

    static Resource newReport(String uri, String name, Profile profile) {
        return new Resource(uri, name, Type.REPORT, profile);
    }

    static Resource newDashboard(String uri, String name, Profile profile) {
        return new Resource(uri, name, Type.DASHBOARD, profile);
    }

    public boolean isReport() {
        return type == Type.REPORT;
    }

    public boolean isDashboard() {
        return type == Type.DASHBOARD;
    }

    public enum Type {
        REPORT, DASHBOARD;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uri);
        dest.writeString(this.name);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeParcelable(this.profile, flags);
    }

    protected Resource(Parcel in) {
        this.uri = in.readString();
        this.name = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : Type.values()[tmpType];
        this.profile = in.readParcelable(Profile.class.getClassLoader());
    }

    public static final Creator<Resource> CREATOR = new Creator<Resource>() {
        @Override
        public Resource createFromParcel(Parcel source) {
            return new Resource(source);
        }

        @Override
        public Resource[] newArray(int size) {
            return new Resource[size];
        }
    };
}
