package com.jaspersoft.android.sdk.widget.report.state;


import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.jsinterface.JsInterface;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public abstract class StateFactory<EF extends EventFactory, CF extends CommandFactory> {

    protected final Dispatcher dispatcher;
    protected final EF eventFactory;
    protected final CF commandFactory;

    public StateFactory(Dispatcher dispatcher, EF eventFactory, CF commandFactory) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
        this.commandFactory = commandFactory;
    }

    public final State createIdleState(JsInterface jsInterface) {
        return new IdleState(dispatcher, eventFactory, commandFactory, jsInterface);
    }

    public abstract State createInitedState(State prevState);

    public abstract State createRenderedState(State prevState);
}
