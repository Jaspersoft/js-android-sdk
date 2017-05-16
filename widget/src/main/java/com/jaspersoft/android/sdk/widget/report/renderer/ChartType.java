package com.jaspersoft.android.sdk.widget.report.renderer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author Olexandr Dahno
 * @since 2.6
 */

public class ChartType implements Parcelable {
    private final String name;

    ChartType(String name) {
        this.name = name;
    }

    protected ChartType(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    /*
     * Parcelable Impl
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }

    public static final Parcelable.Creator<ChartType> CREATOR = new Parcelable.Creator<ChartType>() {
        @Override
        public ChartType createFromParcel(Parcel in) {
            return new ChartType(in);
        }

        @Override
        public ChartType[] newArray(int size) {
            return new ChartType[size];
        }
    };

    /*
     * Equals
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChartType chartType = (ChartType) o;
        boolean isNameEquals = chartType.name.equals(this.name);
        return isNameEquals;
    }
}
