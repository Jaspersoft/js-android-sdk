package com.jaspersoft.android.sdk.widget.report.state;


import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RenderState;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.event.ExceptionEvent;
import com.jaspersoft.android.sdk.widget.report.event.JsInterfaceInjectedEvent;
import com.jaspersoft.android.sdk.widget.report.event.TemplateInitedEvent;
import com.jaspersoft.android.sdk.widget.report.event.TemplateLoadedEvent;
import com.jaspersoft.android.sdk.widget.report.jsinterface.JsInterface;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class IdleState extends State<EventFactory, CommandFactory> {
    private final JsInterface jsInterface;
    private double initialScale;

    IdleState(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory, JsInterface jsInterface) {
        super(dispatcher, eventFactory, commandFactory);
        this.jsInterface = jsInterface;
    }

    @Override
    protected void internalInit(double initialScale) {
        setInProgress(true);

        this.initialScale = initialScale;
        Command injectJsInterfaceCommand = commandFactory.createInjectJsInterfaceCommand(jsInterface);
        injectJsInterfaceCommand.execute();
    }

    @Override
    protected void internalRun(RunOptions runOptions) {
        throw new IllegalStateException("Could not run report. Renderer still not initialized.");
    }

    @Override
    protected void internalApplyParams(List<ReportParameter> parameters) {
        throw new IllegalStateException("Could not apply report params. Renderer still not initialized.");
    }

    @Override
    protected void internalNavigateTo(Destination destination) {
        throw new IllegalStateException("Could not navigate to destination. Renderer still not initialized.");
    }

    @Subscribe
    public void onJsInterfaceInjected(JsInterfaceInjectedEvent jsInterfaceInjectedEvent) {
        Command loadTemplateCommand = commandFactory.createLoadTemplateCommand();
        loadTemplateCommand.execute();
    }

    @Subscribe
    public void onTemplateLoaded(TemplateLoadedEvent templateLoadedEvent) {
        Command initTemplateCommand = commandFactory.createInitTemplateCommand(initialScale);
        initTemplateCommand.execute();
    }

    @Subscribe
    public void onTemplateInited(TemplateInitedEvent templateInitedEvent) {
        setInProgress(false);
        dispatcher.dispatch(eventFactory.createSwapStateEvent(RenderState.INITED));
    }

    @Subscribe
    public void onError(ExceptionEvent exceptionEvent) {
        setInProgress(false);
    }
}
