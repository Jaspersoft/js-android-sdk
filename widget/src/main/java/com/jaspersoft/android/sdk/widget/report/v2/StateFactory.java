package com.jaspersoft.android.sdk.widget.report.v2;

import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class StateFactory {
    private final Dispatcher dispatcher;

    StateFactory(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public PresenterState createInitState(PresenterState.StateContext stateContext) {
        return new InitState(stateContext, dispatcher);
    }
}
