package com.jaspersoft.android.sdk.widget.dashboard;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class WindowError {
    private final String message;
    private final String source;
    private final int line;
    private final int column;

    WindowError(String message, String source, int line, int column) {
        this.message = message;
        this.source = source;
        this.line = line;
        this.column = column;
    }

    public String getMessage() {
        return message;
    }

    public String getSource() {
        return source;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return message + " " + source + " " + line + " " + column;
    }
}
