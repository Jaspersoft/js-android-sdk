package com.jaspersoft.android.sdk.widget.report.renderer.state;


import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.command.CommandExecutor;
import com.jaspersoft.android.sdk.widget.report.renderer.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.jsinterface.JsInterface;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public abstract class StateFactory {
    protected final Dispatcher dispatcher;
    protected final EventFactory eventFactory;
    protected final CommandFactory commandFactory;
    protected final CommandExecutor commandExecutor;
    private  final JsInterface jsInterface;

    public StateFactory(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory, CommandExecutor commandExecutor, JsInterface jsInterface) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
        this.commandFactory = commandFactory;
        this.commandExecutor = commandExecutor;
        this.jsInterface = jsInterface;
    }

    public final State createIdleState(State prevState) {
        return new IdleState(dispatcher, eventFactory, commandFactory, commandExecutor, jsInterface);
    }

    public abstract State createInitedState(State prevState);

    public abstract State createRenderedState(State prevState);

    public final State createDestroyedState(State prevState) {
        return new DestroyedState(dispatcher, eventFactory, commandFactory, commandExecutor);
    }
}
