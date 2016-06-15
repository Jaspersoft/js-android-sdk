package com.jaspersoft.android.sdk.sample.page;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ReportPage implements Parcelable {
    static class State {
        static final int INITIAL = 0;
        static final int TEMPLATE_LOADED = 1;
        static final int SCRIPT_LOADED = 2;
        static final int REPORT_RENDERED = 3;
    }

    private int state = State.INITIAL;

    public void resetState() {
        state = State.INITIAL;
    }

    public void moveToNextState() {
        if (state != State.REPORT_RENDERED) {
            state++;
        }
    }

    public boolean loadingInProgress() {
        return state != State.INITIAL;
    }

    int getState() {
        return state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.state);
    }

    public ReportPage() {
    }

    protected ReportPage(Parcel in) {
        this.state = in.readInt();
    }

    public static final Creator<ReportPage> CREATOR = new Creator<ReportPage>() {
        @Override
        public ReportPage createFromParcel(Parcel source) {
            return new ReportPage(source);
        }

        @Override
        public ReportPage[] newArray(int size) {
            return new ReportPage[size];
        }
    };
}
