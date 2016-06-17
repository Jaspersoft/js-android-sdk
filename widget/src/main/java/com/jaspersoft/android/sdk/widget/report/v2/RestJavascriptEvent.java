package com.jaspersoft.android.sdk.widget.report.v2;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class RestJavascriptEvent extends JavascriptEvent {
    private final Type type;

    RestJavascriptEvent(Type type, Object... data) {
        super(data);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "RestJavascriptEvent{" +
                "type=" + type +
                '}';
    }

    enum Type {
        REPORT_LOADED,
        WINDOW_ERROR;
    }
}
