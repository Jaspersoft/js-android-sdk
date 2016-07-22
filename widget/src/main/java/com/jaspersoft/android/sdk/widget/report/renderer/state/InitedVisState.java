package com.jaspersoft.android.sdk.widget.report.renderer.state;


import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.report.renderer.Bookmark;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.RenderState;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportPart;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;
import com.jaspersoft.android.sdk.widget.report.renderer.command.Command;
import com.jaspersoft.android.sdk.widget.report.renderer.command.CommandExecutor;
import com.jaspersoft.android.sdk.widget.report.renderer.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.event.BookmarksEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.event.ExceptionEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.ReportClearedEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.ReportPartsEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.event.ReportRenderedEvent;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class InitedVisState extends State {
    List<Bookmark> bookmarkList;
    List<ReportPart> reportPartList;

    InitedVisState(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory, CommandExecutor commandExecutor) {
        super(dispatcher, eventFactory, commandFactory, commandExecutor);
    }

    @Override
    protected void internalInit(double initialScale) {
        throw new IllegalStateException("Could not init. Already inited.");
    }

    @Override
    protected void internalRender(RunOptions runOptions) {
        setInProgress(true);
        Command runReportCommand = commandFactory.createRunReportCommand(runOptions);
        commandExecutor.execute(runReportCommand);
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
    protected List<Bookmark> internalGetBookmarks() {
        throw new IllegalStateException("Could not get report bookmarks. Report still not rendered.");
    }

    @Override
    protected List<ReportPart> internalGetReportParts() {
        throw new IllegalStateException("Could not get report parts. Report still not rendered.");
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

    @Subscribe
    public void onReportRendered(ReportRenderedEvent reportRenderedEvent) {
        setInProgress(false);
        dispatcher.dispatch(eventFactory.createSwapStateEvent(RenderState.RENDERED));
    }

    @Subscribe
    public void onBookmarkListChanged(BookmarksEvent bookmarksEvent) {
        bookmarkList = bookmarksEvent.getBookmarkList();
    }

    @Subscribe
    public void onReportPartsChanged(ReportPartsEvent reportPartsEvent) {
        reportPartList = reportPartsEvent.getReportPartList();
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
