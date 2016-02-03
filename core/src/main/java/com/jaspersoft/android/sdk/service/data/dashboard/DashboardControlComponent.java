package com.jaspersoft.android.sdk.service.data.dashboard;

import org.jetbrains.annotations.NotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class DashboardControlComponent {
    @NotNull
    private final String mComponentId;
    @NotNull
    private final String mControlId;

    public DashboardControlComponent(@NotNull String id, @NotNull String controlId) {
        mComponentId = id;
        mControlId = controlId;
    }

    @NotNull
    public String getComponentId() {
        return mComponentId;
    }

    @NotNull
    public String getControlId() {
        return mControlId;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DashboardControlComponent)) return false;

        DashboardControlComponent that = (DashboardControlComponent) o;

        if (mControlId != null ? !mControlId.equals(that.mControlId) : that.mControlId != null) return false;
        if (mComponentId != null ? !mComponentId.equals(that.mComponentId) : that.mComponentId != null) return false;

        return true;
    }

    @Override
    public final int hashCode() {
        int result = mComponentId != null ? mComponentId.hashCode() : 0;
        result = 31 * result + (mControlId != null ? mControlId.hashCode() : 0);
        return result;
    }
}
