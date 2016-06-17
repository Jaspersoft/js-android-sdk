package com.jaspersoft.android.sdk.widget.report.v2;

/**
 * @author Tom Koptel
 * @since 2.6
 */
abstract class JavascriptEvent {
    private final Object[] data;

    protected JavascriptEvent(Object... data) {
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
}
