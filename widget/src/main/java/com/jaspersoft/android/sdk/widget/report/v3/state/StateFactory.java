package com.jaspersoft.android.sdk.widget.report.v3.state;


import com.jaspersoft.android.sdk.widget.report.v3.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.v3.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.v3.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class StateFactory {

    private final Dispatcher dispatcher;
    private final EventFactory eventFactory;
    private final CommandFactory commandFactory;

    public StateFactory(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
        this.commandFactory = commandFactory;
    }

    public State createIdleState() {
        return new IdleState(dispatcher, eventFactory, commandFactory);
    }

    public State createInitedState() {
        return new InitedState(dispatcher, eventFactory, commandFactory);
    }
}
