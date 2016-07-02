package com.jaspersoft.android.sdk.widget.report.jsinterface;

import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.Event;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public abstract class JsInterface {
    protected final Dispatcher dispatcher;
    protected final EventFactory eventFactory;

    public JsInterface(Dispatcher dispatcher, EventFactory eventFactory) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
    }
}
