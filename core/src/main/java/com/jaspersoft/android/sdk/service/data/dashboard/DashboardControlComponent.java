/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.data.dashboard;

import org.jetbrains.annotations.NotNull;

/**
 * @author Tom Koptel
 * @since 2.3
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
