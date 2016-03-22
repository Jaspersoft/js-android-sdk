package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.service.data.report.PageRange;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class ExportIdWrapper5_5 extends ExportIdWrapper {
    private static class SingletonHolder {
        private static final ExportIdWrapper INSTANCE = new ExportIdWrapper5_5();
    }

    /**
     * Initialization-on-demand holder idiom
     *
     * <a href="https://en.wikipedia.org/wiki/Singleton_pattern">SOURCE</a>

     * @return export id wrapper
     */
    public static ExportIdWrapper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private ExportIdWrapper5_5() {
    }

    @Override
    public String getServerId() {
        return mExportDetails.getExportId();
    }

    @Override
    public String getExactId() {
        PageRange pageRange = mOptions.getPageRange();
        if (pageRange == null) {
            return mExportDetails.getExportId();
        }

        ReportFormat format = mOptions.getFormat();
        return format + ";pages=" + pageRange;
    }
}
