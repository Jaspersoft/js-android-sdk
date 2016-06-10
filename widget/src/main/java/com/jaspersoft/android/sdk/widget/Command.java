package com.jaspersoft.android.sdk.widget;

import android.webkit.WebView;

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
