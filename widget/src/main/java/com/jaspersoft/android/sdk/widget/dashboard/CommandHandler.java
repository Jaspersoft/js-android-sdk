package com.jaspersoft.android.sdk.widget.dashboard;

/**
 * @author Tom Koptel
 * @since 2.6
 */
interface CommandHandler<C extends Command> {
    void handle(C command);

    interface Factory {
        CommandHandler<LoadTemplateCommand> loadTemplateCommandHandler();

        CommandHandler<RunCommand> runCommandHandler();

        CommandHandler<MinimizeCommand> minimizeCommandHandler();
    }
}
