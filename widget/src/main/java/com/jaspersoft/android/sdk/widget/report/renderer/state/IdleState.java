package com.jaspersoft.android.sdk.widget.report.renderer.state;


import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.RenderState;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;
import com.jaspersoft.android.sdk.widget.report.renderer.command.Command;
import com.jaspersoft.android.sdk.widget.report.renderer.command.CommandExecutor;
import com.jaspersoft.android.sdk.widget.report.renderer.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.event.ExceptionEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.JsInterfaceInjectedEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.TemplateInitedEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.TemplateLoadedEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.jsinterface.JsInterface;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class IdleState extends State {
    private final JsInterface jsInterface;
    private double initialScale;

    IdleState(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory, CommandExecutor commandExecutor, JsInterface jsInterface) {
        super(dispatcher, eventFactory, commandFactory, commandExecutor);
        this.jsInterface = jsInterface;
    }

    @Override
    protected void internalInit(double initialScale) {
        setInProgress(true);

        this.initialScale = initialScale;
        Command injectJsInterfaceCommand = commandFactory.createInjectJsInterfaceCommand(jsInterface);
        commandExecutor.execute(injectJsInterfaceCommand);
    }

    @Override
    protected void internalRender(RunOptions runOptions) {
        throw new IllegalStateException("Could not render report. Renderer still not initialized.");
    }

    @Override
    protected void internalApplyParams(List<ReportParameter> parameters) {
        throw new IllegalStateException("Could not apply report params. Renderer still not initialized.");
    }

    @Override
    protected void internalNavigateTo(Destination destination) {
        throw new IllegalStateException("Could not navigate to destination. Renderer still not initialized.");
    }

    @Override
    protected void internalRefresh() {
        throw new IllegalStateException("Could not refresh report data. Renderer still not initialized.");
    }

    @Override
    protected void internalReset() {
        // no action required
    }

    @Override
    public RenderState getName() {
        return RenderState.IDLE;
    }

    @Subscribe
    public void onJsInterfaceInjected(JsInterfaceInjectedEvent jsInterfaceInjectedEvent) {
        Command loadTemplateCommand = commandFactory.createLoadTemplateCommand();
        commandExecutor.execute(loadTemplateCommand);
    }

    @Subscribe
    public void onTemplateLoaded(TemplateLoadedEvent templateLoadedEvent) {
        Command initTemplateCommand = commandFactory.createInitTemplateCommand(initialScale);
        commandExecutor.execute(initTemplateCommand);
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
