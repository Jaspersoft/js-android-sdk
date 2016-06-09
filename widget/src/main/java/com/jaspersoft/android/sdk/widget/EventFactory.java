package com.jaspersoft.android.sdk.widget;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class EventFactory implements Event.Factory {
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
    public Event createWindowErrorEvent(WindowError error) {
        return new Event(Event.Type.WINDOW_ERROR, error);
    }
}
