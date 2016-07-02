package com.jaspersoft.android.sdk.widget.report.state;


import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.jsinterface.JsInterface;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public abstract class StateFactory {

    protected final Dispatcher dispatcher;
    protected final EventFactory eventFactory;
    protected final CommandFactory commandFactory;

    public StateFactory(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory) {
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
