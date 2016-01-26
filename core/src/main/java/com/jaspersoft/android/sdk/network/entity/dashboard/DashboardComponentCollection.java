package com.jaspersoft.android.sdk.network.entity.dashboard;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class DashboardComponentCollection {
    private final List<InputControlDashboardComponent> mInputControlComponents;

    public DashboardComponentCollection(List<InputControlDashboardComponent> inputControlComponents) {
        mInputControlComponents = inputControlComponents;
    }

    public List<InputControlDashboardComponent> getInputControlComponents() {
        return mInputControlComponents;
    }
}
