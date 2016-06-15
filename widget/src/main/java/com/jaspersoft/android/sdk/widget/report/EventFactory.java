package com.jaspersoft.android.sdk.widget.report;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.widget.WindowError;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class EventFactory implements Event.Factory {
    private final Gson gson = new Gson();

    @Override
    public Event createInflateCompleteEvent() {
        return new Event(Event.Type.INFLATE_COMPLETE);
    }

    @Override
    public Event createScriptLoadedEvent() {
        return new Event(Event.Type.SCRIPT_LOADED);
    }

    @Override
    public Event createReportLoadedEvent() {
        return new Event(Event.Type.REPORT_LOADED);
    }

    @Override
    public Event createWindowErrorEvent(String data) {
        WindowError error = gson.fromJson(data, WindowError.class);
        return new Event(Event.Type.WINDOW_ERROR, error);
    }
}
