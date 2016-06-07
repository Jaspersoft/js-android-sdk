package com.jaspersoft.android.sdk.widget;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class Dispatcher {
    private final Bus bus;
    private final Handler mainThread = new Handler(Looper.getMainLooper());

    Dispatcher() {
        bus = new Bus();
    }

    public void dispatch(final Object action) {
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                bus.post(action);
            }
        });
    }

    public void register(Object target) {
        bus.register(target);
    }
}
