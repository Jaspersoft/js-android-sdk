package com.jaspersoft.android.sdk.sample;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ResourceApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
