package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;

/**
 * @author Tom Koptel
 * @since 2.0
 */
abstract class ExportIdWrapper {
    protected ExportExecutionDescriptor mExportDetails;
    protected ReportExportOptions mOptions;

    public abstract String getServerId();

    public abstract String getExactId();

    public final void wrap(ExportExecutionDescriptor exportDetails, ReportExportOptions options) {
        mExportDetails = exportDetails;
        mOptions = options;
    }
}
