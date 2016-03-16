package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;

/**
 * @author Tom Koptel
 * @since 2.3
 */
final class ExportOptionsMapper5_5 extends ExportOptionsMapper {
    ExportOptionsMapper5_5(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public ExecutionRequestOptions transform(ReportExportOptions criteria) {
        ExecutionRequestOptions options = super.transform(criteria);
        options.withIgnorePagination(null);
        options.withBaseUrl(null);
        options.withMarkupType(null);
        return options;
    }
}
