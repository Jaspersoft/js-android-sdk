package com.jaspersoft.android.sdk.widget;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class WindowError {
    private final String message;
    private final String source;
    private final int line;
    private final int column;

    private WindowError(Builder builder) {
        message = builder.message;
        source = builder.source;
        line = builder.line;
        column = builder.column;
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

    public static class Builder {

        private String message;
        private String source;
        private int line;
        private int column;

        public Builder() {}

        private Builder(WindowError error) {
            message = error.message;
            source = error.source;
            line = error.line;
            column = error.column;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder line(int line) {
            this.line = line;
            return this;
        }

        public Builder column(int column) {
            this.column = column;
            return this;
        }

        public WindowError build() {
            return new WindowError(this);
        }
    }
}
