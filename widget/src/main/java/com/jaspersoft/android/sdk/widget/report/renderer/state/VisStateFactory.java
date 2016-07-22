package com.jaspersoft.android.sdk.widget.report.renderer.state;


import com.jaspersoft.android.sdk.widget.report.renderer.Bookmark;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportPart;
import com.jaspersoft.android.sdk.widget.report.renderer.command.CommandExecutor;
import com.jaspersoft.android.sdk.widget.report.renderer.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.jsinterface.JsInterface;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class VisStateFactory extends StateFactory {
    public VisStateFactory(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory, CommandExecutor commandExecutor, JsInterface jsInterface) {
        super(dispatcher, eventFactory, commandFactory, commandExecutor, jsInterface);
    }

    @Override
    public State createInitedState(State prevState) {
        return new InitedVisState(dispatcher, eventFactory, commandFactory, commandExecutor);
    }

    @Override
    public State createRenderedState(State prevState) {
        List<Bookmark> bookmarkList = null;
        List<ReportPart> reportPartList = null;
        if (prevState instanceof InitedVisState) {
            bookmarkList = ((InitedVisState) prevState).bookmarkList;
            reportPartList = ((InitedVisState) prevState).reportPartList;
        }
        return new RenderedVisState(dispatcher, eventFactory, commandFactory, commandExecutor, bookmarkList, reportPartList);
    }
}
