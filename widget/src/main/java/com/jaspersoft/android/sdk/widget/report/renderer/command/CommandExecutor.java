package com.jaspersoft.android.sdk.widget.report.renderer.command;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
public class CommandExecutor {
    private CommandsList commandsList;

    public CommandExecutor() {
        commandsList = new CommandsList();
    }

    public final void execute(Command command) {
        cancelPrevious(command.getClass());
        commandsList.add(command);
        command.execute();
    }

    public final void cancelExecution() {
        for (Command command : commandsList) {
            command.cancel();
        }
    }

    private void cancelPrevious(Class commandType) {
        Command prevCommand = commandsList.get(commandType);
        if (prevCommand != null) {
            prevCommand.cancel();
        }
        commandsList.remove(prevCommand);
    }
}
