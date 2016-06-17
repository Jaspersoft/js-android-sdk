package com.jaspersoft.android.sdk.widget.report.v2;

import com.jaspersoft.android.sdk.widget.WindowError;
import com.jaspersoft.android.sdk.widget.report.v2.RestJavascriptEvent.Type;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class RestJavascriptEventFactory extends JavascriptEventFactory {
    public JavascriptEvent createInflateCompleteEvent() {
        return new RestJavascriptEvent(Type.INFLATE_COMPLETE);
    }

    public JavascriptEvent createReportLoadedEvent() {
        return new RestJavascriptEvent(Type.REPORT_LOADED);
    }

    public JavascriptEvent createWindowErrorEvent(String data) {
        WindowError error = fromJson(data, WindowError.class);
        return new RestJavascriptEvent(Type.WINDOW_ERROR, error);
    }
}
