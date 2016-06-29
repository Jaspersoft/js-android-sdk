package com.jaspersoft.android.sdk.widget.report.state;


import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

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

    public State createRenderedState() {
        return new RenderedState(dispatcher, eventFactory, commandFactory);
    }
}
