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
}
