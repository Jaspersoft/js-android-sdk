package com.jaspersoft.android.sdk.widget.report.renderer.state;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.RenderState;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportAction;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;
import com.jaspersoft.android.sdk.widget.report.renderer.command.Command;
import com.jaspersoft.android.sdk.widget.report.renderer.command.CommandExecutor;
import com.jaspersoft.android.sdk.widget.report.renderer.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.event.ExceptionEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.ReportClearedEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.ReportRenderedEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.rest.PageExportedEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.rest.ReportExecutedEvent;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class InitedRestState extends State{
    ReportExecution reportExecution;

    InitedRestState(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory, CommandExecutor commandExecutor) {
        super(dispatcher, eventFactory, commandFactory, commandExecutor);
    }

    @Override
    protected void internalInit(double initialScale) {
        throw new IllegalStateException("Could not init. Already inited.");
    }

    @Override
    protected void internalRender(RunOptions runOptions) {
        setInProgress(true);
        Command executeReportCommand = commandFactory.createExecuteReportCommand(runOptions);
        commandExecutor.execute(executeReportCommand);
    }

    @Override
    protected void internalApplyParams(List<ReportParameter> parameters) {
        throw new IllegalStateException("Could not apply report params. Report still not rendered.");
    }

    @Override
    protected void internalNavigateTo(Destination destination) {
        throw new IllegalStateException("Could not navigate to destination. Report still not rendered.");
    }

    @Override
    protected void internalRefresh() {
        throw new IllegalStateException("Could not refresh report data. Report still not rendered.");
    }

    @Override
    protected void internalReset() {
        setInProgress(true);
        commandExecutor.cancelExecution();

        Command resetCommand = commandFactory.createResetCommand();
        commandExecutor.execute(resetCommand);
    }

    @Override
    public RenderState getName() {
        return RenderState.INITED;
    }

    @Override
    public boolean internalIsActionAvailable(ReportAction reportAction) {
        return reportAction == ReportAction.RENDER || reportAction == ReportAction.DESTROY;
    }

    @Subscribe
    public void onReportExecuted(ReportExecutedEvent reportExecutedEvent) {
        reportExecution = reportExecutedEvent.getReportExecution();
        Command pageExportCommand = commandFactory.createPageExportCommand(reportExecutedEvent.getDestination(), reportExecution);
        commandExecutor.execute(pageExportCommand);
    }

    @Subscribe
    public void onPageExported(PageExportedEvent pageExportedEvent) {
        Command pageExportCommand = commandFactory.createShowPageCommand(pageExportedEvent.getReportPage());
        commandExecutor.execute(pageExportCommand);
    }

    @Subscribe
    public void onReportRendered(ReportRenderedEvent reportRenderedEvent) {
        setInProgress(false);
        dispatcher.dispatch(eventFactory.createSwapStateEvent(RenderState.RENDERED));
    }

    @Subscribe
    public void onReportCleared(ReportClearedEvent reportClearedEvent) {
        setInProgress(false);
        dispatcher.dispatch(eventFactory.createSwapStateEvent(RenderState.INITED));
    }

    @Subscribe
    public void onError(ExceptionEvent exceptionEvent) {
        setInProgress(false);
    }
}
