package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardComponentCollection;
import com.jaspersoft.android.sdk.network.entity.dashboard.InputControlDashboardComponent;
import org.jetbrains.annotations.TestOnly;

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class ControlLocationMapper {
    public List<ControlLocation> transform(String dashboardUri, DashboardComponentCollection componentCollection) {
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

    @TestOnly
    String generateUri(String ownerResourceId, String dashboardUri) {
        if (ownerResourceId.contains("temp")) {
            String modifiedUri = ownerResourceId.replaceAll("^/temp", "");
            return dashboardUri + "_files" + modifiedUri;
        }
        return ownerResourceId;
    }
}
