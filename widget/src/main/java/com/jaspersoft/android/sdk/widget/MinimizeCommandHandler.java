package com.jaspersoft.android.sdk.widget;

import android.webkit.WebView;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class MinimizeCommandHandler implements CommandHandler<MinimizeCommand> {
    private static final String COMMAND_SCRIPT = "javascript:MobileClient.instance().dashboard().minimizeDashlet();";

    @Override
    public void handle(MinimizeCommand command) {
        WebView webView = command.getWebView();
        webView.loadUrl(COMMAND_SCRIPT);
    }
}
