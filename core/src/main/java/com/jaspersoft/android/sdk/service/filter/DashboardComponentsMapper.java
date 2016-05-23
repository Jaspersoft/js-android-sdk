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

package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardComponentCollection;
import com.jaspersoft.android.sdk.network.entity.dashboard.InputControlDashboardComponent;
import com.jaspersoft.android.sdk.service.data.dashboard.DashboardControlComponent;
import org.jetbrains.annotations.TestOnly;

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class DashboardComponentsMapper {
    public List<ControlLocation> toLocations(String dashboardUri, DashboardComponentCollection componentCollection) {
        List<InputControlDashboardComponent> inputControlComponents = componentCollection.getInputControlComponents();
        Map<String, ControlLocation> locationMap = new HashMap<>(inputControlComponents.size());

        for (InputControlDashboardComponent component : inputControlComponents) {
            String ownerResourceId = component.getOwnerResourceId();
            String ownerResourceParameterName = component.getOwnerResourceParameterName();
            String generatedUri = generateUri(ownerResourceId, dashboardUri);

            ControlLocation location = locationMap.get(generatedUri);
            if (location == null) {
                location = new ControlLocation(generatedUri);
                locationMap.put(generatedUri, location);
            }

            location.addId(ownerResourceParameterName);
        }

        return new ArrayList<>(locationMap.values());
    }

    public List<DashboardControlComponent> toComponents(DashboardComponentCollection componentCollection) {
        List<InputControlDashboardComponent> inputControlComponents = componentCollection.getInputControlComponents();
        List<DashboardControlComponent> components = new ArrayList<>(inputControlComponents.size());
        for (InputControlDashboardComponent controlComponent : inputControlComponents) {
            if (controlComponent != null) {
                DashboardControlComponent component = new DashboardControlComponent(
                        controlComponent.getId(),
                        controlComponent.getOwnerResourceParameterName()
                );
                components.add(component);
            }
        }
        return components;
    }

    @TestOnly
    String generateUri(String ownerResourceId, String dashboardUri) {
        if (ownerResourceId.contains("temp")) {
            String modifiedUri = ownerResourceId.replaceAll("^/temp", "");
            return dashboardUri + "_files" + modifiedUri;
        }
        return ownerResourceId;
    }
}
