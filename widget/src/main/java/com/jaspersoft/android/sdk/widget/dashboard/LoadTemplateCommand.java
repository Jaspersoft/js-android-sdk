package com.jaspersoft.android.sdk.widget.dashboard;

import com.jaspersoft.android.sdk.widget.RunOptions;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class LoadTemplateCommand implements Command {

    private final RunOptions options;

    LoadTemplateCommand(RunOptions options) {
        this.options = options;
    }

    public RunOptions getOptions() {
        return options;
    }
}
