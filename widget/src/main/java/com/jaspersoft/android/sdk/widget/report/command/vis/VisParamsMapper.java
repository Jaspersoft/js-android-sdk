package com.jaspersoft.android.sdk.widget.report.command.vis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class VisParamsMapper {

    public String mapParams(List<ReportParameter> parameters) {
        Map<String, Set<String>> reportParams = toMap(parameters);

        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Set<String>>>() {
        }.getType();
        return gson.toJson(reportParams, mapType);
    }

    private Map<String, Set<String>> toMap(List<ReportParameter> parameters) {
        if (parameters.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Set<String>> params = new HashMap<>(parameters.size());
        for (ReportParameter parameter : parameters) {
            params.put(parameter.getName(), parameter.getValue());
        }
        return params;
    }
}
