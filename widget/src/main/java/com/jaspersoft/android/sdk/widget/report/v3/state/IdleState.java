package com.jaspersoft.android.sdk.widget.report.v3.state;


import com.jaspersoft.android.sdk.widget.report.v3.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.v3.RenderState;
import com.jaspersoft.android.sdk.widget.report.v3.command.Command;
import com.jaspersoft.android.sdk.widget.report.v3.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.v3.event.EngineDefinedEvent;
import com.jaspersoft.android.sdk.widget.report.v3.event.ErrorEvent;
import com.jaspersoft.android.sdk.widget.report.v3.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.v3.event.JsInterfaceInjectedEvent;
import com.jaspersoft.android.sdk.widget.report.v3.event.TemplateInitedEvent;
import com.jaspersoft.android.sdk.widget.report.v3.event.TemplateLoadedEvent;
import com.squareup.otto.Subscribe;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class IdleState extends State {

    public IdleState(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory) {
        super(dispatcher, eventFactory, commandFactory);
    }

    @Override
    protected void internalInit() {
        setInProgress(true);

        Command defineEngineCommand = commandFactory.createDefineEngineCommand();
        defineEngineCommand.execute();
    }

    @Override
    protected void internalRun(String reportUri) {
        throw new IllegalStateException("Could not run report. Renderer still not initialized.");
    }

    @Subscribe
    public void onEngineDefined(EngineDefinedEvent engineDefinedEvent) {
        commandFactory.updateServerMetadata(engineDefinedEvent.getVersionCode(), engineDefinedEvent.isPro());

        Command injectJsInterfaceCommand = commandFactory.createInjectJsInterfaceCommand();
        injectJsInterfaceCommand.execute();
    }

    @Subscribe
    public void onJsInterfaceInjected(JsInterfaceInjectedEvent jsInterfaceInjectedEvent) {
        Command loadTemplateCommand = commandFactory.createLoadTemplateCommand();
        loadTemplateCommand.execute();
    }

    @Subscribe
    public void onTemplateLoaded(TemplateLoadedEvent templateLoadedEvent) {
        Command initTemplateCommand = commandFactory.createInitTemplateCommand();
        initTemplateCommand.execute();
    }

    @Subscribe
    public void onTemplateInited(TemplateInitedEvent templateInitedEvent) {
        setInProgress(false);
        dispatcher.dispatch(eventFactory.createSwapStateEvent(RenderState.INITED));
    }

    @Subscribe
    public void onError(ErrorEvent errorEvent) {
        setInProgress(false);
    }
}
