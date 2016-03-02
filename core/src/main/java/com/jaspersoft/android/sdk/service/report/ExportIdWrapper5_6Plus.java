package com.jaspersoft.android.sdk.service.report;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class ExportIdWrapper5_6Plus extends ExportIdWrapper {
    private static class SingletonHolder {
        private static final ExportIdWrapper INSTANCE = new ExportIdWrapper5_6Plus();
    }

    /**
     * Initialization-on-demand holder idiom
     * <p/>
     * <a href="https://en.wikipedia.org/wiki/Singleton_pattern">SOURCE</a>
     *
     * @return export id wrapper
     */
    public static ExportIdWrapper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private ExportIdWrapper5_6Plus() {
    }

    @Override
    public String getServerId() {
        return mExportDetails.getExportId();
    }

    @Override
    public String getExactId() {
        return mExportDetails.getExportId();
    }
}
