package com.jaspersoft.android.sdk.widget.report.state;


import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RenderState;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.EngineDefinedEvent;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.event.ExceptionEvent;
import com.jaspersoft.android.sdk.widget.report.event.JsInterfaceInjectedEvent;
import com.jaspersoft.android.sdk.widget.report.event.TemplateInitedEvent;
import com.jaspersoft.android.sdk.widget.report.event.TemplateLoadedEvent;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class IdleState extends State {

    private double initialScale;

    public IdleState(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory) {
        super(dispatcher, eventFactory, commandFactory);
    }

    @Override
    protected void internalInit(double initialScale) {
        setInProgress(true);

        this.initialScale = initialScale;
        Command defineEngineCommand = commandFactory.createDefineEngineCommand();
        defineEngineCommand.execute();
    }

    @Override
    protected void internalRun(RunOptions runOptions) {
        throw new IllegalStateException("Could not run report. Renderer still not initialized.");
    }

    @Override
    protected void internalApplyParams(List<ReportParameter> parameters) {
        throw new IllegalStateException("Could not apply report params. Renderer still not initialized.");
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
