package com.jaspersoft.android.sdk.widget.report.v2;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class VisJavascriptEvent extends JavascriptEvent {

    private final Type type;

    VisJavascriptEvent(Type type, Object... data) {
        super(data);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "VisJavascriptEvent{" +
                "type=" + type +
                '}';
    }

    enum Type {
        INFLATE_COMPLETE,
        SCRIPT_LOADED,
        REPORT_LOADED,
        WINDOW_ERROR,
        HYPERLINK_CLICK;
    }
}
