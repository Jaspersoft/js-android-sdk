package com.jaspersoft.android.sdk.widget;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class Event {

    private final Type type;

    Event(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    enum Type {
        INFLATE_COMPLETE,
        SCRIPT_LOADED;
    }

    interface Factory {
        Event createInflateCompleteEvent();
        Event createScriptLoadedEvent();
    }
}
