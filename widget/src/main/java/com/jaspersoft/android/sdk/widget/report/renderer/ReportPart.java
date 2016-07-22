package com.jaspersoft.android.sdk.widget.report.renderer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportPart implements Parcelable {
    private final String name;
    private final int page;

    public ReportPart(String name, int page) {
        this.name = name;
        this.page = page;
    }

    public String getName() {
        return name;
    }

    public int getPage() {
        return page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportPart that = (ReportPart) o;

        if (page != that.page) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + page;
        return result;
    }

    protected ReportPart(Parcel in) {
        name = in.readString();
        page = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(page);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReportPart> CREATOR = new Creator<ReportPart>() {
        @Override
        public ReportPart createFromParcel(Parcel in) {
            return new ReportPart(in);
        }

        @Override
        public ReportPart[] newArray(int size) {
            return new ReportPart[size];
        }
    };
}
