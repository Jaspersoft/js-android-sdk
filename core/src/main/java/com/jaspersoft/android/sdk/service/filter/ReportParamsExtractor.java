package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.entity.control.InputControl;
import com.jaspersoft.android.sdk.network.entity.control.InputControlOption;
import com.jaspersoft.android.sdk.network.entity.control.InputControlState;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class ReportParamsExtractor {

    public List<ReportParameter> extractSelectedParams(List<InputControl> controls) {
        List<ReportParameter> parameters = new ArrayList<>();
        for (InputControl inputControl : controls) {
            Set<String> values = extractSelectedValue(inputControl);
            InputControlState state = inputControl.getState();
            ReportParameter parameter = new ReportParameter(state.getId(), values);
            parameters.add(parameter);
        }
        return parameters;
    }

    private Set<String> extractSelectedValue(InputControl inputControl) {
        Set<String> values = new HashSet<>();
        String type = inputControl.getType();
        InputControlState state = inputControl.getState();

        boolean singleSelect = type.contains("singleSelect");
        boolean multiSelect = type.contains("multiSelect");
        if (singleSelect || multiSelect) {
            for (InputControlOption option : state.getOptions()) {
                if (option.isSelected()) {
                    values.add(option.getValue());
                    if (singleSelect) {
                        return values;
                    }
                }
            }
        }
        values.add(state.getValue());
        return values;
    }
}
