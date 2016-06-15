package com.jaspersoft.android.sdk.widget.dashboard;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ReportExecutionDashboardHyperlink extends DashboardHyperlink {
    private final String uri;
    private final Integer page;
    private final String anchor;
    private final List<ReportParameter> parameters;

    ReportExecutionDashboardHyperlink(String uri, Integer page, String anchor, List<ReportParameter> parameters) {
        super(Type.REPORT_EXECUTION);
        this.uri = uri;
        this.page = page;
        this.anchor = anchor;
        this.parameters = parameters;
    }

    public String getUri() {
        return uri;
    }

    public Integer getPage() {
        return page;
    }

    public String getAnchor() {
        return anchor;
    }

    public List<ReportParameter> getParameters() {
        return parameters;
    }
}
