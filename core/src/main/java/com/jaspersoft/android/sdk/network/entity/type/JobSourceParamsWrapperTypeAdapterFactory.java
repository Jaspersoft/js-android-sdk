package com.jaspersoft.android.sdk.network.entity.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSourceParamsWrapper;

import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class JobSourceParamsWrapperTypeAdapterFactory extends CustomizedTypeAdapterFactory<JobSourceParamsWrapper> {
    public JobSourceParamsWrapperTypeAdapterFactory() {
        super(JobSourceParamsWrapper.class);
    }


    @Override
    protected JsonElement afterRead(JsonElement element) {
        if (element.isJsonNull()) {
            return element;
        }

        JsonObject params = new JsonObject();
        JsonObject root = element.getAsJsonObject();
        JsonObject parameterValues = root.get("parameterValues").getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> valuesSet = parameterValues.entrySet();
        for (Map.Entry<String, JsonElement> entry : valuesSet) {
            String paramKey = entry.getKey();
            JsonObject valuesWrapper = entry.getValue().getAsJsonObject();

            JsonArray values = valuesWrapper.get("item").getAsJsonArray();
            params.add(paramKey, values);
        }

        root.remove("parameterValues");
        root.add("parameterValues", params);

        return element;
    }
}
