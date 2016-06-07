package com.jaspersoft.android.sdk.widget;

/**
 * @author Tom Koptel
 * @since 2.5
 */
interface CommandHandler {
    void handle();

    interface Factory {
        CommandHandler create(Command command);
    }
}
