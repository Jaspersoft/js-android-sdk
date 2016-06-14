package com.jaspersoft.android.sdk.widget.dashboard;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class Event {

    private final Type type;
    private final Object[] data;

    Event(Type type, Object... data) {
        this.type = type;
        this.data = data;
    }

    public Object[] getData() {
        return data;
    }

    public <T> T firstArg(Class<T> type) {
        if (data[0] == null) {
            return null;
        }
        return type.cast(data[0]);
    }

    public Type getType() {
        return type;
    }

    enum Type {
        INFLATE_COMPLETE,
        SCRIPT_LOADED,
        DASHBOARD_LOADED,
        MAXIMIZE_START,
        MAXIMIZE_END,
        MINIMIZE_START,
        MINIMIZE_END,
        WINDOW_ERROR,
        HYPERLINK_CLICK;
    }

    interface Factory {
        Event createInflateCompleteEvent();

        Event createScriptLoadedEvent();

        Event createDashboardLoadedEvent();

        Event createWindowErrorEvent(String data);

        Event createMaximizeStartEvent(String name);

        Event createMaximizeEndEvent(String name);

        Event createMinimizeStartEvent(String name);

        Event createMinimizeEndEvent(String name);

        Event createHyperlinkEvent(String data);
    }
}
