package com.jaspersoft.android.sdk.widget.report;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class Event {
    private final Event.Type type;
    private final Object[] data;

    Event(Event.Type type, Object... data) {
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

    public Event.Type getType() {
        return type;
    }

    enum Type {
        INFLATE_COMPLETE,
        SCRIPT_LOADED,
        REPORT_LOADED,
        WINDOW_ERROR,
        HYPERLINK_CLICK;
    }

    interface Factory {
        Event createInflateCompleteEvent();

        Event createScriptLoadedEvent();

        Event createReportLoadedEvent();

        Event createWindowErrorEvent(String errorLog);
    }
}
