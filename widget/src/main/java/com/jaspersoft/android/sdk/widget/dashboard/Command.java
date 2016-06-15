package com.jaspersoft.android.sdk.widget.dashboard;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.RunOptions;

/**
 * @author Tom Koptel
 * @since 2.6
 */
interface Command {
    interface Factory {
        Command createInitCommand(RunOptions options);

        Command createRunCommand(RunOptions options);

        Command createMinimizeCommand(WebView webView);
    }
}
