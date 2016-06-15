package com.jaspersoft.android.sdk.sample.page;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public enum  ReportMessageFactory {
    INSTANCE;

    public String messageFromState(ReportPage page) {
        int state = page.getState();
        switch (state) {
            case ReportPage.State.INITIAL:
                return "Start loading report...";
            case ReportPage.State.TEMPLATE_LOADED:
                return "Finish inflating template...";
            case ReportPage.State.SCRIPT_LOADED:
                return "Finish script loading...";
            case ReportPage.State.REPORT_RENDERED:
                return "Finish report rendering...";
        }
        throw new UnsupportedOperationException("Undefined state: " + state);
    }
}
