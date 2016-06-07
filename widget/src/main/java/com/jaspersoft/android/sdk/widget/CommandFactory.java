package com.jaspersoft.android.sdk.widget;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class CommandFactory implements Command.Factory {
    @Override
    public Command createInitCommand(RunOptions options) {
        return new LoadTemplateCommand(options);
    }

    @Override
    public Command createRunCommand(RunOptions options) {
        return new RunCommand(options);
    }
}
