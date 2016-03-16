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
