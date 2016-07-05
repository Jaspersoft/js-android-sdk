package com.jaspersoft.android.sdk.widget.report.renderer.command;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
public class CommandExecutor {
    private Command lastCommand;

    public void execute(Command command) {
        lastCommand = command;
        command.execute();
    }

    public final void cancelExecution() {
        if (lastCommand != null) {
            lastCommand.cancel();
        }
    }
}
