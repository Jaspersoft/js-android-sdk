package com.jaspersoft.android.sdk.widget.report.jsinterface;

import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public abstract class JsInterface <EF extends EventFactory> {
    protected final Dispatcher dispatcher;
    protected final EF eventFactory;

    public JsInterface(Dispatcher dispatcher, EF eventFactory) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
    }
}
