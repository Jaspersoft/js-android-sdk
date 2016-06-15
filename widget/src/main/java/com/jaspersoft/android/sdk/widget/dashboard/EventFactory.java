package com.jaspersoft.android.sdk.widget.dashboard;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jaspersoft.android.sdk.widget.WindowError;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class EventFactory implements Event.Factory {

    private final Gson gson = new Gson();
    private final DashboardHyperlink.Factory<ReportExecutionDashboardHyperlink> reportExecLinkFactory;
    private final DashboardHyperlink.Factory<ReferenceDashboardHyperlink> referenceHyperlinkFactory;

    EventFactory() {
        this(new ReportExecutionHyperlinkFactory(), new ReferenceHyperlinkFactory());
    }

    EventFactory(DashboardHyperlink.Factory<ReportExecutionDashboardHyperlink> reportExecLinkFactory, DashboardHyperlink.Factory<ReferenceDashboardHyperlink> referenceHyperlinkFactory) {
        this.reportExecLinkFactory = reportExecLinkFactory;
        this.referenceHyperlinkFactory = referenceHyperlinkFactory;
    }

    @Override
    public Event createInflateCompleteEvent() {
        return new Event(Event.Type.INFLATE_COMPLETE);
    }

    @Override
    public Event createScriptLoadedEvent() {
        return new Event(Event.Type.SCRIPT_LOADED);
    }

    @Override
    public Event createDashboardLoadedEvent() {
        return new Event(Event.Type.DASHBOARD_LOADED);
    }

    @Override
    public Event createWindowErrorEvent(String data) {
        WindowError error = gson.fromJson(data, WindowError.class);
        return new Event(Event.Type.WINDOW_ERROR, error);
    }

    @Override
    public Event createMaximizeStartEvent(String name) {
        return new Event(Event.Type.MAXIMIZE_START, name);
    }

    @Override
    public Event createMaximizeEndEvent(String name) {
        return new Event(Event.Type.MAXIMIZE_END, name);
    }

    @Override
    public Event createMinimizeStartEvent(String name) {
        return new Event(Event.Type.MINIMIZE_START, name);
    }

    @Override
    public Event createMinimizeEndEvent(String name) {
        return new Event(Event.Type.MINIMIZE_END, name);
    }

    @Override
    public Event createHyperlinkEvent(String data) {
        HyperlinkData linkData = gson.fromJson(data, HyperlinkData.class);
        String metadata = linkData.metadata.toString();
        String type = linkData.type;

        DashboardHyperlink dashboardHyperlink = null;
        if ("ReportExecution".equals(type)) {
            dashboardHyperlink = reportExecLinkFactory.createLink(metadata);
        } else if ("Reference".equals(type)) {
            dashboardHyperlink = referenceHyperlinkFactory.createLink(metadata);
        }

        return new Event(Event.Type.HYPERLINK_CLICK, dashboardHyperlink);
    }

    private static class HyperlinkData {
        private String type;
        private JsonObject metadata;
    }
}
