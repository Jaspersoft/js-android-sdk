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
