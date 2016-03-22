package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
enum ReportParamsMapper {
    INSTANCE;

    public Map<String, Set<String>> toMap(List<ReportParameter> parameters) {
        int capacity = parameters.size();
        Map<String, Set<String>> params = new HashMap<>(capacity);
        for (int i = 0; i < capacity; i++) {
            ReportParameter param = parameters.get(i);
            params.put(param.getName(), param.getValue());
        }
        return params;
    }
}
