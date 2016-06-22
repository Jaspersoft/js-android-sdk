package com.jaspersoft.android.sdk.widget.report.v3.state;


import com.jaspersoft.android.sdk.widget.report.v3.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.v3.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.v3.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class InitedState extends State {
    public InitedState(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory) {
        super(dispatcher, eventFactory, commandFactory);
    }

    @Override
    protected void internalInit() {
        throw new IllegalStateException("Could not init. Already inited.");
    }

    @Override
    protected void internalRun() {
        setInProgress(true);
    }
}
