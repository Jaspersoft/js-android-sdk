package com.jaspersoft.android.sdk.widget.report.v2;

import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class StateFactory {
    private final Dispatcher dispatcher;
    private final PresenterState.Context context;

    StateFactory(Dispatcher dispatcher, PresenterState.Context context) {
        this.dispatcher = dispatcher;
        this.context = context;
    }

    public PresenterState createInitState() {
        return new InitState(context, dispatcher);
    }

    public PresenterState createEngineInitState(double code) {
        if (code >= 6.0) {
            return new VisInitState(context, dispatcher);
        }
        return new RestInitState(context, dispatcher);
    }
}
