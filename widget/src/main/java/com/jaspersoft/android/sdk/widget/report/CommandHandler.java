package com.jaspersoft.android.sdk.widget.report;

/**
 * @author Tom Koptel
 * @since 2.6
 */
interface CommandHandler<C extends Command> {
    void handle(C command);

    interface Factory {
    }
}
