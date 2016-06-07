package com.jaspersoft.android.sdk.widget;

/**
 * @author Tom Koptel
 * @since 2.5
 */
interface Command {
    interface Factory {
        Command create();
    }
}
