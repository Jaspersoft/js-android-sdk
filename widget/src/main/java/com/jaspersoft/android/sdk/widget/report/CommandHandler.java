package com.jaspersoft.android.sdk.widget.report;

/**
 * @author Tom Koptel
 * @since 2.6
 */
interface CommandHandler<C extends Command> {
    void handle(C command);

    void cancel();

    interface Factory {
        CommandHandler<LoadTemplateCommand> createProxyLoadTemplateCommandHandler();

        CommandHandler<LoadTemplateCommand> createLoadTemplateCommandHandler(double version);

        CommandHandler<RunCommand> createRunCommandHandler(double version);
    }
}
