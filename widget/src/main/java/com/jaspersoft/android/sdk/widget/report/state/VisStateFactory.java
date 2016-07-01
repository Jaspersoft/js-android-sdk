package com.jaspersoft.android.sdk.widget.report.state;


import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.command.vis.VisCommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.vis.VisEventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class VisStateFactory extends StateFactory<VisEventFactory, VisCommandFactory> {
    public VisStateFactory(Dispatcher dispatcher, VisEventFactory eventFactory, VisCommandFactory commandFactory) {
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
