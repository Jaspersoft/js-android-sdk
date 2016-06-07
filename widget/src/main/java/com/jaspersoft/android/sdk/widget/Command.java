package com.jaspersoft.android.sdk.widget;

/**
 * @author Tom Koptel
 * @since 2.6
 */
interface Command {
    interface Factory {
        Command createInitCommand(RunOptions options);

        Command createRunCommand(RunOptions options);
    }
}
