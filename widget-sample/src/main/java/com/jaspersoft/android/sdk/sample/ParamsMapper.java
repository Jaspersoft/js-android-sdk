package com.jaspersoft.android.sdk.sample;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public enum ParamsMapper {

    INSTANCE;

    public List<ReportParameter> fromJson(String data) {
        JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();

        List<ReportParameter> params = new ArrayList<>(entries.size());
        for (Map.Entry<String, JsonElement> entry : entries) {
            JsonElement entryValue = entry.getValue();
            JsonArray asJsonArray = entryValue.getAsJsonArray();
            Set<String> values = parseArray(asJsonArray);
            params.add(new ReportParameter(entry.getKey(), values));
        }

        return params;
    }

    private Set<String> parseArray(JsonArray array) {
        int size = array.size();
        Set<String> values = new HashSet<>(size);
        for (int i = 0; i < size; i++) {
            JsonElement jsonElement = array.get(i);
            JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();
            if (primitive.isString()) {
                values.add(primitive.getAsString());
            } else {
                values.add(primitive.toString());
            }
        }
        return values;
    }
}
