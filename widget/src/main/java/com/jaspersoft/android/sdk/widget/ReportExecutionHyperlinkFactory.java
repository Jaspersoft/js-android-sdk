package com.jaspersoft.android.sdk.widget;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class ReportExecutionHyperlinkFactory extends Hyperlink.Factory<ReportExecutionHyperlink> {
    private final Gson gson = new Gson();

    @Override
    ReportExecutionHyperlink createLink(String data) {
        ExecutionMetadata executionMetadata = gson.fromJson(data, ExecutionMetadata.class);
        List<ReportParameter> parameters = toParams(executionMetadata.params);
        return new ReportExecutionHyperlink(
                executionMetadata.uri,
                executionMetadata.page,
                executionMetadata.anchor,
                parameters
        );
    }

    private List<ReportParameter> toParams(Map<String, Set<String>> data) {
        List<ReportParameter> parameters = new ArrayList<>(data.size());
        for (Map.Entry<String, Set<String>> entry : data.entrySet()) {
            parameters.add(new ReportParameter(entry.getKey(), entry.getValue()));
        }
        return parameters;
    }

    private static class ExecutionMetadata {
        private String uri;
        private String anchor;
        private Integer page;
        private String output;
        private Map<String, Set<String>> params;
    }
}
