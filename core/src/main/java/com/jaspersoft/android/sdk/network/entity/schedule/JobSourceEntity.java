package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class JobSourceEntity {
    @Expose
    private String reportUnitURI;
    @Expose
    private JobSourceParamsWrapper parameters;

    public String getReportUnitURI() {
        return reportUnitURI;
    }

    public void setReportUnitURI(String reportUnitURI) {
        this.reportUnitURI = reportUnitURI;
    }

    public Map<String, Set<String>> getParameters() {
        if (parameters == null) {
            return null;
        }
        return parameters.getParameterValues();
    }

    public void setParameters(Map<String, Set<String>> values) {
        if (parameters == null) {
            parameters = new JobSourceParamsWrapper();
        }
        parameters.setParameterValues(values);
    }
}
