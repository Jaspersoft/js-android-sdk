package com.jaspersoft.android.sdk.widget.report.v2;

import com.jaspersoft.android.sdk.widget.WindowError;
import com.jaspersoft.android.sdk.widget.report.v2.VisJavascriptEvent.Type;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class VisJavascriptEventFactory extends JavascriptEventFactory {

    public JavascriptEvent createInflateCompleteEvent() {
        return new VisJavascriptEvent(Type.INFLATE_COMPLETE);
    }

    public JavascriptEvent createScriptLoadedEvent() {
        return new VisJavascriptEvent(Type.SCRIPT_LOADED);
    }

    public JavascriptEvent createReportLoadedEvent() {
        return new VisJavascriptEvent(Type.REPORT_LOADED);
    }

    public JavascriptEvent createWindowErrorEvent(String data) {
        WindowError error = fromJson(data, WindowError.class);
        return new VisJavascriptEvent(Type.WINDOW_ERROR, error);
    }
}
