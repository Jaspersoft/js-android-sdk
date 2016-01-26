package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobSourceParamsWrapper {
    @Expose
    private Map<String, Set<String>> parameterValues;

    public Map<String, Set<String>> getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(Map<String, Set<String>> parameterValues) {
        this.parameterValues = parameterValues;
    }
}
