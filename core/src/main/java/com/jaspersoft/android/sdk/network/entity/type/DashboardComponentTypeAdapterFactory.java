package com.jaspersoft.android.sdk.network.entity.type;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardComponentCollection;
import com.jaspersoft.android.sdk.network.entity.dashboard.InputControlDashboardComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class DashboardComponentTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType() == DashboardComponentCollection.class) {
            return (TypeAdapter<T>) customizeMyClassAdapter(gson, (TypeToken<DashboardComponentCollection>) type);
        }
        return null;
    }

    private TypeAdapter<DashboardComponentCollection> customizeMyClassAdapter(final Gson gson, TypeToken<DashboardComponentCollection> type) {
        final TypeAdapter<DashboardComponentCollection> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
        return new TypeAdapter<DashboardComponentCollection>() {
            @Override
            public void write(JsonWriter out, DashboardComponentCollection value) throws IOException {
                delegate.toJsonTree(value);
            }

            @Override
            public DashboardComponentCollection read(JsonReader in) throws IOException {
                JsonElement tree = elementAdapter.read(in);
                return adaptComponent(tree, gson);
            }
        };
    }

    private DashboardComponentCollection adaptComponent(JsonElement tree, Gson gson) {
        JsonArray array = tree.getAsJsonArray();
        List<InputControlDashboardComponent> components = new ArrayList<>(array.size());

        for (int i = 0, size = array.size(); i < size; i++) {
            JsonObject object = array.get(i).getAsJsonObject();
            InputControlDashboardComponent component = getIcsComponent(gson, object);
            if (component != null) {
                components.add(component);
            }
        }

        return new DashboardComponentCollection(components);
    }

    private InputControlDashboardComponent getIcsComponent(Gson gson, JsonObject object) {
        String type = object.get("type").getAsString();
        if ("inputControl".equals(type)) {
            return gson.fromJson(object, InputControlDashboardComponent.class);
        }
        return null;
    }
}
