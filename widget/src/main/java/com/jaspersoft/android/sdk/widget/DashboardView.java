package com.jaspersoft.android.sdk.widget;

import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class DashboardView implements Parcelable {

    private WebView webView;

    public static DashboardView newInstance(AuthorizedClient client) {
        return new DashboardView();
    }

    public DashboardView registerView(WebView webView) {
        this.webView = webView;
        return this;
    }

    public DashboardView registerCallbacks(DashboardCallbacks callbacks) {
        return this;
    }

    public DashboardView done() {
        return this;
    }

    public void run(String uri) {
    }

    public void resume() {
    }

    public void pause() {
    }

    public interface DashboardCallbacks {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public DashboardView() {
    }

    protected DashboardView(Parcel in) {
    }

    public static final Creator<DashboardView> CREATOR = new Creator<DashboardView>() {
        @Override
        public DashboardView createFromParcel(Parcel source) {
            return new DashboardView(source);
        }

        @Override
        public DashboardView[] newArray(int size) {
            return new DashboardView[size];
        }
    };
}
