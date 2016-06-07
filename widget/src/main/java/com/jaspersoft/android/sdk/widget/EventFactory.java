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
}
