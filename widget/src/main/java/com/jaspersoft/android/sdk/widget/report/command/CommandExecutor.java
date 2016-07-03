package com.jaspersoft.android.sdk.widget.report.command;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

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
