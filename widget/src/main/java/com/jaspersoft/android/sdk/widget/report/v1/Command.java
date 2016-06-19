package com.jaspersoft.android.sdk.widget.report.v1;

import com.jaspersoft.android.sdk.widget.RunOptions;

/**
 * @author Tom Koptel
 * @since 2.6
 */
interface Command {
    interface Factory {
        Command createLoadTemplateCommand(RunOptions options);

        Command createRunCommand(RunOptions options, double version);
    }
}
