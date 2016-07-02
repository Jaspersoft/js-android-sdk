package com.jaspersoft.android.sdk.widget.report.state;


import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class VisStateFactory extends StateFactory {
    public VisStateFactory(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory) {
        super(dispatcher, eventFactory, commandFactory);
    }

    @Override
    public State createInitedState(State prevState) {
        return new InitedVisState(dispatcher, eventFactory, commandFactory);
    }

    @Override
    public State createRenderedState(State prevState) {
        return new RenderedVisState(dispatcher, eventFactory, commandFactory);
    }
}
