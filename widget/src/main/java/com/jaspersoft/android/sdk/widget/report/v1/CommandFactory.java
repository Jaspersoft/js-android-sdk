package com.jaspersoft.android.sdk.widget.report.v1;

import com.jaspersoft.android.sdk.widget.RunOptions;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class CommandFactory implements Command.Factory {
    @Override
    public Command createLoadTemplateCommand(RunOptions options) {
        return new LoadTemplateCommand(options);
    }

    @Override
    public Command createRunCommand(RunOptions options, double version) {
        return new RunCommand(options, version);
    }
}
