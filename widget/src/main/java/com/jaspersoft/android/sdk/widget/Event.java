package com.jaspersoft.android.sdk.widget;

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

    public Type getType() {
        return type;
    }

    enum Type {
        INFLATE_COMPLETE,
        SCRIPT_LOADED,
        DASHBOARD_LOADED,
        WINDOW_ERROR;
    }

    interface Factory {
        Event createInflateCompleteEvent();

        Event createScriptLoadedEvent();

        Event createDashboardLoadedEvent();

        Event createWindowErrorEvent(WindowError error);
    }
}
