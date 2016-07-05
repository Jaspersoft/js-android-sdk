package com.jaspersoft.android.sdk.widget.report.renderer.jsinterface;

import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;

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
